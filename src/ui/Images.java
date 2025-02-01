package ui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Images {
	
	private Images() {}
	
	private static int count;
	static int k;
	
	static synchronized Images getInstance() {
		if(count == 0) {
			count = 1;
			return new Images();
		}
		return null;
	}
	
	private static final String SLASH = System.getProperty("file.separator");

	private static final ImageIcon WHITE_PAWN_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "WP.png"));
	private static final ImageIcon WHITE_ROOK_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "WR.png"));
	private static final ImageIcon WHITE_BISHOP_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "WB.png"));
	private static final ImageIcon WHITE_KING_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "WK.png"));
	private static final ImageIcon WHITE_QUEEN_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "WQ.png"));
	private static final ImageIcon BLACK_PAWN_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "BP.png"));
	private static final ImageIcon BLACK_ROOK_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "BR.png"));
	private static final ImageIcon BLACK_BISHOP_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "BB.png"));
	private static final ImageIcon BLACK_KING_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "BK.png"));
	private static final ImageIcon BLACK_QUEEN_LARGE = new ImageIcon(String.join(SLASH, "ui", "images", "large", "BQ.png"));
	
	ImageIcon getLargeImage(String piece) {
		
		ImageIcon icon = null;
		
		switch(piece){
		case "P":
			icon = WHITE_PAWN_LARGE;
			break;
		case "R":
			icon = WHITE_ROOK_LARGE;
			break;
		case "B":
			icon = WHITE_BISHOP_LARGE;
			break;
		case "K":
			icon = WHITE_KING_LARGE;
			break;
		case "Q":
			icon = WHITE_QUEEN_LARGE;
			break;
		case "p":
			icon = BLACK_PAWN_LARGE;
			break;
		case "r":
			icon = BLACK_ROOK_LARGE;
			break;
		case "b":
			icon = BLACK_BISHOP_LARGE;
			break;
		case "k":
			icon = BLACK_KING_LARGE;
			break;
		case "q":
			icon = BLACK_QUEEN_LARGE;
			break;
		}
		
		if(icon != null) {
			if(k == 1) {return icon;}
			else {
				Image original = icon.getImage();
				Image resized = original.getScaledInstance(icon.getIconWidth()*k, 
								icon.getIconHeight()*k, 
								java.awt.Image.SCALE_SMOOTH);
				return new ImageIcon(resized);
			}
		}
		else {return null;}
	}

	private static final ImageIcon WHITE_PAWN_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "WP.png"));
	private static final ImageIcon WHITE_ROOK_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "WR.png"));
	private static final ImageIcon WHITE_BISHOP_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "WB.png"));
	private static final ImageIcon WHITE_KING_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "WK.png"));
	private static final ImageIcon WHITE_QUEEN_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "WQ.png"));
	private static final ImageIcon BLACK_PAWN_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "BP.png"));
	private static final ImageIcon BLACK_ROOK_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "BR.png"));
	private static final ImageIcon BLACK_BISHOP_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "BB.png"));
	private static final ImageIcon BLACK_KING_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "BK.png"));
	private static final ImageIcon BLACK_QUEEN_SMALL = new ImageIcon(String.join(SLASH, "ui", "images", "small", "BQ.png"));
	
	ImageIcon getSmallImage(String piece) {

		ImageIcon icon = null;
		
		switch(piece){
		case "P":
			icon =  WHITE_PAWN_SMALL;
			break;
		case "R":
			icon =  WHITE_ROOK_SMALL;
			break;
		case "B":
			icon =  WHITE_BISHOP_SMALL;
			break;
		case "K":
			icon =  WHITE_KING_SMALL;
			break;
		case "Q":
			icon =  WHITE_QUEEN_SMALL;
			break;
		case "p":
			icon =  BLACK_PAWN_SMALL;
			break;
		case "r":
			icon =  BLACK_ROOK_SMALL;
			break;
		case "b":
			icon =  BLACK_BISHOP_SMALL;
			break;
		case "k":
			icon =  BLACK_KING_SMALL;
			break;
		case "q":
			icon =  BLACK_QUEEN_SMALL;
			break;
		}
		
		if(icon != null) {
			if(k == 1) {return icon;}
			else {
				Image original = icon.getImage();
				Image resized = original.getScaledInstance(
								icon.getIconWidth()*k, 
								icon.getIconHeight()*k, 
								java.awt.Image.SCALE_SMOOTH);
				return new ImageIcon(resized);
			}
		}
		else {return null;}
	}
	
}
