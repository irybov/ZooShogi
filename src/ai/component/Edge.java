package ai.component;

public class Edge {
	
	private final int depth;
	private int value;
	
	public Edge(int depth, int value) {
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
