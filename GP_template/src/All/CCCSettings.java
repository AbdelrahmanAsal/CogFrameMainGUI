package All;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;


public class CCCSettings extends JFrame{
	UI ui;
	JRadioButton ethernet, wireless;
	JButton setSettings;
	ButtonGroup buttonGroup;
	public CCCSettings(UI uii){
		this.ui = uii;
		ethernet = new JRadioButton("Ethernet");
		ethernet.setActionCommand("Ethernet");
		wireless = new JRadioButton("Wireless");
		wireless.setActionCommand("Wireless");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(ethernet);
		buttonGroup.add(wireless);
		
		setLayout(new GridLayout(1, 2));
		add(ethernet);
		add(wireless);
		
		setSettings = new JButton("Set settings");
		setSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ui.ccc = buttonGroup.getSelection().getActionCommand();
				//Channel needs to be selected.
				
				
				dispose();
			}
		});
		add(setSettings);
		
		setSize(200, 115);
		setLocationByPlatform(true);
		setVisible(true);
		setLayout(new FlowLayout());
	}
}
