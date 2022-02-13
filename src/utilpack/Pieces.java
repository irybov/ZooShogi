package utilpack;

public enum Pieces {

	WPAWN("Pawn", 20) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if(index(r2, c2)){
				return (r2==r-1 & c2==c);
			}
			return false;
		}
	},
	BPAWN("Pawn", 20) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if(index(r2, c2)){
				return (r2==r+1 & c2==c);
			}
			return false;
		}
	},
	ROOK("Rook", 40) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if(index(r2, c2)){
				return ((r2==r&(c2==c-1||c2==c+1)) || ((r2==r-1||r2==r+1)&c2==c));
			}
			return false;
		}
	},
	BISHOP("Bishop", 30) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if(index(r2, c2)){
				return ((r2==r-1||r2==r+1) & (c2==c-1||c2==c+1));
			}
			return false;
		}
	},
	KING("King", 0) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if(index(r2, c2)){
				return ((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1));
			}
			return false;
		}
	},
	WQUEEN("Queen", 50) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if((r2 == r+1) & (c2 == c-1 || c2 == c+1)) {
				return false;
			}
			if(index(r2, c2)){
				return ((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1));
			}
			return false;
		}
	},
	BQUEEN("Queen", 50) {
		@Override
		public boolean isLegalMove(int r, int c, int r2, int c2){
			if((r2 == r-1) & (c2 == c-1 || c2 == c+1)) {
				return false;
			}
			if(index(r2, c2)){
				return ((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1));
			}
			return false;
		}
	};

	private final String name;
	private final int value;
	
	Pieces(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public int getValue() {
		return value;
	}
	
	public boolean isLegalMove(int r, int c, int r2, int c2) {
		return false;
	}
	private static boolean index(int r2, int c2) {
		return ((r2>=0 && r2<=3) & (c2>=0 && c2<=2));
	}
	
}
