package sockets.MazeAttack.npc;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


public class NpcBuilder {	
	private int mazeSize;
	private Random random;
	private int noOfDiscs = 80;
	private ArrayList<Disc> discs;	

	public NpcBuilder(int mazeSize) {
		this.random = new Random();
		this.mazeSize = mazeSize;
		this.discs = new ArrayList<Disc>();
		
		populateDiscs();
	}


	private void populateDiscs() {
		for(int i = 0; i < noOfDiscs; i++) {
			String uniqueID = UUID.randomUUID().toString();
			Disc tempDisc = new Disc(mazeSize - 46, random.nextInt(mazeSize - 46), random.nextInt(10) + 8, uniqueID);
			discs.add(tempDisc);
		}		
	}
	
	public void moveDiscs() throws InterruptedException {
		// 1 second / 60(fps) = 16.66, this limits how frequently the npc positions are updated to match the canvas framerate
		Thread.sleep(16);
		// Iterates through the discs and moves them accordingly
		for(int i = discs.size() -1; i >= 0; i--) {
			discs.get(i).move(mazeSize);
		}
	}
	
	public void resetDisc(String hitDiscId) {
		// Takes hit disc ID, removes it from the array and spawns a new disc at random location
		for(int i = discs.size() -1; i >= 0; i--) {
			if(discs.get(i).getDiscId().equals(hitDiscId)){
				discs.remove(i);
				String uniqueID = UUID.randomUUID().toString();
				Disc tempDisc = new Disc(mazeSize - 46, random.nextInt(mazeSize - 46), random.nextInt(10) + 8, uniqueID);
				discs.add(tempDisc);
			}
		}		
	}


	public ArrayList<Disc> getDiscs() {
		return discs;
	}


	public void setDiscs(ArrayList<Disc> discs) {
		this.discs = discs;
	}
	
}
