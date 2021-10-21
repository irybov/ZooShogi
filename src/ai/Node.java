package ai;

import java.util.ArrayList;
import java.util.List;

import util.Message;

public class Node implements Comparable<Node>{

	private int depth;
	private int value;
	private List<Node> children;
	
	private final int R;
	private final int C;
	private final int R2;
	private final int C2;
	private final String side;

	public Node(int r, int c, int r2, int c2, String side) {
		this.R = r;
		this.C = c;
		this.R2 = r2;
		this.C2 = c2;
		this.side = side;
	}
	
	@Override
	public String toString() {
		return "Node [depth=" + depth + ", value=" + value + ", side=" + side + ", "+ Message.colName(C) + (R+1) + Message.colName(C2) + (R2+1) + "]";
	}
/*	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + C;
		result = prime * result + C2;
		result = prime * result + R;
		result = prime * result + R2;
		result = prime * result + depth;
		result = prime * result + value;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (C != other.C)
			return false;
		if (C2 != other.C2)
			return false;
		if (R != other.R)
			return false;
		if (R2 != other.R2)
			return false;
		if (depth != other.depth)
		return false;
		if (value != other.value)
			return false;
		return true;
	}
*/	
	@Override
	public int compareTo(Node node) {

		if(this.value == node.value){		
			return 0;
		}
		else if(this.value < node.value){
			return -1;			
		}
		else{
			return 1;			
		}
	}

	public int getR() {
		return R;
	}
	public int getC() {
		return C;
	}
	public int getR2() {
		return R2;
	}
	public int getC2() {
		return C2;
	}
	public String getSide() {
		return side;
	}

	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public List<Node> getChidren() {
		return children;
	}
	public void addChildren(List<Node> newChildren) {
		children = new ArrayList<>(newChildren);
	}
	public boolean hasChildren() {
		if(children == null) {
			return false;
		}
		return true;
	}
	
}
