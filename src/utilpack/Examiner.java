package utilpack;

import java.util.List;

public final class Examiner {
	
	private Examiner() {}
	
	public static boolean isCheck(char[][] board, Turn turn) {
		
		List<Direction> directions;
		
		int r,c,r2,c2;
			
		if(turn.equals(Turn.BLACK)){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c]==('p')){
						r2 = r+1;
						c2 = c;
						if((Piece.BPAWN.isLegalMove(r, c, r2, c2))&&
						   (board[r2][c2]==('K'))){
							return true;
						}
					}
					
					else if(board[r][c]==('r')){						
						directions = Verifier.getDirections('r');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('K'))){
								return true;
							}							
						}
					}

					else if(board[r][c]==('k')){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
								if(r2==0 && c2==0) {}
							if((Piece.KING.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('K'))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c]==('b')){						
						directions = Verifier.getDirections('b');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.BISHOP.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('K'))){
								return true;
							}							
						}
					}
					
					else if(board[r][c]==('q')){						
						directions = Verifier.getDirections('q');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.BQUEEN.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('K'))){
								return true;
							}							
						}
					}
				}
			}
		}				
		else if(turn.equals(Turn.WHITE)){
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c]==('P')){
						r2 = r-1;
						c2 = c;
						if((Piece.WPAWN.isLegalMove(r, c, r2, c2))&&
						   (board[r2][c2]==('k'))){
							return true;
						}
					}
					
					else if(board[r][c]==('R')){						
						directions = Verifier.getDirections('R');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('k'))){
								return true;
							}							
						}
					}
					
					else if(board[r][c]==('K')){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
								if(r2==0 && c2==0) {}
							if((Piece.KING.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('k'))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c]==('B')){						
						directions = Verifier.getDirections('B');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.BISHOP.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('k'))){
								return true;
							}							
						}
					}
					
					else if(board[r][c]==('Q')){						
						directions = Verifier.getDirections('Q');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.WQUEEN.isLegalMove(r, c, r2, c2))&&
								(board[r2][c2]==('k'))){
								return true;
							}							
						}
					}
				}
			}
		}
	return false;
	}
	
	public static boolean isPromotionWon(char[][] board, Turn turn) {
		
		if(turn.equals(Turn.BLACK)) {
			return (board[3][0]==('k')||board[3][1]==('k')||board[3][2]==('k')) &
			(board[0][0]!=('K')&&board[0][1]!=('K')&&board[0][2]!=('K'));		
		}
		else if(turn.equals(Turn.WHITE)){
			return (board[0][0]==('K')||board[0][1]==('K')||board[0][2]==('K')) & 
			(board[3][0]!=('k')&&board[3][1]!=('k')&&board[3][2]!=('k'));
		}
		return false;
	}

	public static boolean isBlackPositionWon(char[][] board, Turn turn)  {
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c]==('K')){
					a = 2;
				}
				if(board[r][c]==('k')){
					b = 1;
				}
			}
		}		
		return((a+b==1) || ((a+b==3 & turn.equals(Turn.BLACK)) & 
			   (board[3][0]==('k')||board[3][1]==('k')||board[3][2]==('k'))));
	}
	
	public static boolean isWhitePositionWon(char[][] board, Turn turn)  {
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c]==('K')){
					a = 2;
				}
				if(board[r][c]==('k')){
					b = 1;
				}
			}
		}		
		return((a+b==2) || ((a+b==3 & turn.equals(Turn.WHITE)) & 
			   (board[0][0]==('K')||board[0][1]==('K')||board[0][2]==('K'))));
	}	
	
}
