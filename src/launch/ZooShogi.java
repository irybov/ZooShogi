package launch;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import ui.Gui;

public class ZooShogi {

	public static void main(String[] args) {
		setup();
	}

	static void setup(){		
		//Frame and content panel
		final JFrame launchScreen = new JFrame();	
		JPanel content = new JPanel();
		//Buttons
		JButton newGame = new JButton("START");
		JButton settings = new JButton("SCREEN");
		JButton about = new JButton("ABOUT");
		//Setup frame
		launchScreen.setSize(300,300);
		launchScreen.setTitle("Zoo Shogi");
		//Set content panel to layout and add buttons
		content.setLayout(new GridLayout(3,1));
		content.add(newGame);
		content.add(settings);
		content.add(about);
		newGame.setFont(new Font("Dialog", Font.PLAIN, 25));
		settings.setFont(new Font("Dialog", Font.PLAIN, 25));
		about.setFont(new Font("Dialog", Font.PLAIN, 25));
		launchScreen.add(content);
		launchScreen.setLocationRelativeTo(null);
		launchScreen.setResizable(false);
		//allow the frame to be closed
		launchScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		launchScreen.setVisible(true);
		//setup button listeners to do what they say they do
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//starts main program
				new File(System.getProperty("user.dir") +
						 System.getProperty("file.separator") + "ui").mkdir();
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new Gui();
							launchScreen.dispose();
						}
					});
			}
		});
/*
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//screen settings
				JOptionPane.showMessageDialog(launchScreen,
					rulesMessage,
					"Resolution",
						JOptionPane.DEFAULT_OPTION);
			}
		});
*/
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//shows information
				JOptionPane.showMessageDialog(launchScreen,
					"Zoo Shogi by Ivan Ryabov\n" +
					"This program is free for private use only\n" +
					"E-mail: v_cho@list.ru",
						"About",
							JOptionPane.DEFAULT_OPTION);
			}
		});

		launchScreen.revalidate();
		launchScreen.repaint();
	}
}
