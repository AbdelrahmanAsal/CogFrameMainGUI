package AttributePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import All.DrawingPanel;
import Data.Node;
import Data.PacketInfo;

public class ChartsPanel extends JPanel {
	JComboBox chartsOptions;
	public Dimension panelDimension;
	DrawingPanel drawingPanel;
	JComboBox machineBox;
    Box box;
    ChartPanel currChartPanel;
    
	public ChartsPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;

		String[] charts = new String[] { "",
				"Primary user behaviour active distribution",
				"Primary user behaviour inactive distribution",
				"CDF for interfaces switching time", "Loss ratio",
				"Pakcet delay" };
		chartsOptions = new JComboBox(charts);
		chartsOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = chartsOptions.getSelectedIndex();
				drawChart(index);
				System.out.println("-> ->"+ index);
			}
		});
		currChartPanel = null;
		machineBox = new JComboBox();
		machineBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = chartsOptions.getSelectedIndex();
				drawChart(index);
				System.out.println("-> ->"+ index);
			}
		});
		box = Box.createVerticalBox();
		box.add(chartsOptions);
		box.add(machineBox);
		add(box);
	}

	public void drawChart(int index) {
		JFreeChart chart = null;
		if (index == 0) {
			// null chart
			machineBox.setModel(new JComboBox().getModel());
			return;
		} else if (index == 1) {
			String[] primaryMachines = new String[drawingPanel.sc.primaryNames.size()];
			for (int i = 0; i < primaryMachines.length; i++) {
				primaryMachines[i] = drawingPanel.sc.primaryNames.get(i);
			}
			machineBox.setModel(new JComboBox(primaryMachines).getModel());
			
			// PU behaviour
			chart = getProbabilityDistributionChart(drawingPanel.sc.primaryActiveDist.get(machineBox.getSelectedIndex()), "active");
			
		}  else if (index == 2) {
			// PU behaviour inactive
			String[] primaryMachines = new String[drawingPanel.sc.primaryNames.size()];
			for (int i = 0; i < primaryMachines.length; i++) {
				primaryMachines[i] = drawingPanel.sc.primaryNames.get(i);
			}
			machineBox.setModel(new JComboBox(primaryMachines).getModel());
			
			chart = getProbabilityDistributionChart(drawingPanel.sc.primaryInactiveDist.get(machineBox.getSelectedIndex()), "inactive");
		} else if (index == 3) {
			// CDF switching
			machineBox.setModel(new JComboBox().getModel());
			chart  = getSwitchingTimeChart();
		} else if (index == 4) {
			// loss ratio
			machineBox.setModel(new JComboBox().getModel());
			chart = getLossRatioChart();
		} else if (index == 5) {
			// packet delay
			machineBox.setModel(new JComboBox().getModel());
			chart = getPacketDelayChart();
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(panelDimension.width,panelDimension.height-20));
		if(currChartPanel != null)
			box.remove(currChartPanel);
		currChartPanel = chartPanel;
		box.add(chartPanel);
	}
	
	public JFreeChart getProbabilityDistributionChart(ArrayList<Double> data, String type) {
		Collections.sort(data);
		XYSeries dataset = new XYSeries("Primary user inactive behaviour");
		dataset.add(0, 0);
		double x = 1;
		for (Double t : data) {
			dataset.add(1.0 * t, x / data.size());
			x++;
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(dataset);
		JFreeChart chart = ChartFactory.createXYLineChart("Primary user "+type+" behaviour", "Time",
				"CDF", xyseriescollection, PlotOrientation.VERTICAL, false,
				true, false);
		return chart;
	}
	
	public JFreeChart getSwitchingTimeChart() {
		ArrayList<Long> switchingTime = drawingPanel.sc.switchingTimeSet;
		Collections.sort(switchingTime);
		XYSeries dataset = new XYSeries("CDF of switching time");
		dataset.add(0, 0);
		double x = 1;
		for (Long t : switchingTime) {
			dataset.add(1.0 * t, x / switchingTime.size());
			x++;
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(dataset);
		JFreeChart chart = ChartFactory.createXYLineChart("CDF of switching time", "Time",
				"CDF", xyseriescollection, PlotOrientation.VERTICAL, false,
				true, false);
		return chart;
	}
	
	public JFreeChart getLossRatioChart() { 
		XYSeries dataset = new XYSeries("Packets Loss Ratio");
		Node dest = drawingPanel.sc.dest;
		Node src = drawingPanel.sc.src;
		long minTime = drawingPanel.sc.minTimestamp;
		for(int packetID : dest.inPackets.keySet()){
			PacketInfo info = dest.inPackets.get(packetID);
			long time = info.timeStamp;
			int sent = 0, rec = 0;
			System.out.println(time);
			for(int sentPacketID: src.outPackets.keySet()){
				PacketInfo sentInfo = src.outPackets.get(sentPacketID);
				long timeSent = sentInfo.timeStamp;
				if(timeSent <= time)
					sent++;
			}
			for(int receivedPacketID: dest.inPackets.keySet()){
				PacketInfo receivedInfo = dest.inPackets.get(receivedPacketID);
				long timeRec = receivedInfo.timeStamp;
				if(timeRec <= time)
					rec++;
			}
			System.out.println(sent);
			System.out.println(rec);
			dataset.add(time - minTime, 1 - (1.0 * rec / sent));
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(dataset);
		JFreeChart chart = ChartFactory.createXYLineChart("Packets Loss Ratio", "Time",
				"Loss Ratio", xyseriescollection, PlotOrientation.VERTICAL, false,
				true, false);
		return chart;
	}
	
	public JFreeChart getPacketDelayChart() { 
		XYSeries dataset = new XYSeries("Packet Delay");
		Node dest = drawingPanel.sc.dest;
		Node src = drawingPanel.sc.src;
		long minTime = drawingPanel.sc.minTimestamp;
		for(int packetID : dest.inPackets.keySet()){
			PacketInfo info = dest.inPackets.get(packetID);
			long time = info.timeStamp;
			double totalDelay = 0, totalCount = 0;
			for(int sentPacketID: src.outPackets.keySet()){
				PacketInfo sentInfo = src.outPackets.get(sentPacketID);
				long timeSent = sentInfo.timeStamp;
				if(timeSent <= time) {
					if (dest.inPackets.containsKey(sentPacketID)) {
						PacketInfo receivedInfo = dest.inPackets.get(sentPacketID);
						if(receivedInfo.timeStamp <= time) {
							totalDelay += (receivedInfo.timeStamp - timeSent);
							totalCount ++;
						}
					}
				}
			}
			dataset.add(time - minTime, totalDelay / totalCount);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(dataset);
		JFreeChart chart = ChartFactory.createXYLineChart("Packet Delay", "Time",
				"Packet Delay", xyseriescollection, PlotOrientation.VERTICAL, false,
				true, false);
		return chart;
	}
	
}
