package sockets.MazeAttack;

import java.util.ArrayList;
import java.util.HashSet;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import sockets.MazeAttack.maze.MazeBuilder;
import sockets.MazeAttack.maze.Wall;
import sockets.MazeAttack.npc.NpcBuilder;
import sockets.MazeAttack.npc.NpcRelay;
import sockets.MazeAttack.users.InitUser;
import sockets.MazeAttack.users.User;

public class MazeWebSocketServer extends WebSocketServer {
	
	private static int TCP_PORT = 9999;
	private Set<WebSocket> connections; // using set instead of list so that it contains no duplicate elements
	private Gson gson = new Gson();
	private int blue[];
	private int red[];
	private int green[];
	private Set<Integer> colorLock;
	private ArrayList<User> users;
	private MazeBuilder mazeBuilder;
	private NpcBuilder npcBuilder;
	private NpcRelay npcRelay;
	
	public MazeWebSocketServer() {	
        super(new InetSocketAddress(TCP_PORT));
        
        connections = new HashSet<>();
        users = new ArrayList<>();
        
        red = new int[] {230,60,255,0,245,145,70,240,210,250,0,230,170,255,128,170,128,255,0,128};
        green = new int[] {25,180,225,130,130,30,240,50,245,190,128,190,110,250,0,255,128,215,0,128};
        blue = new int[] {75,75,25,200,48,180,240,230,60,190,128,255,40,200,0,195,0,180,128,128};
        colorLock = new HashSet<>();
        
        mazeBuilder = new MazeBuilder();
        npcBuilder = new NpcBuilder(mazeBuilder.getMazeSections() * mazeBuilder.getScale());
        npcRelay = new NpcRelay(connections, npcBuilder, users);
    }

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {		
		
		// Creates user for that connection
		User user = new User(conn.toString());
		Random random = new Random();
		
		// Sets color for user and locks that color until disconnect
		int colorId = random.nextInt(red.length);
		while(colorLock.contains(colorId)) {
			colorId = random.nextInt(red.length);
		}		
		user.setRed(red[colorId]);
		user.setGreen(green[colorId]);
		user.setBlue(blue[colorId]);	
		user.setColorId(colorId);
		colorLock.add(colorId);
		
		// Adds user to the connection (needed for user ID)
		conn.setAttachment(user);	
		users.add(user);
		
		// Updates connection details
		connections.add(conn);
		npcRelay.setConnections(connections);
		npcRelay.setUsers(users);
		
		
		// Establishes user player position and color
		ArrayList<InitUser> initData = new ArrayList<InitUser>();
		initData.add(new InitUser(user));				
		conn.send(gson.toJson(initData));
		
		// Sends maze to player
		conn.send(gson.toJson(mazeBuilder.getMaze()));
		conn.send(gson.toJson(createWalls()));

		System.out.println("New client connected! IP = " +conn.getRemoteSocketAddress() + " ID = " + conn.toString());
	}
	
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// Removing the player from the list & removing color so it can be re-used
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUserId().equals(conn.toString())) {
				users.remove(i);
				User tempclose = conn.getAttachment();
				colorLock.remove(tempclose.getColorId());
			}
		}
		// Removing connection
		connections.remove(conn);
		npcRelay.setConnections(connections);
		npcRelay.setUsers(users);

		// Updating all other players with new user list (ie, disconnecting him from other clients)
        for (WebSocket user : connections) {
        	user.send(gson.toJson(users)); 	
        }	
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// Updates players details on the server side
		updateUser(conn, message);
		
		try {
			// Refreshes player details to all users
	        for (WebSocket user : connections) {
	        	user.send(gson.toJson(users)); 	
	        	//System.out.println(gson.toJson(users));
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void updateUser(WebSocket conn, String message) {
		User connID = conn.getAttachment();
		User newDetails = gson.fromJson(message, User.class);
		
        for (User userID : users) {
        	if(userID.getUserId() == connID.getUserId()) {
        		// Updating server details with that clients new details
        		userID.setX(newDetails.getX());
        		userID.setY(newDetails.getY());
        		userID.setLastSaid(newDetails.getLastSaid());
        		userID.setLaserX(newDetails.getLaserX());
        		userID.setLaserY(newDetails.getLaserY());
        		userID.setLaserOn(newDetails.isLaserOn());
        		userID.setInitials(newDetails.getInitials());
        	}        	
        }
        
        // If client reports they hit a disc, calls method with hit disc ID.
        if(newDetails.getHitDiscId() != null) {        	
        	npcBuilder.resetDisc(newDetails.getHitDiscId());
        }
	}
	

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
       if (conn != null) {
    	   onClose(conn, 999, "error", false);
        }
	}

	@Override
	public void onStart() {
		npcRelay.start();
	}	
	
	
	
	private ArrayList<Wall> createWalls() {
		ArrayList<Wall> allWalls = new ArrayList<Wall>();		
		// Spawn area
		allWalls.add(new Wall(341.42, -141.42, 200, 0));
		allWalls.add(new Wall(341.42, -341.42, 341.42, -141.42));
		allWalls.add(new Wall(200, -482.84, 341.42, -341.42));
		allWalls.add(new Wall(0, -482.84, 200, -482.84));
		allWalls.add(new Wall(-141.42, -341.42, 0, -482.84));
		allWalls.add(new Wall(-141.42, -141.42, -141.42, -341.42));
		allWalls.add(new Wall(0, 0, -141.42, -141.42));
		return allWalls;
	}
	
	
	
	
	

}
