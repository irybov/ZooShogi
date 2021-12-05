package utilpack;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ai.ArtIntel;
import ai.Node;

@Ignore
public class ArtIntelTest {

	private static String[][] board;
	private static Node node;
	private static int level;
	
	@BeforeClass
	public static void initBoard() {
		board = new String[][]{{"r","k"," ","b","p","r","b","p"," "},
				  			   {" "," "," "},
				  			   {" "," "," "},
				  			   {" ","K"," "," "," "," "," "," "," "}};				
	}
	
	@BeforeClass
	public static void createNode() {
		node = new Node(0, 0, 1, 0, "black");
	}
	
	@Test(timeout = 5000)
	public void performanceLimitAB() {
		level = 6;
		new ArtIntel(node, Copier.deepCopy(board), level).run();
	}
	
	@Test(timeout = 5000)
	public void performanceLimitEX() {
		level = 7;
		new ArtIntel(node, Copier.deepCopy(board), level).run();
	}
	
	@AfterClass
	public static void clearMemory() {
		board = null;
		node = null;
	}
	
}
