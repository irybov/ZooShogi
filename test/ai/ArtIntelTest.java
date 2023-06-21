package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ai.component.Generator;
import ai.component.Node;
import utilpack.Copier;
import utilpack.Turn;

@Ignore
public class ArtIntelTest {

	private static String[][] board;
	private static List<Node> nodes;
	private static List<Node> legal;
	private static int level;
	private static Generator generator;
	private static AIFactory factory;
	private static int cores;
	private ExecutorService es;
	
	@BeforeClass
	public static void init_board_and_env() {
		board = new String[][]{{" ","k"," "," ","p"," "," "," "," "},
				  			   {" ","r"," "},
				  			   {" ","b"," "},
				  			   {" ","K"," ","B","P","R"," "," "," "}};
		generator = new Generator();
		legal = generator.generateMoves(board, Turn.BLACK);
		cores = Runtime.getRuntime().availableProcessors();
		factory = new AIFactory();
	}
	
	@Before
	public void set_up() {
		es = Executors.newFixedThreadPool(cores);
		nodes = new ArrayList<>(legal.size());
		nodes = generator.sortMoves(board, legal, Turn.BLACK);
	}

	@Test(timeout = 8_000)
	public void performance_timelimit_AB() {
		level = 6;
		nodes.forEach(node-> es.submit(factory.createAI(level, node, Copier.deepCopy(board))));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
	@Test(timeout = 10_000)
	public void performance_timelimit_EX() {
		level = 7;
		nodes.forEach(node-> es.submit(factory.createAI(level, node, Copier.deepCopy(board))));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	
	@After
	public void tear_down() {
		es = null;
		nodes = null;
	}
	
	@AfterClass
	public static void clear_memory() {
		board = null;
		legal = null;
		generator = null;
		factory = null;
	}
	
}
