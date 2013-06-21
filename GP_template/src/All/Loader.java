package All;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import Data.Edge;
import Data.Machine;
import Data.Node;
import Data.Primary;
import Data.StringPair;

public class Loader {

	DrawingPanel drawingPanel;

	public Loader(DrawingPanel dp) {
		drawingPanel = dp;
	}

	public void load() {

		try {
			drawingPanel.listOfNodes.clear();
			String fileName = JOptionPane.showInputDialog(null,
					"Enter the saved file name");
			InputReader r = new InputReader(new FileReader(fileName));
			HashMap<String, Node> map = new HashMap<String, Node>();
			ArrayList<StringPair> pairs = new ArrayList<StringPair>();
			HashMap<String, String> srcToDestName = new HashMap<String, String>();

			// Number Of Nodes
			int noOfNodes = r.nextInt();
			boolean isPrimary;
			for (int i = 0; i < noOfNodes; i++) {
				// Type
				String type = r.next();
				if (type.equalsIgnoreCase("primary")) {
					isPrimary = true;
				} else {
					isPrimary = false;
				}

				// Name, X, Y, ETH_IP, ETH_HW
				String nodeName = r.next();
				int nodeX = r.nextInt();
				int nodeY = r.nextInt();
				Node node;
				if (!isPrimary)
					node = new Machine(nodeName, nodeX, nodeY);
				else
					node = new Primary(nodeName, nodeX, nodeY);
				map.put(nodeName, node);
				String ip = r.next();
				node.ETH_IP = ip;
				String eth_hw = r.next();
				node.ETH_HW = eth_hw;

				// Mobility & Topology
				node.mobilityOption = r.next();
				node.topologyOption = r.next();

				// WLS_HW
				node.WLS_HW = new ArrayList<String>();
				int wls_hw_count = r.nextInt();
				for (int j = 0; j < wls_hw_count; j++) {
					String wls_hw = r.next();
					node.WLS_HW.add(wls_hw);
				}

				// WLS_NAME
				int wls_name_cnt = r.nextInt();
				for (int j = 0; j < wls_name_cnt; j++) {
					String wls_n = r.next();
					node.WLS_HW.add(wls_n);
				}

				// Edges
				node.adjacent = new ArrayList<Edge>();
				int adjacent = r.nextInt();
				for (int j = 0; j < adjacent; j++) {
					String next = r.next();
					pairs.add(new StringPair(nodeName, next));
				}

				// Channels
				node.channels = new ArrayList<Channel>();
				int channels = r.nextInt();
				for (int j = 0; j < channels; j++) {
					int channel = r.nextInt();
					double prob = r.nextDouble();
					node.channels.add(new Channel(channel, prob));
				}

				// Source & destination flags
				String isSource = r.next();
				String isDest = r.next();
				if (isSource.equals("true")) {
					node.isSource = true;
					node.isDestination = false;
				}
				if (isDest.equals("true")) {
					node.isSource = false;
					node.isDestination = true;
				}

				// Node properties
				node.averageSwitchingTime = r.nextDouble();
				node.averageNodalDelay = r.nextDouble();
				node.totalSwitches = r.nextInt();

				// Destination node
				String destName = r.next();
				srcToDestName.put(nodeName, destName);

				// FlowID
				node.flowID = r.nextInt();

				if (isPrimary) {
					// TODO loading activeDist, inactiveDist
				}

				drawingPanel.listOfNodes.add(node);
			}

			for (StringPair e : pairs) {
				Edge edge = new Edge(map.get(e.n1), map.get(e.n2));
				map.get(e.n1).adjacent.add(edge);
			}

			for (Node node : drawingPanel.listOfNodes) {
				if (node.isSource) { // set its destination node
					String srcName = node.name;
					String destName = srcToDestName.get(srcName);
					Node dest = map.get(destName);
					node.destination = dest;
				}
			}

			drawingPanel.repaint();
			JOptionPane.showMessageDialog(null, "File Loaded successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
