//CS 342 - SPRING 2016
//Project 2
//Group 10
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Omaid Khan (okhan23)

//Class: Square.java
//Responsibility: Represent a Square, extend from JButton class.

import javax.swing.*;

@SuppressWarnings("serial")
public class Square extends JButton{
	public int xPos;			//x position in the board
	public int yPos;			//y position in the board
	public boolean hasMine;		//if a square has mine
	public int rClickCount;		//number of right click on the square
	public int minesArd;		//number of mines in adjacent squares
	
	// Square class constructor
	public Square(int x, int y){
		xPos = x;
		yPos = y;
		hasMine = false;
		rClickCount = 0;
		minesArd = 0;
	} //end of constructor
	
	// function setTxt(): set text of square according to info if a square has mine, and mines in adjacent squares
	public void setTxt(){
		if(this.hasMine)
			this.setText("M");
		else
			if(this.minesArd == 0)
				this.setText("");
			else
				this.setText(((Integer)minesArd).toString());
	} //end of setTxt() function
} //end of Square class
