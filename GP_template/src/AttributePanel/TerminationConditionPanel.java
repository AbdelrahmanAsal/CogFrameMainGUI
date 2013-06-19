package AttributePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import All.UI;

public class TerminationConditionPanel extends JFrame{

	
	String[] terminationConditions = { "Time", "Number of Packets"};
	JComboBox terminationConditionChoices;
	JTextField value;
	JButton set;
	public JPanel panel;
	UI _ui;
	
	public TerminationConditionPanel(UI ui) {
		_ui = ui;
		panel = new JPanel();
		terminationConditionChoices = new JComboBox(terminationConditions);
		value = new JTextField();
		set = new JButton("Set Condtion");
		set.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int terminationOption = terminationConditionChoices.getSelectedIndex();
				int val = new Integer(value.getText());
				System.out.println(terminationOption + " " + val);
				if(terminationOption == 0) {
					_ui.terminationOption = "time";
				} else {
					_ui.terminationOption = "numPackets";
				}
				_ui.terminationValue = val;
			}
		});
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

		panel.setLayout(layout);
		add(panel);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				    	   .addComponent(terminationConditionChoices)
				      	   .addComponent(value)
				      	   .addComponent(set)
		);

		layout.setVerticalGroup(
		   	  layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		          .addComponent(terminationConditionChoices)
				  .addComponent(value)
				  .addComponent(set)
		);
		setSize(500, 100);
		setTitle("Expirement Termination Condition");
	}
}
