package util;

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
	public static void initBoard() {
		board = new String[][]{{"r","k","b"," "," "," "," "," "," "},
				  			   {" ","p"," "},
				  			   {" ","P"," "},
				  			   {"B","K","R"," "," "," "," "," "," "}};				
	}
	
	@Test
	public void testDeepCopy() {
		assertTrue(Arrays.deepEquals(board, Copier.deepCopy(board)));
	}
	
	@Test(expected = NullPointerException.class)
	public void testEmptyCopy() {		
		String[][] board = null;		
		assertNull(Matrix.keyMaker(board));
	}
	
	@AfterClass
	public static void clearMemory() {
		board = null;
	}

}
