package AttributePanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;

import All.Constants;
import All.DrawingPanel;
import Data.Machine;
import Data.Primary;
import Distributions.ExponentialDistribution;
import Distributions.ProbabilityDistribution;
import Distributions.UniformDistribution;


public class PrimaryAttributePanel extends NodeAttributesPanel{
	public ProbabilityDistribution tempActiveDist, tempInactiveDist;
	public JLabel activeDistLabel, inactiveDistLabel, selectedChannelLabel;
	public JComboBox activeDist, inactiveDist;
	public JButton editActiveDist, editInactiveDist;
	public JTextArea activeSeed, inactiveSeed;
	public JTextArea selectedChannel;
	public Primary selectedNode;
	public JPanel distPanel;
	public DrawingPanel drawingPanel;
	public PrimaryAttributePanel(DrawingPanel dp){
		this.drawingPanel = dp;
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Primary Attributes Panel"));

		activeDistLabel = new JLabel("Active Distribution");
		activeDist = new JComboBox(new String[]{"Uniform", "Exponential"});
		activeDist.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String s = (String) e.getItem();
				if(e.getStateChange() == e.SELECTED){
					if(s.equals("Uniform")){
						tempActiveDist = new UniformDistribution();
					}else if(s.equals("Exponential")){
						tempActiveDist = new ExponentialDistribution();
					}else{
						System.out.println("Can't found distribution handler");
					}
				}
			}
		});
		editActiveDist = new JButton("Edit");
		editActiveDist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tempActiveDist.showPanel();
			}
		});

		inactiveDistLabel = new JLabel("Inactive Distribution");
		inactiveDist = new JComboBox(new String[]{"Uniform", "Exponential"});
		inactiveDist.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String s = (String) e.getItem();
				if(e.getStateChange() == e.SELECTED){
					if(s.equals("Uniform")){
						tempInactiveDist = new UniformDistribution();
					}else if(s.equals("Exponential")){
						tempInactiveDist = new ExponentialDistribution();
					}else{
						System.out.println("Can't found distribution handler");
					}
				}
			}
		});
		editInactiveDist = new JButton("Edit");
		editInactiveDist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tempInactiveDist.showPanel();
			}
		});

		selectedChannelLabel = new JLabel("Selected Channel");
		selectedChannel = new JTextArea();
		selectedChannel.setLineWrap(true);
		selectedChannel.setBorder(BorderFactory.createLoweredBevelBorder());

		setData = new JButton("Set Data");
		setData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null){
					selectedNode.name = name.getText();

					selectedNode.ETH_IP = ETH_IP.getText();
					selectedNode.ETH_HW = ETH_HW.getText();
					selectedNode.WLS_IP = parseSeparatedString(WLS_IP.getText());
//					selectedNode.WLS_HW = parseSeparatedString(WLS_HW.getText());

					selectedNode.channels = parseChannels(channels.getText());

					selectedNode.mobilityOption = mobilityOption.getSelectedItem().toString();
					selectedNode.topologyOption = topologyOption.getSelectedItem().toString();

					selectedNode.activeDist = tempActiveDist;
					selectedNode.inactiveDist = tempInactiveDist;
					
					drawingPanel.repaint();

					System.out.println("Successfully updated the node:\n" + selectedNode);
				}
			}
		});

		JPanel all = new JPanel();

		GroupLayout layout = new GroupLayout(all);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

		all.setLayout(layout);
		add(all);

		Component gap = Box.createRigidArea(new Dimension(Constants.COMPONENTS_GAP, Constants.COMPONENTS_GAP));

		layout.setHorizontalGroup(
		   layout.createParallelGroup()
		   		  .addGroup(layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    	   .addComponent(nameLabel)
				      	   .addComponent(ETH_IPLabel)
				      	   .addComponent(ETH_HWLabel)
				      	   .addComponent(WLS_IPLabel)
				      	   .addComponent(WLS_HWLabel)
				      	   .addComponent(channelsLabel)
				      	   .addComponent(mobilityOptionLabel)
				      	   .addComponent(topologyOptionLabel)
				      	   .addComponent(gap)
				           .addComponent(activeDistLabel)
				           .addComponent(inactiveDistLabel)
				           .addComponent(selectedChannelLabel))
			          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        		   .addComponent(name)
				      	   .addComponent(ETH_IP)
				      	   .addComponent(ETH_HW)
				      	   .addComponent(WLS_IP)
				      	   .addComponent(ps)
				      	   .addComponent(channels)
				      	   .addComponent(mobilityOption)
				      	   .addComponent(topologyOption)
				      	   .addComponent(gap)
				      	   .addGroup(layout.createSequentialGroup()
				      	   		.addComponent(activeDist)
				      	   		.addComponent(editActiveDist))
			      	   	   .addGroup(layout.createSequentialGroup()
			      	   			.addComponent(inactiveDist)
			      	   			.addComponent(editInactiveDist))
			      	   	   .addComponent(selectedChannel)
			      	   	   .addComponent(setData))
		   			  )
		);

		layout.setVerticalGroup(
		   layout.createSequentialGroup()
		   	  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(nameLabel)
		           .addComponent(name))
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(ETH_IPLabel)
		           .addComponent(ETH_IP))
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(ETH_HWLabel)
		           .addComponent(ETH_HW))
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(WLS_IPLabel)
		           .addComponent(WLS_IP)
		           )
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(WLS_HWLabel)
		           .addComponent(ps)
		           )
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(channelsLabel)
		           .addComponent(channels))
	          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(mobilityOptionLabel)
		           .addComponent(mobilityOption))
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(topologyOptionLabel)
		           .addComponent(topologyOption))
		      .addComponent(gap)
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    	   .addComponent(activeDistLabel)
		    	   .addComponent(activeDist)
		           .addComponent(editActiveDist))
		       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    	   .addComponent(inactiveDistLabel)
		           .addComponent(inactiveDist)
		           .addComponent(editInactiveDist))
		       .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    	   .addComponent(selectedChannelLabel)
		    	   .addComponent(selectedChannel))
		       .addComponent(setData)
		);
	}

	public void setInfo(Primary node){
		selectedNode = node;

		//Fill the required information.
		name.setText(node.name);

		ETH_IP.setText(node.ETH_IP);
		ETH_HW.setText(node.ETH_HW);
		WLS_IP.setText(node.getWLS_IP());
		WirelessInterfacesTable t = (WirelessInterfacesTable)WLS_HW_Table.getModel();
		t.current.clear();
		for(int i = 0; i < node.WLS_HW.size(); i++){
			t.current.add(new WirelessTableRowEntry(node.WLS_Name.get(i), node.WLS_HW.get(i)));
			WLS_HW_Table.getTableHeader().repaint();
			WLS_HW_Table.tableChanged(new TableModelEvent(t));
			WLS_HW_Table.repaint();
		}

		channels.setText(node.getChannels());

		mobilityOption.setSelectedItem(node.mobilityOption);
		topologyOption.setSelectedItem(node.topologyOption);

		activeDist.setSelectedItem(((Primary)node).activeDist.type());
		inactiveDist.setSelectedItem(((Primary)node).inactiveDist.type());

		//Delayed Setting. NEED TO BE CLONED.
		tempActiveDist = (node).activeDist;
		tempInactiveDist = (node).inactiveDist;
	}
}

