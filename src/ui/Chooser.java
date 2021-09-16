package ui;

import javax.swing.*;

import control.Filter;

public class Chooser {

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
					frame.setVisible(false);
				}
				else if (result == JFileChooser.CANCEL_OPTION) {
					Gui.output.setText("No file selected");
					frame.setVisible(false);
				}
	}
	
}
