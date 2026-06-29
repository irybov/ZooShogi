package ai;

import ai.component.Generator;
import ai.component.Node;
import ai.type.ActiveAI;
import ai.type.AI;
import ai.type.CleverAI;
import ai.type.DiceyAI;
import ai.type.ExpertAI;
import ai.type.MasterAI;
import ai.type.NaiveAI;
import ai.type.TrickyAI;

public class AIFactory {

	public AI createAI(final int level, Node root, String[][] board, Generator generator) {
		
		AI ai = null;
		
		switch(level) {
		case 0:
			ai = new NaiveAI(root, board, generator);
			break;			
		case 2:
			ai = new DiceyAI(root, board, generator);
			break;
		case 3:
			ai = new TrickyAI(root, board, generator);
			break;
		case 4:
			ai = new ActiveAI(root, board, generator);
			break;	
		case 5:
			ai = new CleverAI(root, board, generator);
			break;
		case 6:
			ai = new ExpertAI(root, board, generator);
			break;
		case 7:
			ai = new MasterAI(root, board, generator);
			break;	
		}
		return ai;
	}
	
}
