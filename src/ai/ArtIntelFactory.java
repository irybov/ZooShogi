package ai;

import ai.component.Node;
import ai.type.ActiveAI;
import ai.type.ArtIntel;
import ai.type.CleverAI;
import ai.type.ExpertAI;
import ai.type.MasterAI;
import ai.type.NaiveAI;
import ai.type.TrickyAI;

public class ArtIntelFactory {

	public ArtIntel createAI(final int level, Node root, String[][] board) {
		
		ArtIntel ai = null;
		
		switch(level) {
		case 2:
			ai = new NaiveAI(root, board);
			break;
		case 3:
			ai = new TrickyAI(root, board);
			break;
		case 4:
			ai = new ActiveAI(root, board);
			break;	
		case 5:
			ai = new CleverAI(root, board);
			break;
		case 6:
			ai = new ExpertAI(root, board);
			break;
		case 7:
			ai = new MasterAI(root, board);
			break;	
		}
		return ai;
	}
	
}
