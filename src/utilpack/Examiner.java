package utilpack;

public class Examiner {
	
	public static boolean check(String[][] board, String turn) {		
		
		int r,c,r2,c2;
			
		if(turn.equals("black")){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pieces.BPAWN.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("K"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("r")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("k")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("b")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BQUEEN.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
				}
			}
		}
				
		else{
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("P")){
						r2 = r-1;
						c2 = c;
						if((Pieces.WPAWN.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("k"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("R")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("K")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("B")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("Q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.WQUEEN.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
				}
			}
		}
	return false;
	}
	
	public static boolean winPromotion(String[][] board, String turn) {
		
		if(turn.equals("black")) {
			return (board[3][0].equals("k")||board[3][1].equals("k")||board[3][2].equals("k")) &
			(!board[0][0].equals("K")&&!board[0][1].equals("K")&&!board[0][2].equals("K"));		
		}
		else {
			return (board[0][0].equals("K")||board[0][1].equals("K")||board[0][2].equals("K")) & 
			(!board[3][0].equals("k")&&!board[3][1].equals("k")&&!board[3][2].equals("k"));
		}
	}

	public static boolean winPosition(String[][] board, String turn)  {		
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c].equals("K")){
					a = 2;
				}
				if(board[r][c].equals("k")){
					b = 1;
				}
			}
		}
		
		if(turn.equals("white")){
			return (a+b==1);
		}
		else {
			return (a+b==2);			
		}
	}
	
}
