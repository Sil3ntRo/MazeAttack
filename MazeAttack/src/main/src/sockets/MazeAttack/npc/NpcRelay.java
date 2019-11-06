package sockets.MazeAttack.npc;

import java.util.ArrayList;
import java.util.Set;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import sockets.MazeAttack.users.User;

public class NpcRelay extends Thread {
	
	private Set<WebSocket> connections;
	private ArrayList<User> users;
	private NpcBuilder npcBuilder;
	private Gson gson = new Gson();
	private Object lock = new Object();
	
	public NpcRelay(Set<WebSocket> conns, NpcBuilder builder, ArrayList<User> users) {
		this.connections = conns;
		this.npcBuilder = builder;
		this.users = users;
	}
	
	public synchronized void run() {		
		while(true) {			
			try {
				npcBuilder.moveDiscs();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(connections.size() != 0) {
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						updateUsers();
					}
				});
				t1.start();
			};
		}
	}
	
	private void updateUsers() {
		synchronized (lock) {
			try {
				for (WebSocket conn : connections) {	
					ArrayList<Disc> nearByDiscs = new ArrayList<Disc>();
					ArrayList userXY = getUserXY(conn);
					
					// For each user, create array with discs that are nearby
					for(Disc discs :npcBuilder.getDiscs()) {
						if((Math.abs(Integer.parseInt(userXY.get(0).toString()) - discs.getX()) < 1000)	&&
								(Math.abs(Integer.parseInt(userXY.get(1).toString()) - discs.getY()) < 500)) {
							nearByDiscs.add(discs);
						}
					}
					
					// Sends this user, the discs that are near to him
					if(nearByDiscs.size() != 0) {
						conn.send(gson.toJson(nearByDiscs)); 
					}
					
				}	
			} catch (Exception e) {			
				// This will throw Gson concurrent exception with some edges cases.
				// Decided to throw the exception as it'll be picked up on the next relay with no negative affects.
			}

        }	
	}

	
	private ArrayList<Integer> getUserXY(WebSocket conn) {
		User connID = conn.getAttachment();
		ArrayList<Integer> xyDetails = new ArrayList<>();		
		for (User userID : users) {
        	if(userID.getUserId() == connID.getUserId()) {
        		xyDetails.add(userID.getX());
        		xyDetails.add(userID.getY());
        	}        	
        }		
		return xyDetails;
	}
	
	
	
	public Set<WebSocket> getConnections() {
		return connections;
	}

	public void setConnections(Set<WebSocket> connections) {
		this.connections = connections;
	}

	public NpcBuilder getNpcBuilder() {
		return npcBuilder;
	}

	public void setNpcBuilder(NpcBuilder npcBuilder) {
		this.npcBuilder = npcBuilder;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	
	
}
