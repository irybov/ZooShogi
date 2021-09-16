package ui;

import javax.swing.*;

//import control.Director;
import control.Driver;
import control.Filter;

public class Chooser {
	
//	private Director director = Director.getInstance();
	Driver driver = new Driver();

	JFileChooser input;
	
	public Chooser(){
		
		input = new JFileChooser();
		input.setFileFilter(new Filter());
		
		JFrame frame = new JFrame("Explorer");
		frame.setSize(800, 500);
		frame.setResizable(false);
		frame.pack();
	    frame.setLocationRelativeTo(null);
		frame.add(input);
		frame.setVisible(true);
				
				int result = input.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					Gui.output.setText("Selected file is: " + input.getSelectedFile().getName());
//					director.setBoard(driver.input());
//					driver.output(director.getBoard());
					Gui.doClick();
					frame.setVisible(false);
				}
				else if (result == JFileChooser.CANCEL_OPTION) {
					Gui.output.setText("No file selected");
					frame.setVisible(false);
				}
	}
	
}
