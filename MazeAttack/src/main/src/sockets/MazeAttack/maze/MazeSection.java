package sockets.MazeAttack.maze;

public class MazeSection {
	
	private int x;
	private int y;
	private boolean wallU;
	private boolean wallR;
	private boolean wallD;
	private boolean wallL;
	
	public MazeSection() {
		
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
	public boolean isWallU() {
		return wallU;
	}
	public void setWallU(boolean wallU) {
		this.wallU = wallU;
	}
	public boolean isWallR() {
		return wallR;
	}
	public void setWallR(boolean wallR) {
		this.wallR = wallR;
	}
	public boolean isWallD() {
		return wallD;
	}
	public void setWallD(boolean wallD) {
		this.wallD = wallD;
	}
	public boolean isWallL() {
		return wallL;
	}
	public void setWallL(boolean wallL) {
		this.wallL = wallL;
	}
	
	
	
	
}
