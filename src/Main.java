//CS 342 - SPRING 2016
//Project 2
//Group 10
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Omaid Khan (okhan23)

//Class: Main.java
//Responsibility: main class, include main() function: used to create and initialize the game.

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				GameUI game = new GameUI();	//create new GameUI
				game.init();				//initialize and run the game
			}
		});
	} //end of main() function
} //end of Main class
