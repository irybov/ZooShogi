package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

// import java.io.*;

import control.Director;
import sound.Sound;
import util.Bishop;
import util.King;
import util.Message;
import util.Pawn;
import util.Queen;
import util.Rook;

public class Gui {
	
	Director plate = Director.getInstance();
	int click = 1;
	boolean drop = false;
	boolean warn = true;	
	boolean mute = false;
	
	//The Frame that's displayed. (Contains the Panels)
	static JFrame frame = new JFrame();
	
	//Panels that handles the visual for the board object
	static JPanel board = new JPanel(new GridLayout(4,3));
	static JPanel handW = new JPanel(new GridLayout(2,3));
	static JPanel handB = new JPanel(new GridLayout(2,3));
	
	//Arrays of JButtons which handle the visual for the Square objects
	static JButton[][] squares = new JButton[4][3];
	static JButton[] dropW = new JButton[6];
	static JButton[] dropB = new JButton[6];
	
	// dialog windows
	public static JLabel output = new JLabel(" ");
	public static JLabel score = new JLabel(" ");
	
	// components labels
	static JLabel showO = new JLabel("Output:");
	static JLabel showS = new JLabel("Score:");
	static JLabel colA = new JLabel("A");
	static JLabel colB = new JLabel("B");
	static JLabel colC = new JLabel("C");
	static JLabel row1 = new JLabel("1");
	static JLabel row2 = new JLabel("2");
	static JLabel row3 = new JLabel("3");
	static JLabel row4 = new JLabel("4");
	static JLabel white = new JLabel("White's hand");
	static JLabel black = new JLabel("Black's hand");
	
	// service buttons
	static JCheckBox volume = new JCheckBox("Mute volume", false);
	static JSlider boost = new JSlider(-6, 6, 0);
	static JButton newgame = new JButton("New Game");
	static JButton push = new JButton("MOVE");
	static JCheckBox check = new JCheckBox("Check warning", true);

	// playing level selection
	static JLabel behave = new JLabel("Select AI level / behave:");
	static JPanel panel = new JPanel(new GridLayout(2,4));
	static ButtonGroup level = new ButtonGroup();	
	static String[] levelArray = {"Stupid", "Naive", "Greedy", "Tricky", 
								  "Active", "Clever", "Expert", "Master"};
	static JToggleButton[] brain = new JToggleButton[levelArray.length];
	
    // Создание строки главного меню
    static JMenuBar menuBar = new JMenuBar();
    
	public Gui() {
		
        // Добавление в главное меню выпадающих пунктов меню  
        menuBar.add(createFileMenu());
        menuBar.add(createAccountMenu());
//        menuBar.add(createViewMenu());
        // Подключаем меню к интерфейсу приложения
        frame.setJMenuBar(menuBar);
		
		plate.initialize();
		
		// fixed positions layout
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(1600,1000);
		frame.setTitle("Zoo Shogi");
		frame.add(board);
		frame.add(handW);
		frame.add(handB);
		frame.add(output);
		frame.add(behave);
		frame.add(score);
		frame.add(showO);
		frame.add(showS);
		frame.add(colA);
		frame.add(colB);
		frame.add(colC);
		frame.add(row1);
		frame.add(row2);
		frame.add(row3);
		frame.add(row4);
		frame.add(white);
		frame.add(black);
		
		board.setBounds(500,100,600,800);
		handB.setBounds(100,100,300,200);
		handW.setBounds(1200,700,300,200);
		output.setFont(new Font("Dialog", Font.PLAIN, 25));
		output.setHorizontalAlignment(JLabel.CENTER);
		output.setBounds(50,450,400,50);
		behave.setFont(new Font("Dialog", Font.PLAIN, 25));
		behave.setHorizontalAlignment(JLabel.CENTER);
		behave.setBounds(1200,450,300,50);
		score.setFont(new Font("Dialog", Font.PLAIN, 50));
		score.setHorizontalAlignment(JLabel.CENTER);
		score.setBounds(100,750,300,100);
		showO.setFont(new Font("Dialog", Font.PLAIN, 50));
		showO.setHorizontalAlignment(JLabel.CENTER);
		showO.setBounds(100,350,300,50);
		showS.setFont(new Font("Dialog", Font.PLAIN, 50));
		showS.setHorizontalAlignment(JLabel.CENTER);
		showS.setBounds(100,700,300,50);
		colA.setFont(new Font("Dialog", Font.PLAIN, 25));
		colA.setHorizontalAlignment(JLabel.CENTER);
		colA.setBounds(500,50,200,50);
		colB.setFont(new Font("Dialog", Font.PLAIN, 25));
		colB.setHorizontalAlignment(JLabel.CENTER);
		colB.setBounds(700,50,200,50);
		colC.setFont(new Font("Dialog", Font.PLAIN, 25));
		colC.setHorizontalAlignment(JLabel.CENTER);
		colC.setBounds(900,50,200,50);
		row1.setFont(new Font("Dialog", Font.PLAIN, 25));
		row1.setHorizontalAlignment(JLabel.CENTER);
		row1.setBounds(450,175,50,50);
		row2.setFont(new Font("Dialog", Font.PLAIN, 25));
		row2.setHorizontalAlignment(JLabel.CENTER);
		row2.setBounds(450,375,50,50);
		row3.setFont(new Font("Dialog", Font.PLAIN, 25));
		row3.setHorizontalAlignment(JLabel.CENTER);
		row3.setBounds(450,575,50,50);
		row4.setFont(new Font("Dialog", Font.PLAIN, 25));
		row4.setHorizontalAlignment(JLabel.CENTER);
		row4.setBounds(450,775,50,50);
		white.setFont(new Font("Dialog", Font.PLAIN, 25));
		white.setHorizontalAlignment(JLabel.CENTER);
		white.setBounds(1200,650,300,50);
		black.setFont(new Font("Dialog", Font.PLAIN, 25));
		black.setHorizontalAlignment(JLabel.CENTER);
		black.setBounds(100,50,300,50);
		
		volume.setFont(new Font("Dialog", Font.PLAIN, 25));
		volume.setHorizontalAlignment(JCheckBox.CENTER);
		volume.setBounds(100,550,300,50);
		volume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volume = (JCheckBox) e.getSource();
				if(volume.isSelected()){
					mute = true;
					plate.aiMute(true);
				}
				else{
					mute = false;
					plate.aiMute(false);
				}
				return;
			}
		});
		frame.add(volume);
		
		boost.setFont(new Font("Dialog", Font.PLAIN, 25));
		boost.setBounds(100,600,300,50);
		boost.setPaintTicks(true);
		boost.setSnapToTicks(true);
		boost.setMajorTickSpacing(1);
		boost.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boost = (JSlider) e.getSource();
				Gui.output.setText(boost.getValue()>0?"Gain +"+(float)boost.getValue():
													  "Gain "+(float)boost.getValue());
				Sound.setVol((float)boost.getValue());
			}
		});
		frame.add(boost);

		newgame.setFont(new Font("Dialog", Font.PLAIN, 25));
		newgame.setBounds(1200,250,300,50);
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newgame = (JButton) e.getSource();
				push.setEnabled(true);
				unlock();
				output.setText(" ");
				score.setText(" ");
				plate.initialize();
				plate.clearing();
				updateGui();
				return;
			}
		});
		frame.add(newgame);
		
		push.setFont(new Font("Dialog", Font.PLAIN, 50));
		push.setBounds(1200,100,300,100);
		push.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				push = (JButton) e.getSource();
				push.setEnabled(false);
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
					try {
						plate.compute();
					}
					catch (InterruptedException e) {
						e.printStackTrace();
						}
					}
				});
				updateGui();
				return;
			}
		});
		frame.add(push);
		
		check.setFont(new Font("Dialog", Font.PLAIN, 15));
		check.setHorizontalAlignment(JCheckBox.CENTER);
		check.setBounds(1200,625,300,25);
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check = (JCheckBox) e.getSource();
				if(check.isSelected()){
					warn = true;
					plate.aiWarn(true);
				}
				else{
					warn = false;
					plate.aiWarn(false);
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
						if(theButton == brain[i]){
							plate.setLevel(i);
							Gui.output.setText("Level " + Integer.toString(i+1)+ " selected");
							return;
						}
					}
				}
			});
		}
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(panel);
		
		//allow the frame to be closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
						if(theButton == squares[r][c] & click == 1 & 
								plate.list(squares[r][c].getName())){
							squares[r][c].setBackground(Color.YELLOW);
							highlight(r, c);
							plate.from(r, c, squares[r][c].getName());
							click = 2;
							output.setText(Message.pieceName(squares[r][c].getName()) +" choosen");
							drop = false;							
							return;
							}
						else if(theButton == squares[r][c] & click == 2 & 
								plate.list(squares[r][c].getName())==false){
							if(plate.to(r, c)){
								if(drop & squares[r][c].getName()==" "){
									plate.drop();
								}
								else if(drop & squares[r][c].getName()!=" "){
									drop = false;
									output.setText("Wrong move!");
									updateGui();
									return;
								}
								else{
									plate.move();
								}
								updateGui();
								click = 1;
								push.setEnabled(false);
								drop = false;
									javax.swing.SwingUtilities.invokeLater(new Runnable() {
										public void run() {
										try {
											plate.compute();
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
						else if(theButton == squares[r][c] & click == 2 & 
								plate.list(squares[r][c].getName())){
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
						if(theButton == dropW[c] & click == 1 & plate.list(dropW[c].getName())){
							drop = true;
							dropW[c].setBackground(Color.YELLOW);
							highlight();
							plate.from(3, c+3, dropW[c].getName());						
							click = 2;							
							return;
							}
						else if(theButton == dropW[c] & click == 2){
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
		updateGui();
		frame.setVisible(true);
		//Redraw the graphics to show the squares
		frame.revalidate();
		frame.repaint();
	}
	
	void updateGui() {
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setBackground(Color.decode("#db9356"));
				squares[r][c].setName(plate.refresh(r,c));
				squares[r][c].setIcon(new ImageIcon(getClass().getResource
									 (image(squares[r][c].getName()))));
				if(warn){
					warning();
				}
			}
		}		
		for(int c=0;c<6;c++) {
			dropW[c].setBackground(Color.decode("#db9356"));
			dropW[c].setName(plate.refresh(3,c+3));
			dropW[c].setIcon(new ImageIcon(getClass().getResource
							(imageSmall(dropW[c].getName()))));
		}		
		for(int c=0;c<6;c++) {
			dropB[c].setBackground(Color.decode("#db9356"));
			dropB[c].setName(plate.refresh(0,c+3));
			dropB[c].setIcon(new ImageIcon(getClass().getResource
							(imageSmall(dropB[c].getName()))));			
		}
	}
	
	void warning(){
		
		int r2, c2;
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
		if(squares[r][c].getName()=="p"){
			r2 = r+1;
			c2 = c;
			if((Pawn.move(r, c, r2, c2)&&(squares[r2][c2].getName()=="K"))){
				squares[r2][c2].setBackground(Color.RED);
			}
		}
		else if(squares[r][c].getName()=="r"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Rook.move(r, c, r2, c2)&&(squares[r2][c2].getName()=="K"))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="k"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((King.move(r, c, r2, c2)&&(squares[r2][c2].getName()=="K"))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="b"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Bishop.move(r, c, r2, c2)&&(squares[r2][c2].getName()=="K"))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="q"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Queen.move(r, c, r2, c2, "black")&&(squares[r2][c2].getName()=="K"))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
			}
		}
	}
	
	String image(String piece){
		
		String icon = " ";
		
		switch(piece){
			case "P":
			icon = "images/WP.png";
			break;
			case "R":
			icon = "images/WR.png";
			break;
			case "B":
			icon = "images/WB.png";
			break;
			case "K":
			icon = "images/WK.png";
			break;
			case "Q":
			icon = "images/WQ.png";
			break;
			case "p":
			icon = "images/BP.png";
			break;
			case "r":
			icon = "images/BR.png";
			break;
			case "b":
			icon = "images/BB.png";
			break;
			case "k":
			icon = "images/BK.png";
			break;
			case "q":
			icon = "images/BQ.png";
			break;
		}
		return icon;
	}
	
	String imageSmall(String piece){
		
		String icon = " ";
		
		switch(piece){
		case "P":
		icon = "images/WP_small.png";
		break;
		case "R":
		icon = "images/WR_small.png";
		break;
		case "B":
		icon = "images/WB_small.png";
		break;
		case "K":
		icon = "images/WK_small.png";
		break;
		case "Q":
		icon = "images/WQ_small.png";
		break;
		case "p":
		icon = "images/BP_small.png";
		break;
		case "r":
		icon = "images/BR_small.png";
		break;
		case "b":
		icon = "images/BB_small.png";
		break;
		case "k":
		icon = "images/BK_small.png";
		break;
		case "q":
		icon = "images/BQ_small.png";
		break;
		}
		return icon;
	}
	
	void highlight(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(drop & squares[r][c].getName()==" "){
				squares[r][c].setBackground(Color.GREEN);
				}
			}
		}
	}
	
	void highlight(int r, int c){
		
		int r2, c2;
		
		if(squares[r][c].getName()=="P"){
			r2 = r-1;
			c2 = c;
			if((Pawn.move(r, c, r2, c2)&&plate.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
		}
		else if(squares[r][c].getName()=="R"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Rook.move(r, c, r2, c2)&&plate.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="K"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((King.move(r, c, r2, c2)&&plate.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="B"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Bishop.move(r, c, r2, c2)&&plate.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName()=="Q"){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Queen.move(r, c, r2, c2, "white")&&plate.legal(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
	}
	
	public static void lock(){
		
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
	
	void unlock(){
		
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
	
	public static void doClick(){

		loop:
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(squares[r][c].getName()=="k"){
					squares[r][c].doClick();
					break loop;
				}
			}
		}
	}
	
	JMenu createFileMenu(){

        JMenu file = new JMenu("File");
        JMenuItem newgame2 = new JMenuItem("New game");
        JMenuItem savegame = new JMenuItem("Save game");
        JMenuItem loadgame = new JMenuItem("Load game");
		JMenuItem exit = new JMenuItem("Quit");

		file.add(newgame2);
		file.add(savegame);
		file.add(loadgame);
		file.addSeparator();
		file.add(exit);
		
		savegame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
				plate.saveGame();
						}
					});
			}
		});				
		
		loadgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
				plate.loadGame();
						}
					});
				push.setEnabled(false);
			}
		});			
		
		newgame2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newgame.doClick();
			}
		});
		
		exit.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
			   
			   System.exit(0);
		   }
		});
		
        return file;
	}

	JMenu createAccountMenu(){
		
        JMenu account = new JMenu("Account");
        JMenuItem create = new JMenuItem("Create");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem delete = new JMenuItem("Delete");
       
        account.add(create);
        account.add(load);
        account.addSeparator();
        account.add(delete);
		
		create.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   
			   }
			});
		
		delete.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   
			   }
			});
		
		load.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   
			   }
			});
        		
        return account;
	}
	
}
