package util;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilpack.Matrix;

public class MatrixTest {

	private static String[][] board;
	
	@BeforeClass
	public static void initBoard() {
		board = new String[][]{{"r","k","b"," "," "," "," "," "," "},
				  			   {" ","p"," "},
				  			   {" ","P"," "},
				  			   {"B","K","R"," "," "," "," "," "," "}};				
	}
	
	@Test
	public void testKeyMakerCreate() {		
		assertNotNull(Matrix.keyMaker(board));
	}

	@Test(expected = NullPointerException.class)
	public void testKeyMakerEmpty() {		
		String[][] board = null;		
		assertNull(Matrix.keyMaker(board));
	}
	
	@Test
	public void testKeyMakerLength() {
		assertEquals(24, Matrix.keyMaker(board).length());
	}
	
	@AfterClass
	public static void clearMemory() {
		board = null;
	}
	
}
