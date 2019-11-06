package sockets.MazeAttack.users;

import org.java_websocket.WebSocket;

public class User {
	
	// Server generated info
	private String msgType;
	private String userId;
	private int colorId;
	private int red;
	private int green;
	private int blue;
	private int r;
	
	// Client generated info
	private int x;
	private int y;
	private String initials;
	private String lastSaid;
	private String hitDiscId;
	private double laserX;
	private double laserY;
	private boolean laserOn;

	public User(String connID) {
		this.userId = connID;
		this.msgType = "relay";
		this.r = 25;
	}
	
	public String getLastSaid() {
		return lastSaid;
	}

	public void setLastSaid(String lastSaid) {
		this.lastSaid = lastSaid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public int getColorId() {
		return colorId;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public String getHitDiscId() {
		return hitDiscId;
	}

	public void setHitDiscId(String hitDiscId) {
		this.hitDiscId = hitDiscId;
	}

	public double getLaserX() {
		return laserX;
	}

	public void setLaserX(double laserX) {
		this.laserX = laserX;
	}

	public double getLaserY() {
		return laserY;
	}

	public void setLaserY(double laserY) {
		this.laserY = laserY;
	}

	public boolean isLaserOn() {
		return laserOn;
	}

	public void setLaserOn(boolean laserOn) {
		this.laserOn = laserOn;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	
	
	
}
