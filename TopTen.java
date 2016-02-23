//CS 342 - SPRING 2016
//Project 2
//Group 10
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Omaid Khan (okhan23)

//Class: TopTen.java
//Responsibility: represent the list of top ten scores to write to file.

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TopTen implements Serializable{
	public ArrayList<Score> topten;
	
	// TopTen class constructor
	public TopTen(ArrayList<Score> t){
		topten = new ArrayList<Score>(t);
	} //end of constructor
	
	//function addScore(): add a score to the arraylist
	public void addScore(Score s){
		topten.add(s);
	} //end of addScore() function
} //end of TopTen class
