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
	private int clickNumber = 1;
	private boolean isMoveAsDrop = false;
	private boolean isCheckWarningEnabled = true;	
	boolean isVolumeMuted = false;

	private Clocks clocks = Clocks.getInstance();

	//The Frame that's displayed. (Contains the Panels)
	private static JFrame frame = new JFrame();
	
	//Panels that handle the visual for the board object
	private static JPanel board = new JPanel(new GridLayout(4,3));
	private static JPanel handWhite = new JPanel(new GridLayout(2,3));
	private static JPanel handBlack = new JPanel(new GridLayout(2,3));
	private static JPanel columns = new JPanel(new GridLayout(1,3));
	private static JPanel rows = new JPanel(new GridLayout(4,1));
	
//	static JPanel sound = new JPanel(new GridLayout(2,1));	
	
	//Arrays of JButtons which handle the visual for the Square objects
	private static JButton[][] squares = new JButton[4][3];
	private static JButton[] dropAreaWhite = new JButton[6];
	private static JButton[] dropAreaBlack = new JButton[6];
	
	// dialog windows
	public static JLabel output = new JLabel(" ");
	public static JLabel score = new JLabel(" ");
	static JLabel profile = new JLabel("Player");

	public static JLabel clockBlack = new JLabel("00:00");
	public static JLabel clockWhite = new JLabel("00:00");
	
	// components labels
	private static JLabel scoreLabel = new JLabel("Score:");
	private static JLabel columnA = new JLabel("A");
	private static JLabel columnB = new JLabel("B");
	private static JLabel columnC = new JLabel("C");
	private static JLabel row1 = new JLabel("1");
	private static JLabel row2 = new JLabel("2");
	private static JLabel row3 = new JLabel("3");
	private static JLabel row4 = new JLabel("4");
	private static JLabel whiteHand = new JLabel("White's hand");
	private static JLabel blackHand = new JLabel("Black's hand");
	
	private static JLabel computer = new JLabel("Computer");
	private static JLabel nodesLabel = new JLabel("Nodes:");
	public static JLabel nodes = new JLabel(" ");
	
	// service buttons
	private static JCheckBox volume = new JCheckBox("Mute volume", false);
	private static JSlider boost = new JSlider(-10, 10, 0);
	private static JButton newgame = new JButton("New Game");
	private static JButton forceBlack = new JButton();
	private static JLabel pushButtonLabelTop = new JLabel("Force");
	private static JLabel pushButtonLabelBottom = new JLabel("Black");
	private static JCheckBox checkWarning = new JCheckBox("Check warning", true);
	
	private static JButton nextBestMove = new JButton();
	private static JLabel nextBestMoveButtonLabelTop = new JLabel("Next");
	private static JLabel nextBestMoveButtonLabelBottom = new JLabel("Best");

	// playing level selection
	private static JLabel behaveLabel = new JLabel("Select AI level / behave:");
	private static JPanel levelPanel = new JPanel(new GridLayout(2,4));
	private static ButtonGroup levelButtons = new ButtonGroup();	
	private static String[] levels = {"Stupid", "Greedy", "Naive", "Tricky", 
								  "Active", "Clever", "Expert", "Master"};
	private static JToggleButton[] brain = new JToggleButton[levels.length];
	
	private static JMenuBar menuBar = new JMenuBar();
    
	public Gui() {
		
		new Thread(new Runnable() {
			public void run() {
				clocks.showClocks();
			}
		}).start();
		
		director.initBoard();
		
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
		frame.add(handWhite);
		frame.add(handBlack);		
		frame.add(columns);		
		frame.add(rows);		
		frame.add(output);
		frame.add(behaveLabel);
		frame.add(score);
		frame.add(scoreLabel);		
		frame.add(whiteHand);
		frame.add(blackHand);		
		frame.add(profile);
		
		frame.add(clockBlack);
		frame.add(clockWhite);
//		frame.add(sound);
//		sound.setBounds(100, 500, 300, 100);
		frame.add(computer);
		frame.add(nodesLabel);
		frame.add(nodes);
		
		board.setBounds(500,100,600,800);
		handBlack.setBounds(100,100,300,200);
		handWhite.setBounds(1200,700,300,200);
		columns.setBounds(500,50,600,50);
		rows.setBounds(450,100,50,800);		
		output.setFont(new Font("Dialog", Font.PLAIN, 20));
		output.setHorizontalAlignment(JLabel.CENTER);
		output.setBounds(100,475,300,50);
		output.setOpaque(true);
		output.setBackground(Color.LIGHT_GRAY);
		behaveLabel.setFont(new Font("Dialog", Font.PLAIN, 25));
		behaveLabel.setHorizontalAlignment(JLabel.CENTER);
		behaveLabel.setBounds(1200,450,300,50);
		score.setFont(new Font("Dialog", Font.PLAIN, 40));
		score.setHorizontalAlignment(JLabel.CENTER);
		score.setBounds(100,825,300,75);
		score.setOpaque(true);
		score.setBackground(Color.LIGHT_GRAY);
		scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 45));
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setBounds(100,750,300,50);
		columnA.setFont(new Font("Dialog", Font.PLAIN, 25));
		columnA.setHorizontalAlignment(JLabel.CENTER);
//		colA.setBounds(500,50,200,50);
		columnB.setFont(new Font("Dialog", Font.PLAIN, 25));
		columnB.setHorizontalAlignment(JLabel.CENTER);
//		colB.setBounds(700,50,200,50);
		columnC.setFont(new Font("Dialog", Font.PLAIN, 25));
		columnC.setHorizontalAlignment(JLabel.CENTER);
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
		whiteHand.setFont(new Font("Dialog", Font.PLAIN, 25));
		whiteHand.setHorizontalAlignment(JLabel.CENTER);
		whiteHand.setBounds(1200,650,300,50);
		blackHand.setFont(new Font("Dialog", Font.PLAIN, 25));
		blackHand.setHorizontalAlignment(JLabel.CENTER);
		blackHand.setBounds(100,50,300,50);		
		profile.setFont(new Font("Dialog", Font.PLAIN, 25));
		profile.setHorizontalAlignment(JLabel.CENTER);
		profile.setBounds(1200,225,300,50);
		columns.add(columnA);
		columns.add(columnB);
		columns.add(columnC);
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		rows.add(row4);
		
		clockBlack.setBounds(1200,100,300,50);
		clockBlack.setFont(new Font("Dialog", Font.PLAIN, 50));
		clockBlack.setHorizontalAlignment(JLabel.CENTER);
		clockBlack.setOpaque(true);
		clockBlack.setBackground(Color.BLACK);
		clockBlack.setForeground(Color.WHITE);
		clockWhite.setBounds(1200,175,300,50);
		clockWhite.setFont(new Font("Dialog", Font.PLAIN, 50));
		clockWhite.setHorizontalAlignment(JLabel.CENTER);
		clockWhite.setOpaque(true);
		clockWhite.setBackground(Color.WHITE);
		computer.setFont(new Font("Dialog", Font.PLAIN, 25));
		computer.setHorizontalAlignment(JLabel.CENTER);
		computer.setBounds(1200,50,300,50);
		nodesLabel.setFont(new Font("Dialog", Font.PLAIN, 25));
		nodesLabel.setHorizontalAlignment(JLabel.LEFT);
		nodesLabel.setBounds(100,675,100,50);
		nodes.setFont(new Font("Dialog", Font.PLAIN, 25));
		nodes.setHorizontalAlignment(JLabel.CENTER);
		nodes.setBounds(200,675,200,50);
		nodes.setOpaque(true);
		nodes.setBackground(Color.LIGHT_GRAY);
		
		volume.setFont(new Font("Dialog", Font.PLAIN, 20));
		volume.setHorizontalAlignment(JCheckBox.CENTER);
		volume.setBounds(175,550,150,50);
		volume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volume = (JCheckBox) e.getSource();
				if(volume.isSelected()){
					isVolumeMuted = true;
					director.setVolumeMute(true);
				}
				else{
					isVolumeMuted = false;
					director.setVolumeMute(false);
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
				Sound.setVolumeLevel((float)boost.getValue());
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
				if(director.isClientActive() == false) {
					enableLevels();
				}
				output.setText(" ");
				score.setText(" ");
				director.initBoard();
				director.newGame();
				
				clocks.resetClocks();
				
				updateGui();
				return;
			}
		});
		frame.add(newgame);
		
//		push.setFont(new Font("Dialog", Font.PLAIN, 45));
		forceBlack.setLayout(new GridLayout(2,1));
		forceBlack.add(pushButtonLabelTop);
		forceBlack.add(pushButtonLabelBottom);
		pushButtonLabelTop.setFont(new Font("Dialog", Font.PLAIN, 45));
		pushButtonLabelBottom.setFont(new Font("Dialog", Font.PLAIN, 45));
		pushButtonLabelTop.setHorizontalAlignment(JLabel.CENTER);
		pushButtonLabelBottom.setHorizontalAlignment(JLabel.CENTER);
		forceBlack.setBounds(250,350,150,100);		
		forceBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceBlack = (JButton) e.getSource();
				disableLevels();
				
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
		frame.add(forceBlack);

//		next.setFont(new Font("Dialog", Font.PLAIN, 45));
		nextBestMove.setLayout(new GridLayout(2,1));
		nextBestMove.add(nextBestMoveButtonLabelTop);
		nextBestMove.add(nextBestMoveButtonLabelBottom);
		nextBestMoveButtonLabelTop.setFont(new Font("Dialog", Font.PLAIN, 45));
		nextBestMoveButtonLabelBottom.setFont(new Font("Dialog", Font.PLAIN, 45));
		nextBestMoveButtonLabelTop.setHorizontalAlignment(JLabel.CENTER);
		nextBestMoveButtonLabelBottom.setHorizontalAlignment(JLabel.CENTER);
		nextBestMove.setBounds(100,350,150,100);		
		nextBestMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextBestMove = (JButton) e.getSource();

				if(director.isStartOfGame() || !Cache.isEmpty()) {
					return;
				}
				Clocks.setNodes(0);
				director.undoMove();
				updateGui();
				return;
			}
		});
		frame.add(nextBestMove);
		
		checkWarning.setFont(new Font("Dialog", Font.PLAIN, 25));
		checkWarning.setHorizontalAlignment(JCheckBox.CENTER);
		checkWarning.setBounds(1250,375,200,50);
		checkWarning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkWarning = (JCheckBox) e.getSource();
				if(checkWarning.isSelected()){
					isCheckWarningEnabled = true;
					director.setCheckWarning(true);
				}
				else{
					isCheckWarningEnabled = false;
					director.setCheckWarning(false);
				}
				return;
			}
		});
		frame.add(checkWarning);

		levelPanel.setBounds(1200,500,300,100);
		for(int i=0; i<levels.length; i++){
			brain[i] = new JToggleButton();
			brain[i].setText(levels[i]);
			levelButtons.add(brain[i]);
			levelPanel.add(brain[i]);
			brain[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton theButton = (JToggleButton) e.getSource();
					for(int i=0; i<levels.length; i++){
						if(theButton.equals(brain[i])){
							director.setLevel(i);
							output.setText("Level " + Integer.toString(i)+ " selected");
							if(director.isClientActive()) {
								director.disconnectClient();
							}
							return;
						}
					}
				}
			});
		}
		frame.getContentPane().add(levelPanel);
		
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
						if(theButton.equals(squares[r][c]) & clickNumber == 1 & 
								director.isPlayerPiece(squares[r][c].getName())){
							squares[r][c].setBackground(Color.YELLOW);
							highlightSquares(r, c);
							director.moveFrom(r, c, squares[r][c].getName());
							clickNumber = 2;
							output.setText(Message.getPieceName(squares[r][c].getName())
																		+" choosen");
							isMoveAsDrop = false;
							return;
							}
						else if(theButton.equals(squares[r][c]) & clickNumber == 2 & 
								director.isPlayerPiece(squares[r][c].getName())==false){
							if(director.moveTo(r, c)){
								if(isMoveAsDrop & squares[r][c].getName().equals(" ")){
									director.doDrop();
								}
								else if(isMoveAsDrop & !squares[r][c].getName().equals(" ")){
									isMoveAsDrop = false;
									output.setText("Wrong move!");
									updateGui();
									return;
								}
								else{
									director.doMove();
								}
								updateGui();
								clickNumber = 1;
								disableLevels();
								isMoveAsDrop = false;								
								
									javax.swing.SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											try {											
												Clocks.setNodes(0);
												Gui.nodes.setText(" ");
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
								clickNumber = 1;
								isMoveAsDrop = false;
								output.setText("Wrong move!");
								updateGui();
								return;
								}
							}
						else if(theButton.equals(squares[r][c]) & clickNumber == 2 & 
								director.isPlayerPiece(squares[r][c].getName())){
							squares[r][c].setBackground(Color.decode("#db9356"));
							clickNumber = 1;
							isMoveAsDrop = false;
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
				dropAreaWhite[c] = new JButton();
				dropAreaWhite[c].setSize(100, 100);
				dropAreaWhite[c].setBorder(new LineBorder(Color.GRAY));
				dropAreaWhite[c].setBackground(Color.decode("#db9356"));
				dropAreaWhite[c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton theButton = (JButton) e.getSource();
						for(int c=0;c<6;c++) {
						if(theButton.equals(dropAreaWhite[c])&clickNumber==1&director.isPlayerPiece(dropAreaWhite[c].getName())){
							isMoveAsDrop = true;
							dropAreaWhite[c].setBackground(Color.YELLOW);
							highlightSquares();
							director.moveFrom(3, c+3, dropAreaWhite[c].getName());						
							clickNumber = 2;							
							return;
							}
						else if(theButton.equals(dropAreaWhite[c]) & clickNumber == 2){
							dropAreaWhite[c].setBackground(Color.decode("#db9356"));
							clickNumber = 1;
							isMoveAsDrop = false;
							output.setText("Wrong place!");
							updateGui();
							return;
							}					}
						}
					});
				//add each square to the GUI panel
				handWhite.add(dropAreaWhite[c]);
		}
		for(int c=0;c<6;c++) {
				//Initialize the label in the board array
				dropAreaBlack[c] = new JButton();
				dropAreaBlack[c].setSize(100, 100);
				dropAreaBlack[c].setBorder(new LineBorder(Color.GRAY));
				dropAreaBlack[c].setBackground(Color.decode("#db9356"));
				//add each square to the GUI panel
				handBlack.add(dropAreaBlack[c]);
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
				squares[r][c].setName(director.refreshBoard(r,c));
				squares[r][c].setIcon(new ImageIcon(getLargeImage(squares[r][c].getName())));
			}
		}		
		for(int c=0;c<6;c++) {
			dropAreaWhite[c].setBackground(Color.decode("#db9356"));
			dropAreaWhite[c].setName(director.refreshBoard(3,c+3));
			dropAreaWhite[c].setIcon(new ImageIcon(getSmallImage(dropAreaWhite[c].getName())));
		}		
		for(int c=0;c<6;c++) {
			dropAreaBlack[c].setBackground(Color.decode("#db9356"));
			dropAreaBlack[c].setName(director.refreshBoard(0,c+3));
			dropAreaBlack[c].setIcon(new ImageIcon(getSmallImage(dropAreaBlack[c].getName())));			
		}
		
		if(isCheckWarningEnabled){
			for(int r=0;r<4;r++) {
				for(int c=0;c<3;c++) {
					checkWarning();
				}
			}	
		}
	}
	
	private void checkWarning(){
		
		int r2, c2;
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
		if(squares[r][c].getName().equals("p")){
			r2 = r+1;
			c2 = c;
			if((Pieces.BPAWN.isLegalMove(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);
			}
		}
		else if(squares[r][c].getName().equals("r")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.isLegalMove(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("k")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.isLegalMove(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("b")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.isLegalMove(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BQUEEN.isLegalMove(r, c, r2, c2)&&(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
			}
		}
	}
	
	private String getLargeImage(String piece){
		
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
	
	private String getSmallImage(String piece){
		
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
	
	private void highlightSquares(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(isMoveAsDrop & squares[r][c].getName().equals(" ")){
				squares[r][c].setBackground(Color.GREEN);
				}
			}
		}
	}
	
	private void highlightSquares(int r, int c){
		
		int r2, c2;
		
		if(squares[r][c].getName().equals("P")){
			r2 = r-1;
			c2 = c;
			if((Pieces.WPAWN.isLegalMove(r, c, r2, c2)&&director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
		}
		else if(squares[r][c].getName().equals("R")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.isLegalMove(r, c, r2, c2)&&director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("K")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.isLegalMove(r, c, r2, c2)&&director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("B")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.isLegalMove(r, c, r2, c2)&&director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("Q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.WQUEEN.isLegalMove(r, c, r2, c2)&&director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
	}
	
	public static void lockBoard(){
		
		nextBestMove.setEnabled(false);
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setEnabled(false);
			}
		}
		for(int c=0;c<6;c++) {
			dropAreaWhite[c].setEnabled(false);			
		}
		for(int c=0;c<6;c++) {
			dropAreaBlack[c].setEnabled(false);			
		}
	}
	
	private void unlockBoard(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setEnabled(true);								
			}
		}
		for(int c=0;c<6;c++) {
			dropAreaWhite[c].setEnabled(true);			
		}
		for(int c=0;c<6;c++) {
			dropAreaBlack[c].setEnabled(true);			
		}		
	}	
	
	private void unlockButtons(){				
		forceBlack.setEnabled(true);
		nextBestMove.setEnabled(true);		
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
		for(int i=0; i<levels.length; i++){
			if(brain[i].isSelected()){
				return brain[i].getText();
			}
		}
		return null;
	}
	public static String getPlayerName() {
		return profile.getText();
	}
	
	private void disableLevels() {
		
		forceBlack.setEnabled(false);
		for(int i=0; i<levels.length; i++){
			brain[i].setEnabled(false);
		}		
	}	
	private void enableLevels() {
		
		forceBlack.setEnabled(true);
		for(int i=0; i<levels.length; i++){
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
								disableLevels();
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
										director.shutdownServer();
										newgame.doClick();
									}
					            }				   
		   }
		});
		
        return other;
	}
		
}
