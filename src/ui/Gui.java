package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import ai.Cache;
import control.Clocks;
import control.Director;
import sound.Sound;
import utilpack.Message;
import utilpack.Pieces;

public class Gui {
	
	private Director director = Director.getInstance();
	private int click = 1;
	private boolean drop = false;
	private boolean warn = true;	
	boolean mute = false;

	private Clocks clocks = Clocks.getInstance();

	//The Frame that's displayed. (Contains the Panels)
	private static JFrame frame = new JFrame();
	
	//Panels that handle the visual for the board object
	private static JPanel board = new JPanel(new GridLayout(4,3));
	private static JPanel handW = new JPanel(new GridLayout(2,3));
	private static JPanel handB = new JPanel(new GridLayout(2,3));
	private static JPanel cols = new JPanel(new GridLayout(1,3));
	private static JPanel rows = new JPanel(new GridLayout(4,1));
	
//	static JPanel sound = new JPanel(new GridLayout(2,1));	
	
	//Arrays of JButtons which handle the visual for the Square objects
	private static JButton[][] squares = new JButton[4][3];
	private static JButton[] dropW = new JButton[6];
	private static JButton[] dropB = new JButton[6];
	
	// dialog windows
	public static JLabel output = new JLabel(" ");
	public static JLabel score = new JLabel(" ");
	static JLabel profile = new JLabel("Player");

	public static JLabel clockB = new JLabel("00:00");
	public static JLabel clockW = new JLabel("00:00");
	
	// components labels
	private static JLabel show = new JLabel("Score:");
	private static JLabel colA = new JLabel("A");
	private static JLabel colB = new JLabel("B");
	private static JLabel colC = new JLabel("C");
	private static JLabel row1 = new JLabel("1");
	private static JLabel row2 = new JLabel("2");
	private static JLabel row3 = new JLabel("3");
	private static JLabel row4 = new JLabel("4");
	private static JLabel white = new JLabel("White's hand");
	private static JLabel black = new JLabel("Black's hand");
	
	private static JLabel comp = new JLabel("Computer");
	private static JLabel nodes = new JLabel("Nodes:");
	public static JLabel counter = new JLabel(" ");
	
	// service buttons
	private static JCheckBox volume = new JCheckBox("Mute volume", false);
	private static JSlider boost = new JSlider(-10, 10, 0);
	private static JButton newgame = new JButton("New Game");
	private static JButton push = new JButton();
	private static JLabel label1 = new JLabel("Force");
	private static JLabel label2 = new JLabel("Black");
	private static JCheckBox check = new JCheckBox("Check warning", true);
	
	private static JButton next = new JButton();
	private static JLabel label3 = new JLabel("Next");
	private static JLabel label4 = new JLabel("Best");

	// playing level selection
	private static JLabel behave = new JLabel("Select AI level / behave:");
	private static JPanel panel = new JPanel(new GridLayout(2,4));
	private static ButtonGroup level = new ButtonGroup();	
	private static String[] levelArray = {"Stupid", "Greedy", "Naive", "Tricky", 
								  "Active", "Clever", "Expert", "Master"};
	private static JToggleButton[] brain = new JToggleButton[levelArray.length];
	
	private static JMenuBar menuBar = new JMenuBar();
    
	public Gui() {
		
		new Thread(new Runnable() {
			public void run() {
				clocks.showClock();
			}
		}).start();
		
		director.initialize();
		
        menuBar.add(createFileMenu());
        menuBar.add(createAccountMenu());
        menuBar.add(createOtherMenu());
        frame.setJMenuBar(menuBar);
		
		// fixed positions layout
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(1600,1000);
		frame.setTitle("Zoo Shogi");
		frame.add(board);
		frame.add(handW);
		frame.add(handB);		
		frame.add(cols);		
		frame.add(rows);		
		frame.add(output);
		frame.add(behave);
		frame.add(score);
		frame.add(show);		
		frame.add(white);
		frame.add(black);		
		frame.add(profile);
		
		frame.add(clockB);
		frame.add(clockW);
//		frame.add(sound);
//		sound.setBounds(100, 500, 300, 100);
		frame.add(comp);
		frame.add(nodes);
		frame.add(counter);
		
		board.setBounds(500,100,600,800);
		handB.setBounds(100,100,300,200);
		handW.setBounds(1200,700,300,200);
		cols.setBounds(500,50,600,50);
		rows.setBounds(450,100,50,800);		
		output.setFont(new Font("Dialog", Font.PLAIN, 20));
		output.setHorizontalAlignment(JLabel.CENTER);
		output.setBounds(100,475,300,50);
		output.setOpaque(true);
		output.setBackground(Color.LIGHT_GRAY);
		behave.setFont(new Font("Dialog", Font.PLAIN, 25));
		behave.setHorizontalAlignment(JLabel.CENTER);
		behave.setBounds(1200,450,300,50);
		score.setFont(new Font("Dialog", Font.PLAIN, 40));
		score.setHorizontalAlignment(JLabel.CENTER);
		score.setBounds(100,825,300,75);
		score.setOpaque(true);
		score.setBackground(Color.LIGHT_GRAY);
		show.setFont(new Font("Dialog", Font.PLAIN, 45));
		show.setHorizontalAlignment(JLabel.CENTER);
		show.setBounds(100,750,300,50);
		colA.setFont(new Font("Dialog", Font.PLAIN, 25));
		colA.setHorizontalAlignment(JLabel.CENTER);
//		colA.setBounds(500,50,200,50);
		colB.setFont(new Font("Dialog", Font.PLAIN, 25));
		colB.setHorizontalAlignment(JLabel.CENTER);
//		colB.setBounds(700,50,200,50);
		colC.setFont(new Font("Dialog", Font.PLAIN, 25));
		colC.setHorizontalAlignment(JLabel.CENTER);
//		colC.setBounds(900,50,200,50);
		row1.setFont(new Font("Dialog", Font.PLAIN, 25));
		row1.setHorizontalAlignment(JLabel.CENTER);
//		row1.setBounds(450,175,50,50);
		row2.setFont(new Font("Dialog", Font.PLAIN, 25));
		row2.setHorizontalAlignment(JLabel.CENTER);
//		row2.setBounds(450,375,50,50);
		row3.setFont(new Font("Dialog", Font.PLAIN, 25));
		row3.setHorizontalAlignment(JLabel.CENTER);
//		row3.setBounds(450,575,50,50);
		row4.setFont(new Font("Dialog", Font.PLAIN, 25));
		row4.setHorizontalAlignment(JLabel.CENTER);
//		row4.setBounds(450,775,50,50);
		white.setFont(new Font("Dialog", Font.PLAIN, 25));
		white.setHorizontalAlignment(JLabel.CENTER);
		white.setBounds(1200,650,300,50);
		black.setFont(new Font("Dialog", Font.PLAIN, 25));
		black.setHorizontalAlignment(JLabel.CENTER);
		black.setBounds(100,50,300,50);		
		profile.setFont(new Font("Dialog", Font.PLAIN, 25));
		profile.setHorizontalAlignment(JLabel.CENTER);
		profile.setBounds(1200,225,300,50);
		cols.add(colA);
		cols.add(colB);
		cols.add(colC);
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		rows.add(row4);
		
		clockB.setBounds(1200,100,300,50);
		clockB.setFont(new Font("Dialog", Font.PLAIN, 50));
		clockB.setHorizontalAlignment(JLabel.CENTER);
		clockB.setOpaque(true);
		clockB.setBackground(Color.BLACK);
		clockB.setForeground(Color.WHITE);
		clockW.setBounds(1200,175,300,50);
		clockW.setFont(new Font("Dialog", Font.PLAIN, 50));
		clockW.setHorizontalAlignment(JLabel.CENTER);
		clockW.setOpaque(true);
		clockW.setBackground(Color.WHITE);
		comp.setFont(new Font("Dialog", Font.PLAIN, 25));
		comp.setHorizontalAlignment(JLabel.CENTER);
		comp.setBounds(1200,50,300,50);
		nodes.setFont(new Font("Dialog", Font.PLAIN, 25));
		nodes.setHorizontalAlignment(JLabel.LEFT);
		nodes.setBounds(100,675,100,50);
		counter.setFont(new Font("Dialog", Font.PLAIN, 25));
		counter.setHorizontalAlignment(JLabel.CENTER);
		counter.setBounds(200,675,200,50);
		counter.setOpaque(true);
		counter.setBackground(Color.LIGHT_GRAY);
		
		volume.setFont(new Font("Dialog", Font.PLAIN, 20));
		volume.setHorizontalAlignment(JCheckBox.CENTER);
		volume.setBounds(175,550,150,50);
		volume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volume = (JCheckBox) e.getSource();
				if(volume.isSelected()){
					mute = true;
					director.aiMute(true);
				}
				else{
					mute = false;
					director.aiMute(false);
				}
				return;
			}
		});
		frame.add(volume);
//		sound.add(volume);
		
		boost.setFont(new Font("Dialog", Font.PLAIN, 25));
		boost.setBounds(100,600,300,50);
		boost.setPaintTicks(true);
		boost.setSnapToTicks(true);
		boost.setMajorTickSpacing(1);
		boost.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boost = (JSlider) e.getSource();
				output.setText(boost.getValue()>0?"Gain +"+(float)boost.getValue():
												  "Gain "+(float)boost.getValue());
				Sound.setVol((float)boost.getValue());
			}
		});
		frame.add(boost);
//		sound.add(boost);

		newgame.setFont(new Font("Dialog", Font.PLAIN, 25));
		newgame.setBounds(1200,300,300,50);
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newgame = (JButton) e.getSource();
				
				unlockBoard();
				unlockButtons();
				if(director.checkClient() == false) {
					enable();
				}
				output.setText(" ");
				score.setText(" ");
				director.initialize();
				director.newGame();
				
				clocks.reset();
				
				updateGui();
				return;
			}
		});
		frame.add(newgame);
		
//		push.setFont(new Font("Dialog", Font.PLAIN, 45));
		push.setLayout(new GridLayout(2,1));
		push.add(label1);
		push.add(label2);
		label1.setFont(new Font("Dialog", Font.PLAIN, 45));
		label2.setFont(new Font("Dialog", Font.PLAIN, 45));
		label1.setHorizontalAlignment(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);
		push.setBounds(250,350,150,100);		
		push.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				push = (JButton) e.getSource();
				disable();
				
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							director.compute();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				return;
			}
		});
		frame.add(push);

//		next.setFont(new Font("Dialog", Font.PLAIN, 45));
		next.setLayout(new GridLayout(2,1));
		next.add(label3);
		next.add(label4);
		label3.setFont(new Font("Dialog", Font.PLAIN, 45));
		label4.setFont(new Font("Dialog", Font.PLAIN, 45));
		label3.setHorizontalAlignment(JLabel.CENTER);
		label4.setHorizontalAlignment(JLabel.CENTER);
		next.setBounds(100,350,150,100);		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				next = (JButton) e.getSource();

				if(director.beginning() || !Cache.empty()) {
					return;
				}
				Clocks.setNodes(0);
				director.undoMove();
				updateGui();
				return;
			}
		});
		frame.add(next);
		
		check.setFont(new Font("Dialog", Font.PLAIN, 25));
		check.setHorizontalAlignment(JCheckBox.CENTER);
		check.setBounds(1250,375,200,50);
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check = (JCheckBox) e.getSource();
				if(check.isSelected()){
					warn = true;
					director.aiWarn(true);
				}
				else{
					warn = false;
					director.aiWarn(false);
				}
				return;
			}
		});
		frame.add(check);

		panel.setBounds(1200,500,300,100);
		for(int i=0; i<levelArray.length; i++){
			brain[i] = new JToggleButton();
			brain[i].setText(levelArray[i]);
			level.add(brain[i]);
			panel.add(brain[i]);
			brain[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton theButton = (JToggleButton) e.getSource();
					for(int i=0; i<levelArray.length; i++){
						if(theButton.equals(brain[i])){
							director.setLevel(i);
							output.setText("Level " + Integer.toString(i)+ " selected");
							if(director.checkClient()) {
								director.disconnect();
							}
							return;
						}
					}
				}
			});
		}
		frame.getContentPane().add(panel);
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				//Initialize the button in the board array
				squares[r][c] = new JButton();
				squares[r][c].setSize(200, 200);
				squares[r][c].setBorder(new LineBorder(Color.GRAY));
				squares[r][c].setBackground(Color.decode("#db9356"));
				squares[r][c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton theButton = (JButton) e.getSource();						
						for(int r=0;r<4;r++) {
							for(int c=0;c<3;c++) {
						if(theButton.equals(squares[r][c]) & click == 1 & 
								director.list(squares[r][c].getName())){
							squares[r][c].setBackground(Color.YELLOW);
							highlight(r, c);
							director.from(r, c, squares[r][c].getName());
							click = 2;
							output.setText(Message.getPieceName(squares[r][c].getName())
																		+" choosen");
							drop = false;
							return;
							}
						else if(theButton.equals(squares[r][c]) & click == 2 & 
								director.list(squares[r][c].getName())==false){
							if(director.to(r, c)){
								if(drop & squares[r][c].getName().equals(" ")){
									director.drop();
								}
								else if(drop & !squares[r][c].getName().equals(" ")){
									drop = false;
									output.setText("Wrong move!");
									updateGui();
									return;
								}
								else{
									director.move();
								}
								updateGui();
								click = 1;
								disable();
								drop = false;								
								
									javax.swing.SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											try {											
												Clocks.setNodes(0);
												Gui.counter.setText(" ");
												director.compute();
											}
											catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									});
								return;
								}
							else{
								squares[r][c].setBackground(Color.decode("#db9356"));
								click = 1;
								drop = false;
								output.setText("Wrong move!");
								updateGui();
								return;
								}
							}
						else if(theButton.equals(squares[r][c]) & click == 2 & 
								director.list(squares[r][c].getName())){
							squares[r][c].setBackground(Color.decode("#db9356"));
							click = 1;
							drop = false;
							output.setText("Wrong place!");
							updateGui();
							return;
								}
							}
						}
						updateGui();
						return;
					}
				});
				//add each square to the GUI panel
				board.add(squares[r][c]);
			}
		}
		for(int c=0;c<6;c++) {
				//Initialize the button in the board array
				dropW[c] = new JButton();
				dropW[c].setSize(100, 100);
				dropW[c].setBorder(new LineBorder(Color.GRAY));
				dropW[c].setBackground(Color.decode("#db9356"));
				dropW[c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton theButton = (JButton) e.getSource();
						for(int c=0;c<6;c++) {
						if(theButton.equals(dropW[c])&click==1&director.list(dropW[c].getName())){
							drop = true;
							dropW[c].setBackground(Color.YELLOW);
							highlight();
							director.from(3, c+3, dropW[c].getName());						
							click = 2;							
							return;
							}
						else if(theButton.equals(dropW[c]) & click == 2){
							dropW[c].setBackground(Color.decode("#db9356"));
							click = 1;
							drop = false;
							output.setText("Wrong place!");
							updateGui();
							return;
							}					}
						}
					});
				//add each square to the GUI panel
				handW.add(dropW[c]);
		}
		for(int c=0;c<6;c++) {
				//Initialize the label in the board array
				dropB[c] = new JButton();
				dropB[c].setSize(100, 100);
				dropB[c].setBorder(new LineBorder(Color.GRAY));
				dropB[c].setBackground(Color.decode("#db9356"));
				//add each square to the GUI panel
				handB.add(dropB[c]);
		}
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		brain[0].doClick();
		updateGui();
		frame.setVisible(true);
		//Redraw the graphics to show the squares
		frame.revalidate();
		frame.repaint();
	}
	
	private void updateGui() {
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setBackground(Color.decode("#db9356"));
				squares[r][c].setName(director.refresh(r,c));
				squares[r][c].setIcon(new ImageIcon(imageLarge(squares[r][c].getName())));
			}
		}		
		for(int c=0;c<6;c++) {
			dropW[c].setBackground(Color.decode("#db9356"));
			dropW[c].setName(director.refresh(3,c+3));
			dropW[c].setIcon(new ImageIcon(imageSmall(dropW[c].getName())));
		}		
		for(int c=0;c<6;c++) {
			dropB[c].setBackground(Color.decode("#db9356"));
			dropB[c].setName(director.refresh(0,c+3));
			dropB[c].setIcon(new ImageIcon(imageSmall(dropB[c].getName())));			
		}
		
		if(warn){
			for(int r=0;r<4;r++) {
				for(int c=0;c<3;c++) {
					warning();
				}
			}	
		}
	}
	
	private void warning(){
		
		int r2, c2;
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
		if(squares[r][c].getName().equals("p")){
			r2 = r+1;
			c2 = c;
			if((Pieces.BPAWN.move(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);
			}
		}
		else if(squares[r][c].getName().equals("r")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.move(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("k")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.move(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("b")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.move(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BQUEEN.move(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
			}
		}
	}
	
	private String imageLarge(String piece){
		
		String icon = " ";
		
		switch(piece){
			case "P":
				icon = "ui/images/large/WP.png";
				break;
			case "R":
				icon = "ui/images/large/WR.png";
				break;
			case "B":
				icon = "ui/images/large/WB.png";
				break;
			case "K":
				icon = "ui/images/large/WK.png";
				break;
			case "Q":
				icon = "ui/images/large/WQ.png";
				break;
			case "p":
				icon = "ui/images/large/BP.png";
				break;
			case "r":
				icon = "ui/images/large/BR.png";
				break;
			case "b":
				icon = "ui/images/large/BB.png";
				break;
			case "k":
				icon = "ui/images/large/BK.png";
				break;
			case "q":
				icon = "ui/images/large/BQ.png";
				break;
		}
		return icon;
	}
	
	private String imageSmall(String piece){
		
		String icon = " ";
		
		switch(piece){
			case "P":
				icon = "ui/images/small/WP_small.png";
				break;
			case "R":
				icon = "ui/images/small/WR_small.png";
				break;
			case "B":
				icon = "ui/images/small/WB_small.png";
				break;
			case "K":
				icon = "ui/images/small/WK_small.png";
				break;
			case "Q":
				icon = "ui/images/small/WQ_small.png";
				break;
			case "p":
				icon = "ui/images/small/BP_small.png";
				break;
			case "r":
				icon = "ui/images/small/BR_small.png";
				break;
			case "b":
				icon = "ui/images/small/BB_small.png";
				break;
			case "k":
				icon = "ui/images/small/BK_small.png";
				break;
			case "q":
				icon = "ui/images/small/BQ_small.png";
				break;
		}
		return icon;
	}
	
	private void highlight(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(drop & squares[r][c].getName().equals(" ")){
				squares[r][c].setBackground(Color.GREEN);
				}
			}
		}
	}
	
	private void highlight(int r, int c){
		
		int r2, c2;
		
		if(squares[r][c].getName().equals("P")){
			r2 = r-1;
			c2 = c;
			if((Pieces.WPAWN.move(r, c, r2, c2)&&director.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
		}
		else if(squares[r][c].getName().equals("R")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.move(r, c, r2, c2)&&director.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("K")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.move(r, c, r2, c2)&&director.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("B")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.move(r, c, r2, c2)&&director.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("Q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.WQUEEN.move(r, c, r2, c2)&&director.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
	}
	
	public static void lock(){
		
		next.setEnabled(false);
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setEnabled(false);
			}
		}
		for(int c=0;c<6;c++) {
			dropW[c].setEnabled(false);			
		}
		for(int c=0;c<6;c++) {
			dropB[c].setEnabled(false);			
		}
	}	
	private void unlockBoard(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setEnabled(true);								
			}
		}
		for(int c=0;c<6;c++) {
			dropW[c].setEnabled(true);			
		}
		for(int c=0;c<6;c++) {
			dropB[c].setEnabled(true);			
		}		
	}	
	private void unlockButtons(){				
		push.setEnabled(true);
		next.setEnabled(true);		
	}
	
	public static void doClick(){

		loop:
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(squares[r][c].getName().equals("k")){
					squares[r][c].doClick();
					break loop;
				}
			}
		}
	}
	
	public static void setLevel(int i) {
		brain[i].doClick();
	}
	public static String getLevel() {
		for(int i=0; i<levelArray.length; i++){
			if(brain[i].isSelected()){
				return brain[i].getText();
			}
		}
		return null;
	}
	public static String getHuman() {
		return profile.getText();
	}
	
	private void disable() {
		
		push.setEnabled(false);
		for(int i=0; i<levelArray.length; i++){
			brain[i].setEnabled(false);
		}		
	}	
	private void enable() {
		
		push.setEnabled(true);
		for(int i=0; i<levelArray.length; i++){
			brain[i].setEnabled(true);
		}		
	}
	
	private JMenu createFileMenu(){

        JMenu file = new JMenu("File");
        JMenuItem newgame2 = new JMenuItem("New game");
        JMenuItem savegame = new JMenuItem("Save game");
        JMenuItem loadgame = new JMenuItem("Load game");
		JMenuItem exit = new JMenuItem("Exit");

		file.add(newgame2);
		file.add(savegame);
		file.add(loadgame);
		file.addSeparator();
		file.add(exit);
		
		newgame2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				newgame.doClick();
			}
		});
		
		savegame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							director.saveGame();
							JOptionPane.showMessageDialog(loadgame,
									"Position saved",
										"Success!",
											JOptionPane.INFORMATION_MESSAGE);
						}
					});
			}
		});				
		
		loadgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if(director.loadGame()) {
								Gui.doClick();
								disable();
								JOptionPane.showMessageDialog(loadgame,
										"Last game loaded",
											"Success!",
												JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								JOptionPane.showMessageDialog(loadgame,
										"No saved game found",
											"Fail",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			}
		});			
		
		exit.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent e) {			   
			   System.exit(0);
		   }
		});
		
        return file;
	}

	private JMenu createAccountMenu(){
		
        JMenu account = new JMenu("Account");
        JMenuItem create = new JMenuItem("Create");
        JMenuItem select = new JMenuItem("Select");
        JMenuItem delete = new JMenuItem("Delete");
       
        account.add(create);
        account.add(select);
        account.addSeparator();
        account.add(delete);
		
		create.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {				   							
							new Login();								
		   }
		});
		
		select.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   			new Users();				   
		   }
		});
		
		delete.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {

							int result = JOptionPane.showConfirmDialog(delete,
										"Do you want to delete your stats?",
											"Profile erasing",
												JOptionPane.YES_NO_OPTION, 
												JOptionPane.WARNING_MESSAGE);
					            if (result == JOptionPane.YES_OPTION) {
					            	director.deletePlayer();
					            	profile.setText("Player");
					            }				   
		   }
		});
		
        return account;
	}
	
	private JMenu createOtherMenu(){
		
        JMenu other = new JMenu("Other");
        JMenuItem records = new JMenuItem("Records");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem engine = new JMenuItem("Engine");
        JMenuItem server = new JMenuItem("Server");
       
        other.add(records);
        other.add(help);
        other.addSeparator();        
        other.add(engine);
        other.add(server);
        
        records.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
							new Table();
		   }
		});
		
        help.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   
					JOptionPane.showMessageDialog(help,
							"For the rules of game watch on Youtube:\n" +
							"How to play Dobutsu Shogi (Catch the Lion)",
								"About",
									JOptionPane.DEFAULT_OPTION);		   
		   }
		});
        
        engine.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
							new Chooser();
		   }
		});
        
		server.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {

							int result = JOptionPane.showConfirmDialog(server,
										"Do you want to stop server?",
											"Server shutdown",
												JOptionPane.YES_NO_OPTION, 
												JOptionPane.WARNING_MESSAGE);
					            if (result == JOptionPane.YES_OPTION) {
									if(director.getServer() != null) {
										director.shutdown();
										newgame.doClick();
									}
					            }				   
		   }
		});
		
        return other;
	}
		
}
