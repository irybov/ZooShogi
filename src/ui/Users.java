package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import control.Director;
import data.Player;

public class Users extends JFrame {
	
	private Director director = Director.getInstance();
	private List<Player> players = director.getList();
	private String username;
	private String password;
	private JLabel label = new JLabel(" ");

    public Users()
    {    	
    	String[] data = new String[players.size()];
		for(int i = 0; i<data.length; i++) {
				data[i] = players.get(i).getName();
			}
        // �������� ������
        JPanel contents = new JPanel();
        contents.setLayout(new BorderLayout());
        // �������� ������
        JButton select = new JButton("Select");
        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ���������� ���� ����� ������ : ��������, ��������� � ����
                // ������� �����, ��� ����������� ���� (������)
            	if(data.length > 0) {
                password = JOptionPane.showInputDialog(select, "Please enter your pasword: ",
                									"Authorization", 
                                                JOptionPane.DEFAULT_OPTION); 
            	if(director.selectPlayer(username, password)) {
        			Gui.profile.setText("Player: " + username);
                dispose();
            	}
            	else {
                    JOptionPane.showMessageDialog(select,
                            "Wrong password!",
                            "Fail",
                            JOptionPane.ERROR_MESSAGE);
            		}
            	}
            }
        });
        
        JList<String> list = new JList<String>(data);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(
                new ListSelectionListener() {
                     public void valueChanged(ListSelectionEvent e) {
                          username = list.getSelectedValue();
                          label.setText(username);
                     }
                });
        
        // ���������� ����������� � ������
        contents.add(select, BorderLayout.WEST);
        contents.add(new JScrollPane(list), BorderLayout.CENTER);
        contents.add(label, BorderLayout.SOUTH);
        setContentPane(contents);
        // ����� ����
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
