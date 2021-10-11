package util;

public class Matrix {
	
	String[][] board;
	
	private Matrix() {
		board = new String[][]{
			{"0" ,"1" ,"2" ,"3" ,"4" ,"5" ,"6" ,"7" ,"8" ,"9"},
			{"10","11","12"},
			{"13","14","15"},
			{"16","17","18","19","20","21","22","23","24","25"}};
	}
	
	public String getCode(int r, int c) {	
		return board[r][c];
	}
		
}
