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

public class ArtIntelTest {

	private static String[][] board;
	private static List<Node> nodes;
	private static List<Node> legal;
	private static int level;
	private static Generator generator;
	private static int cores;
	private static ExecutorService es1;
	private static ExecutorService es2;
	
	@BeforeClass
	public static void initBoard() {
		board = new String[][]{{" "," ","k","b","p"," "," "," "," "},
				  			   {" ","r"," "},
				  			   {"K"," "," "},
				  			   {" "," "," ","B","P","R"," "," "," "}};
		generator = new Generator();
		legal = generator.generateMoves(board, "black");
		cores = Runtime.getRuntime().availableProcessors();
		nodes = new ArrayList<>(legal.size());
		nodes = generator.sortMoves(board, legal, "black", false);
	}

	@Test(timeout = 10000)
	public void performanceLimitAB() {
		es1 = Executors.newFixedThreadPool(cores);
		level = 6;
		nodes.forEach(node-> es1.submit(new ArtIntel(node, Copier.deepCopy(board), level)));
		es1.shutdown();
		try {
			es1.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
	@Test(timeout = 15000)
	public void performanceLimitEX() {
		es2 = Executors.newFixedThreadPool(cores);
		level = 7;
		nodes.forEach(node-> es2.submit(new ArtIntel(node, Copier.deepCopy(board), level)));
		es2.shutdown();
		try {
			es2.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
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
	}
	
}
