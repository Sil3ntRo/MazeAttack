package sockets.MazeAttack.npc;

public class Disc {
	private String msgType;
	private String discId;
	private int x;
	private int y;
	private int dia;
	private int speed;
	private boolean movingLeft;
	
	public Disc(int x, int y, int speed, String discId) {
		this.discId = discId;
		this.msgType = "npcDisc";
		this.x = x;
		this.y = y;		
		this.speed = speed;		
		this.dia = 91;
		this.movingLeft = true;
	}
	
	
	public void move(int mazeSize) {
		if(movingLeft == true) {
			int newPos = this.x - speed;
			if(newPos <= dia / 2) {
				movingLeft = false;
			} else {
				this.x = newPos;
			}			
		}
		
		if(movingLeft == false) {
			int newPos = this.x + speed;
			if(newPos >= mazeSize - (dia/ 2)) {
				movingLeft = true;
			} else {
				this.x = newPos;
			}			
		}			
	}


	public String getDiscId() {
		return discId;
	}


	public void setDiscId(String discId) {
		this.discId = discId;
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
		
	
	
}
