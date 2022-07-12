package ai;

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

	public AI createAI(final int level, Node root, char[][] cs) {
		
		AI ai = null;
		
		switch(level) {
		case 2:
			ai = new NaiveAI(root, cs);
//			ai = new DiceyAI(root, board);
			break;
		case 3:
			ai = new TrickyAI(root, cs);
			break;
		case 4:
			ai = new ActiveAI(root, cs);
			break;	
		case 5:
			ai = new CleverAI(root, cs);
			break;
		case 6:
			ai = new ExpertAI(root, cs);
			break;
		case 7:
			ai = new MasterAI(root, cs);
			break;	
		}
		return ai;
	}
	
}
