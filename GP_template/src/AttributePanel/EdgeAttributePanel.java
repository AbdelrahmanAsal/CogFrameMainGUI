package AttributePanel;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import All.Constants;
import Data.Edge;

public class EdgeAttributePanel extends JPanel {
	public JLabel linkDelayLabel, lossRatioLabel, throughputLabel, linkDelay, lossRatio, throughput, packetCountLabel, packetCount;

	public EdgeAttributePanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Edge Attributes Panel"));
		
		linkDelayLabel = new JLabel("Link Delay");
		lossRatioLabel = new JLabel("Loss Ratio");
		throughputLabel = new JLabel("Throughput");
		packetCountLabel = new JLabel("Packet Count");

		linkDelay = new JLabel();
		lossRatio = new JLabel();
		throughput = new JLabel();
		packetCount = new JLabel();
		
		JPanel all = new JPanel();
		
		GroupLayout layout = new GroupLayout(all);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
		
		all.setLayout(layout);
		add(all);
		
		Component gap = Box.createRigidArea(new Dimension(Constants.COMPONENTS_GAP, Constants.COMPONENTS_GAP));
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(linkDelayLabel).addComponent(lossRatioLabel).addComponent(throughputLabel).addComponent(packetCountLabel))
				.addGroup(layout.createParallelGroup().addComponent(linkDelay).addComponent(lossRatio).addComponent(throughput).addComponent(packetCount))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(linkDelayLabel).addComponent(linkDelay))
				.addGroup(layout.createParallelGroup().addComponent(lossRatioLabel).addComponent(lossRatio))
				.addGroup(layout.createParallelGroup().addComponent(throughputLabel).addComponent(throughput))
				.addGroup(layout.createParallelGroup().addComponent(packetCountLabel).addComponent(packetCount))
		);
	}

	public void setInfo(Edge edge) {
		linkDelay.setText(edge.linkDelay + "");
		lossRatio.setText(edge.lossRatio + "");
		throughput.setText(edge.throughput + "");
		packetCount.setText(edge.packetCount + "");
	}
}
