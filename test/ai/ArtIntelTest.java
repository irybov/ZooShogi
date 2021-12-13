package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ai.ArtIntel;
import ai.Node;
import utilpack.Copier;

@Ignore
public class ArtIntelTest {

	private static String[][] board;
	private static List<Node> nodes;
	private static List<Node> legal;
	private static int level;
	private static Generator generator;
	private static int cores;
	private static ExecutorService es;
	
	@BeforeClass
	public static void initBoard() {
		board = new String[][]{{" "," ","k","b","p","r"," "," "," "},
				  			   {" ","r"," "},
				  			   {"K"," "," "},
				  			   {" "," "," ","B","P","R"," "," "," "}};
		generator = new Generator();
		legal = generator.generateMoves(board, "black");
		nodes = new ArrayList<>(legal.size());
		nodes = generator.sortMoves(board, legal, "black", false);
		cores = Runtime.getRuntime().availableProcessors();
		es = Executors.newFixedThreadPool(cores);
	}

//	@Ignore
	@Test(timeout = 25000)
	public void performanceLimitAB() {
		level = 6;
		nodes.forEach(node-> es.submit(new ArtIntel(node, Copier.deepCopy(board), level)));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
//	@Ignore
	@Test(timeout = 45000)
	public void performanceLimitEX() {
		level = 7;
		nodes.forEach(node-> es.submit(new ArtIntel(node, Copier.deepCopy(board), level)));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
	@AfterClass
	public static void clearMemory() {
		board = null;
		nodes = null;
		legal = null;
		generator = null;
		es = null;
	}
	
}
