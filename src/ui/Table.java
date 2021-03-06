package ui;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import control.Director;
import data.PlayerInfo;

class Table {
	
	// Create a couple of columns 								
	private Director director = Director.getInstance();
	private List<PlayerInfo> players = director.getPlayers();
	private final String[] columns = {"Name", "Joined", "Score"};
	
	public Table() {
		// Create a couple of columns 										
		DefaultTableModel model = new DefaultTableModel(columns, players.size()) {

		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		model.setRowCount(0);
		JTable table = new JTable(model);
		
	    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)
	    table.getDefaultRenderer(String.class);
	    renderer.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    Collections.sort(players, Collections.reverseOrder());

			for(PlayerInfo player: players) {
				// Append a row 
				model.addRow(new Object[]{player.getName(), player.getRegistrationDate(),
						Integer.toString(player.getScore())});
				}
				table.setRowHeight(table.getRowHeight() + 20);
				table.setFont(new Font("Dialog", Font.PLAIN, 20));
				JTableHeader th = table.getTableHeader();
				th.setFont(new Font("Dialog", Font.PLAIN, 25));
			    JFrame popup = new JFrame("Scoresheet");
			    popup.setResizable(false);
			    //Add in whatever components you want
			    popup.add(new JScrollPane(table));							    
			    popup.pack();
			    popup.setLocationRelativeTo(null);
			    popup.setVisible(true);
	}

}
