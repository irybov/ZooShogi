package launch;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.plaf.nimbus.*;

import ui.Gui;

public class ZooShogi {
		
	private static int k;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		if((int) ss.getWidth()/1000 < 3000) {k = 1;}
		else {k = 2;}		
		setup();
	}

	private static void setup(){
		//Frame and content panel
		final JFrame launchScreen = new JFrame();	
		JPanel content = new JPanel();
		//Buttons
		JButton newGame = new JButton("START");
		JButton settings = new JButton("STYLE");
		JButton about = new JButton("ABOUT");
		//Setup frame
		launchScreen.setSize(300*k,300*k);
		launchScreen.setTitle("Zoo Shogi");
		//Set content panel to layout and add buttons
		content.setLayout(new GridLayout(3,1));
		content.add(newGame);
		content.add(settings);
		content.add(about);
		newGame.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		settings.setFont(new Font("Dialog", Font.PLAIN, 25*k));
		about.setFont(new Font("Dialog", Font.PLAIN, 25*k));
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
						 System.getProperty("file.separator") + "exp").mkdir();
				
				 javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new Gui(k).launch();
							launchScreen.dispose();
						}
					});
				Thread.currentThread().interrupt();
			}
		});

		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//changes GUI style 
				LookAndFeel laf = new NimbusLookAndFeel();
				try {
					UIManager.setLookAndFeel(laf);
				}
				catch (UnsupportedLookAndFeelException exc) {
					exc.printStackTrace();
				}
		
			}
		});

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//shows information
				JOptionPane.showMessageDialog(launchScreen,
					"Zoo Shogi by Ivan Ryabov\n" +
					"This program is free for non-commercial use only\n" +						
					"E-mail: v_cho@list.ru",
						"About",
							JOptionPane.DEFAULT_OPTION);
			}
		});

		launchScreen.revalidate();
		launchScreen.repaint();
	}
}
