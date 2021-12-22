package ai;

class Edge {
	
	private final int depth;
	private int value;
	
	Edge(int depth, int value) {
		this.depth = depth;
		this.value = value;
	}

	int getDepth() {
		return this.depth;
	}
	int getValue() {
		return this.value;
	}
	void setValue(int value) {
		this.value = value;
	}
	
}
