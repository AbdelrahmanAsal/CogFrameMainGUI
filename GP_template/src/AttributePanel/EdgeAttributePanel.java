package AttributePanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import All.Constants;
import All.DrawingPanel;
import Data.Edge;

public class EdgeAttributePanel extends JPanel {
	public JLabel linkDelayLabel, lossRatioLabel, throughputLabel, linkDelay, lossRatio, throughput, packetCountLabel, packetCount;
	public JLabel snrLabel, linkQualityLabel, dataRateLabel;
	public JTextField snr, linkQuality, dataRate;
	public JButton setData;
	public Edge selectedEdge;
	public DrawingPanel drawingPanel;
	public EdgeAttributePanel(DrawingPanel dp) {
		drawingPanel = dp;
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Edge Attributes Panel"));
		
		linkDelayLabel = new JLabel("Link Delay");
		lossRatioLabel = new JLabel("Loss Ratio");
		throughputLabel = new JLabel("Throughput");
		packetCountLabel = new JLabel("Packet Count");
		snrLabel = new JLabel("SNR");
		linkQualityLabel = new JLabel("Link Quality");
		dataRateLabel = new JLabel("Data Rate");

		linkDelay = new JLabel();
		lossRatio = new JLabel();
		throughput = new JLabel();
		packetCount = new JLabel();
		snr = new JTextField();
		linkQuality = new JTextField();
		dataRate = new JTextField();
		
		setData = new JButton("Set Data");
		setData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedEdge != null){
					selectedEdge.snr = Double.parseDouble(snr.getText());
					selectedEdge.linkQuality = Double.parseDouble(linkQuality.getText());
					selectedEdge.dataRate = Double.parseDouble(dataRate.getText());
					
					drawingPanel.repaint();

					System.out.println("Successfully updated the edge:\n" + selectedEdge);
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
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup().addComponent(snrLabel).addComponent(linkQualityLabel).addComponent(dataRateLabel).addComponent(linkDelayLabel).addComponent(lossRatioLabel)/*.addComponent(throughputLabel)*/.addComponent(packetCountLabel).addComponent(setData))
			.addGroup(layout.createParallelGroup().addComponent(snr).addComponent(linkQuality).addComponent(dataRate).addComponent(linkDelay).addComponent(lossRatio)/*.addComponent(throughput)*/.addComponent(packetCount))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(snrLabel).addComponent(snr))
				.addGroup(layout.createParallelGroup().addComponent(linkQualityLabel).addComponent(linkQuality))
				.addGroup(layout.createParallelGroup().addComponent(dataRateLabel).addComponent(dataRate))
				
				.addGroup(layout.createParallelGroup().addComponent(linkDelayLabel).addComponent(linkDelay))
				.addGroup(layout.createParallelGroup().addComponent(lossRatioLabel).addComponent(lossRatio))
//				.addGroup(layout.createParallelGroup().addComponent(throughputLabel).addComponent(throughput))
				.addGroup(layout.createParallelGroup().addComponent(packetCountLabel).addComponent(packetCount))
				.addComponent(setData)
				
		);
	}

	public void setInfo(Edge edge) {
		selectedEdge = edge;
		
		snr.setText(edge.snr + "");
		linkQuality.setText(edge.linkQuality + "");
		dataRate.setText(edge.dataRate + "");
		
		String linkDelayStr = String.format("%.3f\n", edge.linkDelay);
		linkDelay.setText(linkDelayStr + " ms");
		String lossRatioStr = String.format("%.3f\n", edge.lossRatio * 100);
		lossRatio.setText(lossRatioStr + " %");
		//EOF
//		String throughputStr = String.format("%.3f\n", edge.throughput);
//		throughput.setText(throughputStr + " pps"); // packets per second
		String packetCountStr = String.format("%d\n", edge.packetCount);
		packetCount.setText(packetCountStr + " packets");
	}
}
