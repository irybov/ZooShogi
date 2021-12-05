package ai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>, Serializable{

	private int depth;
	private int value;
	private List<Node> children;
	private Node parent;
	private int trap = Integer.MIN_VALUE;
	
	private final int R;
	private final int C;
	private final int R2;
	private final int C2;
	private final String SIDE;

	public Node(int R, int C, int R2, int C2, String SIDE) {
		this.R = R;
		this.C = C;
		this.R2 = R2;
		this.C2 = C2;
		this.SIDE = SIDE;
	}
	
	@Override
	public String toString() {
		return "Node [depth=" + depth + ", value=" + value + ", side=" + SIDE + ", "
													+ (R) + (C) + (R2) + (C2) + "]";
	}

	@Override
	public int compareTo(Node node) {

		if(this.value == node.value){
			if(this.trap == node.trap) {
				return 0;
			}
			else if(this.trap < node.trap){
				return -1;			
			}
			else{
				return 1;			
			}
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
		return SIDE;
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
	
	public Node getParent() {
		return parent;
	}
	public void addParent(Node newParent) {
		parent = newParent;
	}
	public boolean hasParent() {
		if(parent == null) {
			return false;
		}
		return true;
	}
	
	public int getTrap(){
		return trap;
	}
	public void setTrap(int newTrap) {
		trap = newTrap;
	}
	
}
