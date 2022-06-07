package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ai.component.Generator;
import ai.component.Node;
import utilpack.Copier;
import utilpack.Turn;

public class ArtIntelTest {

	private static String[][] board;
	private static List<Node> nodes;
	private static List<Node> legal;
	private static int level;
	private static Generator generator;
	private static int cores;
	private static ExecutorService es1;
	private static ExecutorService es2;
	private static ArtIntelFactory factory;
	
	@BeforeClass
	public static void initBoard() {
		board = new String[][]{{" ","k"," "," ","p"," "," "," "," "},
				  			   {" ","r"," "},
				  			   {" ","b"," "},
				  			   {" ","K"," ","B","P","R"," "," "," "}};
		generator = new Generator();
		legal = generator.generateMoves(board, Turn.BLACK);
		cores = Runtime.getRuntime().availableProcessors();
		nodes = new ArrayList<>(legal.size());
		nodes = generator.sortMoves(board, legal, Turn.BLACK, false);
		factory = new ArtIntelFactory();
	}

	@Test(timeout = 8000)
	public void performanceLimitAB() {
		es1 = Executors.newFixedThreadPool(cores);
		level = 6;
		nodes.forEach(node-> es1.submit(factory.createAI(level, node, Copier.deepCopy(board))));
		es1.shutdown();
		try {
			es1.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
	@Test(timeout = 10000)
	public void performanceLimitEX() {
		es2 = Executors.newFixedThreadPool(cores);
		level = 7;
		nodes.forEach(node-> es2.submit(factory.createAI(level, node, Copier.deepCopy(board))));
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
		factory = null;
	}
	
}
