package ai.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilpack.Turn;

public class Node implements Comparable<Node>, Serializable{

	private int depth;
	private int value;
	private List<Node> children;
	private Node parent;
	private int trappiness = Integer.MIN_VALUE;
	
	private final int rowFrom;
	private final int columnFrom;
	private final int rowTo;
	private final int columnTo;
	private final Turn SIDE;

	public Node(int R, int C, int R2, int C2, Turn SIDE) {
		this.rowFrom = R;
		this.columnFrom = C;
		this.rowTo = R2;
		this.columnTo = C2;
		this.SIDE = SIDE;
	}
	
	@Override
	public String toString() {
		return "Node [depth=" + depth + ", value=" + value + ", side=" + SIDE.name() + ", "
										+ (rowFrom) + (columnFrom) + (rowTo) + (columnTo) + "]";
	}

	@Override
	public int compareTo(Node node) {

		if(this.value == node.value){
			if(this.trappiness == node.trappiness) {
				return 0;
			}
			else if(this.trappiness < node.trappiness){
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

	public int getRowFrom() {
		return rowFrom;
	}
	public int getColumnFrom() {
		return columnFrom;
	}
	public int getRowTo() {
		return rowTo;
	}
	public int getColumnTo() {
		return columnTo;
	}
	public Turn getSide() {
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
	
	public int getTrappiness(){
		return trappiness;
	}
	public void setTrappiness(int newTrap) {
		trappiness = newTrap;
	}
	
}
