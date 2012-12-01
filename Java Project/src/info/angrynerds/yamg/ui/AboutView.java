package info.angrynerds.yamg.ui;

import java.awt.*;
import java.awt.event.*;

import info.angrynerds.yamg.*;
import info.angrynerds.yamg.utils.*;

import javax.swing.*;

public class AboutView {
	private JFrame frame;
	private JPanel mainPanel;
	
	private void addRow(String first, String second) {
		mainPanel.add(new JLabel(first));
		mainPanel.add(new JLabel(second));
	}
	
	public void setVisible(boolean visible) {
		if(frame == null) {
			frame = new JFrame("About YAMG");
			mainPanel = new JPanel(new GridLayout(18, 2));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			addRow("Game Version", Yamg.VERSION);
			addRow("Lead Programmer", "Daniel Glus");
			addRow("Assistant Programmer", "John Lhota");
			addRow("Game Designer", "Daniel Glus");
			addRow("Playtesters", "John Lhota");
			for(String tester:new String[] {"Brent Morden",
					"Alexander Chan", "Miles Shebar", "Jennifer Lee", "Andrew Glus",
					"Vaughan McDonald", "Elliot Kahn", "Richard Zheng"}) {// rows 5 to 10
				addRow("", tester);
			}
			addRow("Bug Finders", "Alexander Chan");
			for(String finder:new String[] {"Johnston Jiaa", "Richard Zheng",
					"Jennifer Lee", "John Lhota"}) {
				addRow("", finder);
			}

			JPanel bottom = new JPanel();
				JButton doneButton = new JButton("Done");
				doneButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						frame.setVisible(false);
					}
				});
			bottom.add(doneButton);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(bottom, BorderLayout.SOUTH);
			frame.setBounds(Helper.getCenteredBounds(500, 500));
		}
		frame.setVisible(visible);
	}
}