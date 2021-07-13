package util;

public enum Pieces {

	PAWN("Pawn", 20) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			return ((r2>=0 & r2<=3) & (c2>=0 & c2<=2));
		}
	},
	ROOK("Rook", 40) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			return((((r2==r)&(c2==c-1||c2==c+1)) || ((r2==r-1||r2==r+1)&(c2==c))) &
					   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
		}
	},
	BISHOP("Bishop", 30) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			return(((r2==r-1||r2==r+1) & (c2==c-1||c2==c+1)) &
					   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
		}
	},
	KING("King", 0) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			return(((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1)) &
					   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
		}
	},
	WQUEEN("Queen", 50) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			if((r2 == r+1) & (c2 == c-1 || c2 == c+1)) {
				return false;
			}
			return (((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1)) &
					   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
		}
	},
	BQUEEN("Queen", 50) {
		@Override
		boolean move(int r, int c, int r2, int c2){
			if((r2 == r-1) & (c2 == c-1 || c2 == c+1)) {
				return false;
			}
			return (((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1)) &
					   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
		}
	};

	private final String name;
	private final int value;
	
	Pieces(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	String getName() {
		return name;
	}
	int getValue() {
		return value;
	}
	boolean move(int r, int c, int r2, int c2) {
		return false;
	}
		
}
