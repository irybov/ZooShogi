package utilpack;

public enum Direction {
	NORTH(-1,0), SOUTH(+1,0), EAST(0,+1), WEST(0,-1), NE(-1,+1), NW(-1,-1), SE(+1,+1), SW(+1,-1);
	
	private final int x;
	private final int y;
	
	Direction(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
