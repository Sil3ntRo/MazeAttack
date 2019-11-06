package sockets.MazeAttack.maze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class MazeBuilder {	
	private int mazeSections = 20; // confirms the number of sections in the maze.json (20 x 20)
	private int scale = 200; // sets the pixel width/height of each grid section
	private ArrayList<MazeWall> maze;
	private Gson gson;
	
	public MazeBuilder() {
		this.gson = new Gson();
		this.maze = new ArrayList<MazeWall>();
		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void readFile() throws IOException {
		// Loads maze.json file
		//InputStream in = getClass().getResourceAsStream("sockets/MazeAttack/maze/maze.json"); // used for compiled JAR file	
		//JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in))); // used for compiled JAR file	
		JsonReader reader = new JsonReader(new FileReader("src/main/java/sockets/MazeAttack/maze/maze.json")); // used for running in IDE
		
		// Converts the json file into an array of objects (MazeSections)
		ArrayList<MazeSection> download = gson.fromJson(reader, new TypeToken<ArrayList<MazeSection>>(){}.getType());
		
		// Constructs Maze by looping through the array
		for(int i = 0; i < download.size(); i++) { 								
			if (download.get(i).isWallU() == true) {
				
				int x1 = download.get(i).getX() * this.scale;
				int y1 = download.get(i).getY() * this.scale;
				
				int x2 = (download.get(i).getX() + 1) * this.scale;
				int y2 = download.get(i).getY() * this.scale;
				maze.add(new MazeWall(x1,y1,x2,y2));
			}
			if (download.get(i).isWallR() == true) {	
				
				int x1 = (download.get(i).getX() + 1) * this.scale;
				int y1 = download.get(i).getY() * this.scale;
				
				int x2 = (download.get(i).getX() + 1) * this.scale;
				int y2 = (download.get(i).getY() + 1) * this.scale;
				maze.add(new MazeWall(x1,y1,x2,y2));
			}
			if (download.get(i).isWallD() == true) {
				
				int x1 = download.get(i).getX() * this.scale;
				int y1 = (download.get(i).getY() + 1) * this.scale;
				
				int x2 = (download.get(i).getX() + 1) * this.scale;
				int y2 = (download.get(i).getY() + 1) * this.scale;
				maze.add(new MazeWall(x1,y1,x2,y2));
			}
			if (download.get(i).isWallL() == true) {
				
				int x1 = download.get(i).getX() * this.scale;
				int y1 = download.get(i).getY() * this.scale;
				
				int x2 = download.get(i).getX() * this.scale;
				int y2 = (download.get(i).getY() + 1) * this.scale;
				maze.add(new MazeWall(x1,y1,x2,y2));				
			}

		}
		
	}
	
	public ArrayList<MazeWall> getMaze(){
		return this.maze;
	}


	public int getScale() {
		return scale;
	}


	public void setScale(int scale) {
		this.scale = scale;
	}


	public int getMazeSections() {
		return mazeSections;
	}


	public void setMazeSections(int mazeSections) {
		this.mazeSections = mazeSections;
	}

	
	
	
}
