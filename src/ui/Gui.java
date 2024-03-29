package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import ai.component.Cache;
import control.Clocks;
import control.Director;
import sound.Sound;
import utilpack.Expositor;
import utilpack.Pieces;

public class Gui {
	
	private Director director = Director.getInstance();	
	private Clocks clocks = Clocks.getInstance();
	private Images images = Images.getInstance();
	
	private int clickNumber = 1;
	private boolean moveIsDrop = false;
	private boolean checkWarningEnabled = true;	
	boolean isVolumeMuted = false;

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
	static JLabel profile = new JLabel("Player");
	private static JLabel computer = new JLabel("Computer");
	public static JLabel clockBlack = new JLabel("00:00");
	public static JLabel clockWhite = new JLabel("00:00");
	private static JLabel nodesLabel = new JLabel("Nodes:");
	public static JLabel nodes = new JLabel(" ");
	public static JLabel output = new JLabel(" ");
	public static JLabel score = new JLabel(" ");
	
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
	private static String[] levels = {"Stupid", "Greedy", "Quirky", "Tricky", 
								  "Active", "Clever", "Expert", "Master"};
	private static JToggleButton[] brain = new JToggleButton[levels.length];
	
	private static JMenuBar menuBar = new JMenuBar();
	
	private int k;
	public Gui(int k) {
		this.k = k;
	}
    
	public void launch() {
		
		Images.k = k;
		
		new Thread(() -> clocks.showClocks()).start();
		
		director.initBoard();
		
        menuBar.add(createFileMenu());
        menuBar.add(createAccountMenu());
        menuBar.add(createOtherMenu());
        frame.setJMenuBar(menuBar);
		
		// fixed positions layout
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(1600*k,1000*k);
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
		
		board.setBounds(500*k,100*k,600*k,800*k);
		handBlack.setBounds(100*k,100*k,300*k,200*k);
		handWhite.setBounds(1200*k,700*k,300*k,200*k);
		columns.setBounds(500*k,50*k,600*k,50*k);
		rows.setBounds(450*k,100*k,50*k,800*k);		
		output.setFont(new Font("Dialog", Font.PLAIN, 20*k));
		output.setHorizontalAlignment(JLabel.CENTER);
		output.setBounds(100*k,475*k,300*k,50*k);
		output.setOpaque(true);
		output.setBackground(Color.LIGHT_GRAY);
		behaveLabel.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		behaveLabel.setHorizontalAlignment(JLabel.CENTER);
		behaveLabel.setBounds(1200*k,450*k,300*k,50*k);
		score.setFont(new Font("Dialog", Font.PLAIN, 40*k));
		score.setHorizontalAlignment(JLabel.CENTER);
		score.setBounds(100*k,825*k,300*k,75*k);
		score.setOpaque(true);
		score.setBackground(Color.LIGHT_GRAY);
		scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 45*k));
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setBounds(100*k,750*k,300*k,50*k);
		columnA.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		columnA.setHorizontalAlignment(JLabel.CENTER);
//		colA.setBounds(500,50,200,50);
		columnB.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		columnB.setHorizontalAlignment(JLabel.CENTER);
//		colB.setBounds(700,50,200,50);
		columnC.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		columnC.setHorizontalAlignment(JLabel.CENTER);
//		colC.setBounds(900,50,200,50);
		row1.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		row1.setHorizontalAlignment(JLabel.CENTER);
//		row1.setBounds(450,175,50,50);
		row2.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		row2.setHorizontalAlignment(JLabel.CENTER);
//		row2.setBounds(450,375,50,50);
		row3.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		row3.setHorizontalAlignment(JLabel.CENTER);
//		row3.setBounds(450,575,50,50);
		row4.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		row4.setHorizontalAlignment(JLabel.CENTER);
//		row4.setBounds(450,775,50,50);
		whiteHand.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		whiteHand.setHorizontalAlignment(JLabel.CENTER);
		whiteHand.setBounds(1200*k,650*k,300*k,50*k);
		blackHand.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		blackHand.setHorizontalAlignment(JLabel.CENTER);
		blackHand.setBounds(100*k,50*k,300*k,50*k);		
		profile.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		profile.setHorizontalAlignment(JLabel.CENTER);
		profile.setBounds(1200*k,225*k,300*k,50*k);
		columns.add(columnA);
		columns.add(columnB);
		columns.add(columnC);
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		rows.add(row4);
		
		clockBlack.setBounds(1200*k,100*k,300*k,50*k);
		clockBlack.setFont(new Font("Dialog", Font.PLAIN, 50*k));
		clockBlack.setHorizontalAlignment(JLabel.CENTER);
		clockBlack.setOpaque(true);
		clockBlack.setBackground(Color.BLACK);
		clockBlack.setForeground(Color.WHITE);
		clockWhite.setBounds(1200*k,175*k,300*k,50*k);
		clockWhite.setFont(new Font("Dialog", Font.PLAIN, 50*k));
		clockWhite.setHorizontalAlignment(JLabel.CENTER);
		clockWhite.setOpaque(true);
		clockWhite.setBackground(Color.WHITE);
		computer.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		computer.setHorizontalAlignment(JLabel.CENTER);
		computer.setBounds(1200*k,50*k,300*k,50*k);
		nodesLabel.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		nodesLabel.setHorizontalAlignment(JLabel.LEFT);
		nodesLabel.setBounds(100*k,675*k,100*k,50*k);
		nodes.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		nodes.setHorizontalAlignment(JLabel.CENTER);
		nodes.setBounds(200*k,675*k,200*k,50*k);
		nodes.setOpaque(true);
		nodes.setBackground(Color.LIGHT_GRAY);
		
		volume.setFont(new Font("Dialog", Font.PLAIN, 20*k));
		volume.setHorizontalAlignment(JCheckBox.CENTER);
		volume.setBounds(175*k,550*k,150*k,50*k);
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
		
		boost.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		boost.setBounds(100*k,600*k,300*k,50*k);
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

		newgame.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		newgame.setBounds(1200*k,300*k,300*k,50*k);
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newgame = (JButton) e.getSource();
				
				unlockBoard();
				unlockButtons();
				if(director.isClientActive() == false) {
					enableLevels();
				}
				nodes.setText(" ");
				Clocks.setNodes(0);
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
		pushButtonLabelTop.setFont(new Font("Dialog", Font.PLAIN, 45*k));
		pushButtonLabelBottom.setFont(new Font("Dialog", Font.PLAIN, 45*k));
		pushButtonLabelTop.setHorizontalAlignment(JLabel.CENTER);
		pushButtonLabelBottom.setHorizontalAlignment(JLabel.CENTER);
		forceBlack.setBounds(250*k,350*k,150*k,100*k);		
		forceBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceBlack = (JButton) e.getSource();
				prepareGui();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {	
//				javax.swing.SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
						try {
							director.compute();
							updateGui();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
				return;
			}
		});
		frame.add(forceBlack);

//		next.setFont(new Font("Dialog", Font.PLAIN, 45));
		nextBestMove.setLayout(new GridLayout(2,1));
		nextBestMove.add(nextBestMoveButtonLabelTop);
		nextBestMove.add(nextBestMoveButtonLabelBottom);
		nextBestMoveButtonLabelTop.setFont(new Font("Dialog", Font.PLAIN, 45*k));
		nextBestMoveButtonLabelBottom.setFont(new Font("Dialog", Font.PLAIN, 45*k));
		nextBestMoveButtonLabelTop.setHorizontalAlignment(JLabel.CENTER);
		nextBestMoveButtonLabelBottom.setHorizontalAlignment(JLabel.CENTER);
		nextBestMove.setBounds(100*k,350*k,150*k,100*k);		
		nextBestMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextBestMove = (JButton) e.getSource();

				if(director.isStartOfGame() || !Cache.isEmpty()) {
					return;
				}
				director.undoMove();
				updateGui();
				return;
			}
		});
		frame.add(nextBestMove);
		
		checkWarning.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		checkWarning.setHorizontalAlignment(JCheckBox.CENTER);
		checkWarning.setBounds(1250*k,375*k,200*k,50*k);
		checkWarning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkWarning = (JCheckBox) e.getSource();
				if(checkWarning.isSelected()){
					checkWarningEnabled = true;
					director.setCheckWarning(true);
				}
				else{
					checkWarningEnabled = false;
					director.setCheckWarning(false);
				}
				return;
			}
		});
		frame.add(checkWarning);

		levelPanel.setBounds(1200*k,500*k,300*k,100*k);
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
				squares[r][c].setSize(200*k, 200*k);
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
								output.setText(Expositor.getPieceName(squares[r][c].getName())
																			+" choosen");
								moveIsDrop = false;
								return;
							}
							else if(theButton.equals(squares[r][c]) & clickNumber == 2 & 
									director.isPlayerPiece(squares[r][c].getName())==false){
								if(director.moveTo(r, c)){
									if(moveIsDrop & squares[r][c].getName().equals(" ")){
										director.doDrop();
									}
									else if(moveIsDrop & !squares[r][c].getName().equals(" ")){
										moveIsDrop = false;
										output.setText("Wrong move!");
										updateGui();
										return;
									}
									else{
										director.doMove();
									}
									
									prepareGui();									
									clickNumber = 1;
									moveIsDrop = false;								
	
									Thread t = new Thread(new Runnable() {
										@Override
										public void run() {								
	//									javax.swing.SwingUtilities.invokeLater(new Runnable() {
	//										public void run() {
												try {
													director.compute();
													updateGui();
												}
												catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										});
									t.start();
									return;
								}
								else{
									squares[r][c].setBackground(Color.decode("#db9356"));
									clickNumber = 1;
									moveIsDrop = false;
									output.setText("Wrong move!");
									updateGui();
									return;
								}
							}
							else if(theButton.equals(squares[r][c]) & clickNumber == 2 & 
									director.isPlayerPiece(squares[r][c].getName())){
								squares[r][c].setBackground(Color.decode("#db9356"));
								clickNumber = 1;
								moveIsDrop = false;
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
				dropAreaWhite[c].setSize(100*k, 100*k);
				dropAreaWhite[c].setBorder(new LineBorder(Color.GRAY));
				dropAreaWhite[c].setBackground(Color.decode("#db9356"));
				dropAreaWhite[c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton theButton = (JButton) e.getSource();
						for(int c=0;c<6;c++) {
						if(theButton.equals(dropAreaWhite[c])&clickNumber==1
								&director.isPlayerPiece(dropAreaWhite[c].getName())){
							moveIsDrop = true;
							dropAreaWhite[c].setBackground(Color.YELLOW);
							highlightSquares();
							director.moveFrom(3, c+3, dropAreaWhite[c].getName());						
							clickNumber = 2;							
							return;
							}
						else if(theButton.equals(dropAreaWhite[c]) & clickNumber == 2){
							dropAreaWhite[c].setBackground(Color.decode("#db9356"));
							clickNumber = 1;
							moveIsDrop = false;
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
				dropAreaBlack[c].setSize(100*k, 100*k);
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
	
	private void prepareGui() {
		
		score.setText(" ");
		Clocks.resetScore();
		nodes.setText(" ");
		Clocks.setNodes(0);
		disableLevels();
		updateGui();
	}

	private void updateGui() {
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				squares[r][c].setBackground(Color.decode("#db9356"));
				squares[r][c].setName(director.refreshBoard(r,c));
				squares[r][c].setIcon(images.getLargeImage(squares[r][c].getName()));
			}
		}		
		for(int c=0;c<6;c++) {
			dropAreaWhite[c].setBackground(Color.decode("#db9356"));
			dropAreaWhite[c].setName(director.refreshBoard(3,c+3));
			dropAreaWhite[c].setIcon(images.getSmallImage(dropAreaWhite[c].getName()));
		}		
		for(int c=0;c<6;c++) {
			dropAreaBlack[c].setBackground(Color.decode("#db9356"));
			dropAreaBlack[c].setName(director.refreshBoard(0,c+3));
			dropAreaBlack[c].setIcon(images.getSmallImage(dropAreaBlack[c].getName()));
		}
		
		if(checkWarningEnabled){
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
			if((Pieces.BPAWN.isLegalMove(r, c, r2, c2)&&
					(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);
			}
		}
		else if(squares[r][c].getName().equals("r")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.isLegalMove(r, c, r2, c2)&&
					(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("k")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.isLegalMove(r, c, r2, c2)&&
					(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("b")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.isLegalMove(r, c, r2, c2)&&
					(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BQUEEN.isLegalMove(r, c, r2, c2)&&
					(squares[r2][c2].getName().equals("K")))){
				squares[r2][c2].setBackground(Color.RED);				
			}
				}
			}
		}
			}
		}
	}
	
	private void highlightSquares(){
		
		for(int r=0;r<4;r++) {
			for(int c=0;c<3;c++) {
				if(moveIsDrop & squares[r][c].getName().equals(" ")){
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
			if((Pieces.WPAWN.isLegalMove(r, c, r2, c2)&&
					director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
		}
		else if(squares[r][c].getName().equals("R")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.ROOK.isLegalMove(r, c, r2, c2)&&
					director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("K")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.KING.isLegalMove(r, c, r2, c2)&&
					director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("B")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.BISHOP.isLegalMove(r, c, r2, c2)&&
					director.isLegalMove(squares[r2][c2].getName()))){
				squares[r2][c2].setBackground(Color.GREEN);				
			}
				}
			}
		}
		else if(squares[r][c].getName().equals("Q")){
			for(r2=r-1; r2<r+2; r2++){
				for(c2=c-1; c2<c+2; c2++){
			if((Pieces.WQUEEN.isLegalMove(r, c, r2, c2)&&
					director.isLegalMove(squares[r2][c2].getName()))){
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
								disableLevels();
								Gui.doClick();
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
