package ai;

import utilpack.Message;

public class Edge {
	
	private final int R;
	private final int C;
	private final int R2;
	private final int C2;
	private final String PIECE;

	public Edge(int R, int C, int R2, int C2, String PIECE) {
		this.R = R;
		this.C = C;
		this.R2 = R2;
		this.C2 = C2;
		this.PIECE = PIECE.toUpperCase();
	}
	
	@Override
	public String toString() {
		return (C < 3 ? Message.colName(C) + (R+1) : PIECE + Message.colName(C))
												+ Message.colName(C2) + (R2+1);
	}

}
