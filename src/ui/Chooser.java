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
					Gui.output.setText(name + " has been loaded");
					
					if(director.checkClient()) {
						director.disconnect();
					}
					if(director.getServer() == null) {
						director.establish();
					}
					else {
						director.activate(true);
					}
					
					JarFile jarfile = null;
					File file = null;
					if(name.endsWith(".jar")) {
						try {
							jarfile = new JarFile(input.getSelectedFile().getAbsolutePath());
								Runtime.getRuntime().exec("javaw " + "-jar " + name + " " + 
								jarfile.getManifest().getMainAttributes().getValue("Main-Class"));
						}
						catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					else {
						try {
							file = new File(input.getSelectedFile().getAbsolutePath());
							String fileName = file.getName();
							Runtime.getRuntime().exec(fileName);
						}
						catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					frame.setVisible(false);
				}
				
				else if (result == JFileChooser.CANCEL_OPTION) {
					Gui.output.setText("No engine selected");
					frame.setVisible(false);
				}
	}
	
}
