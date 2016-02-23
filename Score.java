//CS 342 - SPRING 2016
//Project 2
//Group 10
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Omaid Khan (okhan23)

//Class: Score.java
//Responsibility: represent a top score: include name and time.

import java.io.Serializable;

@SuppressWarnings("serial")
public class Score implements Serializable{
	public String name;
	public int score;
	
	// Score class constructor
	public Score(String n, int s){
		name = n;
		score = s;
	} //end of constructor
} //end of Score class
