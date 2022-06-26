package utilpack;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilpack.Copier;
import utilpack.Matrix;

public class CopierTest {

	private static String[][] board;
	
	@BeforeClass
	public static void init_board() {
		board = new String[][]{{"r","k","b"," "," "," "," "," "," "},
				  			   {" ","p"," "},
				  			   {" ","P"," "},
				  			   {"B","K","R"," "," "," "," "," "," "}};				
	}
	
	@Test
	public void deep_copy() {
		assertTrue(Arrays.deepEquals(board, Copier.deepCopy(board)));
	}
	
	@Test(expected = NullPointerException.class)
	public void empty_copy() {		
		String[][] board = null;		
		assertNull(Matrix.makeKey(board));
	}
	
	@AfterClass
	public static void clear_memory() {
		board = null;
	}

}
