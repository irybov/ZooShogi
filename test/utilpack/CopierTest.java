package utilpack;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilpack.Copier;
import utilpack.Matrix;

public class CopierTest {

	private static char[][] board;
	
	@BeforeClass
	public static void init_board() {
		board = new char[][]{{'r','k','b',' ',' ',' ',' ',' ',' '},
			   				 {' ','p',' '},
			   				 {' ','P',' '},
			   				 {'B','K','R',' ',' ',' ',' ',' ',' '}};				
	}
	
	@Test
	public void deep_copy() {
		assertTrue(Arrays.deepEquals(board, Copier.deepCopy(board)));
	}
	
	@Test(expected = NullPointerException.class)
	public void empty_copy() {		
		char[][] board = null;		
		assertNull(Matrix.makeKey(board));
	}
	
	@AfterClass
	public static void clear_memory() {
		board = null;
	}

}
