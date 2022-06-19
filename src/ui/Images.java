package ui;

import javax.swing.ImageIcon;

public class Images {
	
	private Images() {}
	
	private static int count;
	
	static synchronized Images getInstance() {
		if(count == 0) {
			count = 1;
			return new Images();
		}
		return null;
	}
	
	private static final String SLASH = System.getProperty("file.separator");

	private static final ImageIcon WHITE_PAWN_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"WP.png");
	private static final ImageIcon WHITE_ROOK_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"WR.png");
	private static final ImageIcon WHITE_BISHOP_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"WB.png");
	private static final ImageIcon WHITE_KING_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"WK.png");
	private static final ImageIcon WHITE_QUEEN_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"WQ.png");
	private static final ImageIcon BLACK_PAWN_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"BP.png");
	private static final ImageIcon BLACK_ROOK_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"BR.png");
	private static final ImageIcon BLACK_BISHOP_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"BB.png");
	private static final ImageIcon BLACK_KING_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"BK.png");
	private static final ImageIcon BLACK_QUEEN_LARGE = new ImageIcon("ui"+SLASH+"images"+SLASH+"large"+SLASH+"BQ.png");
	
	ImageIcon getLargeImage(String piece) {
		
		switch(piece){
		case "P":
			return WHITE_PAWN_LARGE;
		case "R":
			return WHITE_ROOK_LARGE;
		case "B":
			return WHITE_BISHOP_LARGE;
		case "K":
			return WHITE_KING_LARGE;
		case "Q":
			return WHITE_QUEEN_LARGE;
		case "p":
			return BLACK_PAWN_LARGE;
		case "r":
			return BLACK_ROOK_LARGE;
		case "b":
			return BLACK_BISHOP_LARGE;
		case "k":
			return BLACK_KING_LARGE;
		case "q":
			return BLACK_QUEEN_LARGE;
		default:
			return null;
		}
	}

	private static final ImageIcon WHITE_PAWN_SMALL = new ImageIcon("ui/images/small/WP.png");
	private static final ImageIcon WHITE_ROOK_SMALL = new ImageIcon("ui/images/small/WR.png");
	private static final ImageIcon WHITE_BISHOP_SMALL = new ImageIcon("ui/images/small/WB.png");
	private static final ImageIcon WHITE_KING_SMALL = new ImageIcon("ui/images/small/WK.png");
	private static final ImageIcon WHITE_QUEEN_SMALL = new ImageIcon("ui/images/small/WQ.png");
	private static final ImageIcon BLACK_PAWN_SMALL = new ImageIcon("ui/images/small/BP.png");
	private static final ImageIcon BLACK_ROOK_SMALL = new ImageIcon("ui/images/small/BR.png");
	private static final ImageIcon BLACK_BISHOP_SMALL = new ImageIcon("ui/images/small/BB.png");
	private static final ImageIcon BLACK_KING_SMALL = new ImageIcon("ui/images/small/BK.png");
	private static final ImageIcon BLACK_QUEEN_SMALL = new ImageIcon("ui/images/small/BQ.png");
	
	ImageIcon getSmallImage(String piece) {
		
		switch(piece){
		case "P":
			return WHITE_PAWN_SMALL;
		case "R":
			return WHITE_ROOK_SMALL;
		case "B":
			return WHITE_BISHOP_SMALL;
		case "K":
			return WHITE_KING_SMALL;
		case "Q":
			return WHITE_QUEEN_SMALL;
		case "p":
			return BLACK_PAWN_SMALL;
		case "r":
			return BLACK_ROOK_SMALL;
		case "b":
			return BLACK_BISHOP_SMALL;
		case "k":
			return BLACK_KING_SMALL;
		case "q":
			return BLACK_QUEEN_SMALL;
		default:
			return null;
		}
	}
	
}
