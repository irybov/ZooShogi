package utilpack;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilpack.Matrix;

public class MatrixTest {

	private static char[][] board;
	private static String key;
	
	@BeforeClass
	public static void init_board() {
		board = new char[][]{{'r','k',' ','b',' ',' ',' ',' ',' '},
				  			 {' ',' ','p'},
				  			 {' ','P',' '},
				  			 {'B','K',' ',' ',' ','R',' ',' ',' '}};				  			   
	}
	
	@Test
	public void keymaker_create() {		
		assertNotNull(Matrix.makeKey(board));
	}

	@Test(expected = NullPointerException.class)
	public void keymaker_empty() {		
		char[][] board = null;		
		assertNull(Matrix.makeKey(board));
	}
	
	@Test
	public void keymaker_length() {
		assertEquals(24, Matrix.makeKey(board).length());
	}
	
	@Test
	public void keyswapper_correct() {
		key = Matrix.makeKey(board);
		assertEquals("#krp###P##KBb#######R###", Matrix.swapKey(key));
	}
	
	@AfterClass
	public static void clear_memory() {
		board = null;
		key = null;
	}
	
}
