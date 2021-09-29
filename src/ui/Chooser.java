package ui;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import javax.swing.*;

import control.Director;
import control.Filter;

public class Chooser {
	
	private Director director = Director.getInstance();

	JFileChooser input;
	
	public Chooser(){
		
		input = new JFileChooser(new File(System.getProperty("user.dir")));
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
					
					String name = input.getSelectedFile().getName();
					
					if(director.checkClient()) {
						director.disconnect();
					}
					if(director.getServer() == null) {
						director.establish();
					}
					else {
						director.activate();
					}
					
					JarFile jarfile = null;
					File file = null;
					if(name.endsWith(".jar")) {
						try {
							jarfile = new JarFile(input.getSelectedFile().getAbsolutePath());
								Runtime.getRuntime().exec("javaw "+"-jar "+jarfile.getName()+" "+
								jarfile.getManifest().getMainAttributes().getValue("Main-Class"));
						}
						catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					else {
						try {
							file = new File(input.getSelectedFile().getAbsolutePath());
							Runtime.getRuntime().exec(file.getName());
						}
						catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					Gui.output.setText(name + " engine loaded");
					frame.setVisible(false);
				}
				
				else if (result == JFileChooser.CANCEL_OPTION) {
					Gui.output.setText("No engine selected");
					frame.setVisible(false);
				}
	}
	
}
