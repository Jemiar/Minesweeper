//CS 342 - SPRING 2016
//Project 2
//Group 10
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Omaid Khan (okhan23)

//Class: GameUI.java
//Responsibility: create and show GUI, as well as handle action events.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameUI {
		public JFrame frame;			//frame of the whole GUI
		public JPanel mineBoard;		//panel of 100 mines
		public int minesNo;				//number of mines
		public int open;				//number of safe squares cleared, used to determined if user has cleared 90 squares
		public JLabel mineIndicator;	//label to display how many squared have been marked with flag
		public JLabel timeIndicator;	//label display elapsed time
		public int clickCount;			//count how many clicks user has clicked on mine board, used to determine the 1st click
		public int countTime;			//variable storing elapsed time
		public Timer timer;				//Timer object used for running the time
		public ArrayList<Score> top10;	//array list to store top 10 scores
		public JButton restartButton;	//restart button
	
	// GameUI class constructor
	public GameUI(){
		top10 = new ArrayList<Score>();			//create new top 10 scores arraylist
		frame = new JFrame("Minesweeper 1.0");	//create new frame
		mineBoard = new JPanel();				//create new mineboard
		mineIndicator = new JLabel();			//create new mine indicator label
		timeIndicator = new JLabel();			//create new time indicator label
		minesNo = 10;							//set number of mine squares haven't been marked as 10
		open = 0;								//set number of cleared squares to 0
		clickCount = 0;							//set number of click to 0
		countTime = 0;							//set elapsed time to 0
		// create new Timer, fire an event every 1s, and increase elapsed time by 10 and update time indicator label
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				countTime++;
				timeIndicator.setText(((Integer)countTime).toString());
			}
		});
	}// end of GameUI class constructor
	
	// function init() to initialize the game and GUI component
	public void init(){
		
		// read top 10 saved file, if file exist, read the data and store in arraylist
		File file = new File("topten.ser");
		if(file.exists()){
			try{
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				TopTen tt = (TopTen)objIn.readObject();
				top10 = tt.topten;
				objIn.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}else{
			// if file doesn't exist (first time play), create the file, write some blank data to the file, and then read back the file
			try {
				file.createNewFile();
				try{
					FileOutputStream fileOut = new FileOutputStream(file);
					ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
					objOut.writeObject(new TopTen(top10));
					objOut.close();
					FileInputStream fileIn = new FileInputStream(file);
					ObjectInputStream objIn = new ObjectInputStream(fileIn);
					TopTen tt = (TopTen)objIn.readObject();
					top10 = tt.topten;
					objIn.close();
				} catch (Exception ex){
					ex.printStackTrace();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		// set frame attributes
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create and set up the menu bar
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		
		// Menu item: Start new game (under File), when clicked restart the game
		JMenuItem restartGame = new JMenuItem("New game", 'N');
		restartGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restart();
			}
		});
		fileMenu.add(restartGame);
		
		// Menu item: Show top 10 score. When clicked, build a string from arraylist, and then display to a message dialogue
		JMenuItem topScore = new JMenuItem("Top 10 scores", 'T');
		topScore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					FileInputStream fileIn = new FileInputStream(file);
					ObjectInputStream objIn = new ObjectInputStream(fileIn);
					TopTen tt = (TopTen)objIn.readObject();
					top10 = tt.topten;
					objIn.close();
				} catch (Exception ex){
					ex.printStackTrace();
				}
				// if there is no data in topten.ser, show nothing
				if(top10.isEmpty())
					JOptionPane.showMessageDialog(null, "");
				// if there is data, build the string and display the result
				else{
					StringBuilder sb = new StringBuilder();
					sb.append(" " + "Name" + "\t\t\t\t\t" + " Time\n");
					for(int i = 0; i < top10.size(); i++){
						sb.append(" " + (i+1) + "." + top10.get(i).name + "\t\t\t\t\t" + top10.get(i).score + "\n");
					}
					JOptionPane.showMessageDialog(null, sb);
				}
			}
		});
		fileMenu.add(topScore);
		
		//Menu item: reset top 10 scores: build a new empty arraylist, and overwrite the topten.ser file
		JMenuItem resetTopScore = new JMenuItem("Reset top 10 scores", 'R');
		resetTopScore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					FileOutputStream fileOut = new FileOutputStream("topten.ser");
					ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
					top10 = new ArrayList<Score>();
					objOut.writeObject(new TopTen(top10));
					objOut.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		fileMenu.add(resetTopScore);
		
		//Menu item: exit: when click, show confirm dialogue, close the program based on the response
		JMenuItem exit = new JMenuItem("EXit", 'X');
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int quit = JOptionPane.showConfirmDialog(null, "Are you sure to quit the game?", "", JOptionPane.YES_NO_OPTION);
				if(quit == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		fileMenu.add(exit);
		
		//Menu item: help: show how to play the game
		JMenuItem help = new JMenuItem("HeLp", 'L');
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String h = "Leftclick to reveal a square.\n"
						+ "Rightclick to mark a mine.\n"
						+ "Rightclick again if in doubt.\n"
						+ "Click restart button to start a new game.\n"
						+ "Click on File/New game to start a new game.\n"
						+ "Click on File/Top 10 scores to show 10 fastest times.\n"
						+ "Click on File/Reset top 10 scores to reset scores.\n"
						+ "Click on File/Quit to quit the game.\n"
						+ "Click on Help/Help for how to play game.\n"
						+ "Click on Help/About to show info about the game.";
				JOptionPane.showMessageDialog(null, h);
			}
		});
		helpMenu.add(help);
		
		//Menu item: show info about the game and developers
		JMenuItem about = new JMenuItem("About", 'A');
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String h = "Minesweeper version 1.0\n"
						+ "Created by Hoang Minh Huynh Nguyen and Omaid Khan.\n"
						+ "GNU License. 2016";
				JOptionPane.showMessageDialog(null, h);
			}
		});
		helpMenu.add(about);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		//set menu bar for frame
		frame.setJMenuBar(menuBar);
		
		//jpanel infoBar includes: mine indicator label, restart button and time indicator label
		JPanel infoBar = new JPanel();
		infoBar.setLayout(new GridLayout());
		
		mineIndicator.setText(((Integer)minesNo).toString());
		mineIndicator.setHorizontalAlignment(SwingConstants.CENTER);
		infoBar.add(mineIndicator);
		
		//restart button, execute restart() function when clicked
		restartButton = new JButton("");
		restartButton.setPreferredSize(new Dimension(30, 30));
		restartButton.setIcon(new ImageIcon("smile.png"));
		restartButton.setFocusPainted(false);
		restartButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restart();
			}
		});
		infoBar.add(restartButton);
		
		timeIndicator.setText(((Integer)countTime).toString());
		timeIndicator.setHorizontalAlignment(SwingConstants.CENTER);
		infoBar.add(timeIndicator);
		
		frame.add(infoBar, BorderLayout.NORTH);
		
		//create new mineboard with setBoard() function, and assign to mineBoard
		mineBoard = setBoard();
		frame.add(mineBoard);
		frame.revalidate();
		
		// show the frame
		frame.pack();
		frame.setVisible(true);
	} //end of init() function
	
	// writeScore function: add new score to arraylist, sort the arraylist, if size > 10, clear sublist and then write to the file
	public void writeScore(String n, int sc){
		try{
			FileOutputStream fileOut = new FileOutputStream("topten.ser");
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			top10.add(new Score(n, sc));
			Collections.sort(top10, new Comparator<Score>(){
				public int compare(Score s1, Score s2){
					if(s1.score < s2.score)
						return -1;
					else
						if(s1.score > s2.score)
							return 1;
						else
							return 0;
				}
			});
			if(top10.size() > 10)
				top10.subList(10, top10.size()).clear();
			objOut.writeObject(new TopTen(top10));
			objOut.close();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	} //end of writeScore() function;
	
	// restart() function: reset all value to initial value, create new GUI and show
	public void restart(){
		restartButton.setIcon(new ImageIcon("smile.png"));
		minesNo = 10;
		mineIndicator.setText(((Integer)minesNo).toString());
		
		open = 0;
		
		clickCount = 0;
		countTime = 0;
		timer.stop();
		timeIndicator.setText(((Integer)countTime).toString());
		
		frame.remove(mineBoard);
		mineBoard = setBoard();
		frame.add(mineBoard);
		frame.revalidate();
		frame.pack();
		frame.setVisible(true);
	} //end of restart() function
	
	// setBoard(): create JPanel with 100 button, then return the resulting JPanel
	public JPanel setBoard(){
		JPanel board = new JPanel();
		//create 12x12 buttons, use the edge for easy calculation
		Square[][] field = new Square[12][12];
		board.setLayout(new GridLayout(10, 10));
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 12; j++){
				field[i][j] = new Square(i, j);
				field[i][j].setText("");
				field[i][j].setFocusPainted(false);
				field[i][j].setPreferredSize(new Dimension(30, 30));
			}
		}
		//place 10 mines on to the field
		placeMine(field);
		//calculate number of mines in adjacent squares
		calMine(field);
		
		//add mouse listener to buttons
		for(int i = 1; i < 11; i++)
			for(int j = 1; j < 11; j++)
				field[i][j].addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e){
						//if the first click, start the clock
						if(clickCount == 0)
							timer.start();
						clickCount++;
						if(SwingUtilities.isLeftMouseButton(e)){ //if left click
							if(((Square)e.getSource()).rClickCount == 0 && ((Square)e.getSource()).isEnabled()){ //if left click is possible
								if(((Square)e.getSource()).hasMine)
									explode(field);//if click on mine, explode
								else{
									if(((Square)e.getSource()).minesArd == 0){ //if click on square with no mine adjacent, show all around as well
										int r = ((Square)e.getSource()).xPos;
										int c = ((Square)e.getSource()).yPos;
										reveal(field, r, c);
									}else{
										((Square)e.getSource()).setTxt(); // if click and have mine adjacent, only reveal the clicked square
										((Square)e.getSource()).setEnabled(false);
										open++;
										if(open == 90){ //check if all safe squares have been clicked, if yes finish the game
											finish(field);
										}
									}
								}
							}
						}
						if(SwingUtilities.isRightMouseButton(e)){ // if right click
							rightClick(e);
						}
					}
				});
		
		for(int i = 1; i < 11; i++){
			for(int j = 1; j < 11; j++){
				board.add(field[i][j]);
			}
		}
		return board;
	} //end of setBoard() function
	
	// finish() function: stop the clock, set all button unclickable
	public void finish(Square[][] f){
		timer.stop();
		for(int i = 1; i < 11; i++){
			for(int j = 1; j < 11; j++){
				if(!f[i][j].hasMine){
					f[i][j].setTxt();
				}
				f[i][j].setEnabled(false);
			}
		}
		// check top 10 score to write to topten10 file
		if(top10.size() < 10 || (top10.size() == 10 && top10.get(9).score > countTime)){
			String response = JOptionPane.showInputDialog("You are in the Top 10. Please enter your name");
			if(response != null && response.length() > 0)
				writeScore(response, countTime);
		}else{
			JOptionPane.showMessageDialog(null, "Minefield cleared. Mission complete");
		}
	}
	
	//rightClick() function: change icon flag, question mark, normal square
	public void rightClick(MouseEvent e){
		if(((Square)e.getSource()).isEnabled()){
			((Square)e.getSource()).rClickCount = (((Square)e.getSource()).rClickCount + 1) % 3;
			switch(((Square)e.getSource()).rClickCount){
				case 0:
					((Square)e.getSource()).setText("");
					break;
				case 1:
					{
						((Square)e.getSource()).setIcon(new ImageIcon("flag.png"));;
						minesNo--;
						mineIndicator.setText(((Integer)minesNo).toString());
					}
					break;
				case 2:
					{
						((Square)e.getSource()).setIcon(null);
						((Square)e.getSource()).setText("?");
						minesNo++;
						mineIndicator.setText(((Integer)minesNo).toString());
					}
					break;
			}//end of switch
		}
	} //end of rightClick() function
	
	// reveal() function: reveal adjacent squares if no mine around. Propagate: if there is such square, add to stack, then pop and check
	public void reveal(Square[][] f, int r, int c){
		Stack<Square> zero = new Stack<Square>();
		zero.push(f[r][c]);
		f[r][c].setTxt();
		f[r][c].setEnabled(false);
		if(r > 0 && r < 11 && c > 0 && c < 11){
			open++;
			if(open == 90)
				finish(f);
		}
		while(!zero.empty()){
			Square s = zero.pop();
			int m = s.xPos;
			int n = s.yPos;
			if(m > 0 && m < 11 && n > 0 && n < 11){
				for(int k = n-1; k < n+2; k++){
					if(f[m-1][k].rClickCount == 0){
						f[m-1][k].setTxt();
						if((m-1) > 0 && (m-1) < 11 && k > 0 && k < 11 && f[m-1][k].isEnabled()){
							open++;
							if(open == 90)
								finish(f);
						}
						if(!f[m-1][k].hasMine && f[m-1][k].minesArd == 0 && f[m-1][k].isEnabled())
							zero.push(f[m-1][k]);
						f[m-1][k].setEnabled(false);
					}
					
					if(f[m+1][k].rClickCount == 0){
						f[m+1][k].setTxt();
						if((m+1) > 0 && (m+1) < 11 && k > 0 && k < 11 && f[m+1][k].isEnabled()){
							open++;
							if(open == 90)
								finish(f);
						}
						if(!f[m+1][k].hasMine && f[m+1][k].minesArd == 0 && f[m+1][k].isEnabled())
							zero.push(f[m+1][k]);
						f[m+1][k].setEnabled(false);
					}
				}
				
				if(f[m][n-1].rClickCount == 0){
					f[m][n-1].setTxt();
					if(m > 0 && m < 11 && (n-1) > 0 && (n-1) < 11 && f[m][n-1].isEnabled()){
						open++;
						if(open == 90)
							finish(f);
					}
					if(!f[m][n-1].hasMine && f[m][n-1].minesArd == 0 && f[m][n-1].isEnabled())
						zero.push(f[m][n-1]);
					f[m][n-1].setEnabled(false);
				}
				
				if(f[m][n+1].rClickCount == 0){
					f[m][n+1].setTxt();
					if(m > 0 && m < 11 && (n+1) > 0 && (n+1) < 11 && f[m][n+1].isEnabled()){
						open++;
						if(open == 90)
							finish(f);
					}
					if(!f[m][n+1].hasMine && f[m][n+1].minesArd == 0 && f[m][n+1].isEnabled())
						zero.push(f[m][n+1]);
					f[m][n+1].setEnabled(false);
				}
			}else
				continue;
		}
	} //end of reveal() function
	
	// explode() function: show all the mines, set restart button icon to sad face, disable all squares
	public void explode(Square[][] f){
		timer.stop();
		restartButton.setIcon(new ImageIcon("boom.png"));
		for(int i = 1; i < 11; i++){
			for(int j = 1; j < 11; j++){
				if(f[i][j].hasMine){
					f[i][j].setIcon(new ImageIcon("bomb.png"));
				}
				f[i][j].setEnabled(false);
			}
		}
	} //end of explode() function
	
	// placeMine() function: randomly place 10 mines on the board
	public void placeMine(Square[][] f){
		Random rand = new Random();
		int count = 10;
		while(count > 0){
			int v = rand.nextInt(118) + 13;
			int j = v % 12;
			int i = (v - j) / 12;
			if(!f[i][j].hasMine && (i > 0 && i < 11) && (j > 0 && j < 11)){
				f[i][j].hasMine = true;
				count--;
			}
		} //end of while
	} //end of placeMine() function
	
	// calMine() function: check adjacent squares, and count mines around
	public void calMine(Square[][] f){
		for(int i = 1; i < 11; i++){
			for(int j = 1; j < 11; j++){
				if(!f[i][j].hasMine){
					for(int k = j-1; k < j+2; k++){
						if(f[i-1][k].hasMine)
							f[i][j].minesArd += 1;
						if(f[i+1][k].hasMine)
							f[i][j].minesArd += 1;
					}
					if(f[i][j-1].hasMine)
						f[i][j].minesArd += 1;
					if(f[i][j+1].hasMine)
						f[i][j].minesArd += 1;
				}
			}
		}
	} //end of calMine() function
} //end of GameUI class
