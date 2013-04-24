package AttributePanel;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import All.DrawingPanel;


public class VisualizeAttributePanel extends JPanel{
	public ButtonGroup visualiztionOptions;
	public ArrayList<JRadioButton> buttons;
	public VisualizeAttributePanel(){
		buttons = new ArrayList<JRadioButton>();
		setBorder(BorderFactory.createTitledBorder("Visualization Attributes Panel"));
		
		JRadioButton lossRatio = new JRadioButton("Loss Ratio");
		lossRatio.setActionCommand("LossRatio");
		buttons.add(lossRatio);
		JRadioButton throughput = new JRadioButton("Throughput");
		throughput.setActionCommand("Throughput");
		buttons.add(throughput);
		JRadioButton nodalDelay = new JRadioButton("Nodal Delay");
		nodalDelay.setActionCommand("NodalDelay");
		buttons.add(nodalDelay);
		JRadioButton linkDelay = new JRadioButton("Link Delay");
		linkDelay.setActionCommand("LinkDelay");
		buttons.add(linkDelay);
		JRadioButton wirelessInterfaces = new JRadioButton("Wireless Interfaces");
		wirelessInterfaces.setActionCommand("WirelessInterfaces");
		buttons.add(wirelessInterfaces);
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		visualiztionOptions = new ButtonGroup();
		for(JRadioButton button: buttons)
			visualiztionOptions.add(button);
		
		
		lossRatio.setSelected(true);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(lossRatio)
					.addComponent(throughput)
					.addComponent(nodalDelay)
					.addComponent(linkDelay)
					.addComponent(wirelessInterfaces)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(lossRatio)
					.addComponent(throughput)
					.addComponent(nodalDelay)
					.addComponent(linkDelay)
					.addComponent(wirelessInterfaces)
		);
	}
}

