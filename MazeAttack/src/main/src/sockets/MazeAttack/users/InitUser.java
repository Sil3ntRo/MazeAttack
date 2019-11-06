package sockets.MazeAttack.users;

import org.java_websocket.WebSocket;

public class InitUser {
	private String msgType;
	private String myId;
	private int red;
	private int green;
	private int blue;
	
	public InitUser(User connID) {
		msgType = "initUser";
		myId = connID.getUserId();
		red = connID.getRed();
		green = connID.getGreen();
		blue = connID.getBlue();
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMyId() {
		return myId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}


	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	
}
