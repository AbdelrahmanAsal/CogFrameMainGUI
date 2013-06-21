package All;

import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import Data.Edge;
import Data.Node;
import Data.Primary;

public class Saver {

	DrawingPanel drawingPanel;

	public Saver(DrawingPanel dp) {
		drawingPanel = dp;
	}

	public void save() {
		try {
			String fileName = JOptionPane.showInputDialog(null,
					"Enter the file name");

			if (fileName == null) {
				System.out.println("Saving cancelled.");
				return;
			}

			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			// Number Of Nodes
			out.println(drawingPanel.listOfNodes.size());
			for (Node node : drawingPanel.listOfNodes) {
				// Type
				if (node instanceof Primary) {
					out.println("primary");
				} else {
					out.println("machine");
				}

				// Name, X, Y, ETH_IP, ETH_HW
				out.println(node.name + " " + node.x + " " + node.y);
				out.println(node.ETH_IP);
				out.println(node.ETH_HW);

				// Mobility & Topology
				out.println(node.mobilityOption);
				out.println(node.topologyOption);

				// WLS_HW
				out.println(node.WLS_HW.size());
				for (String w : node.WLS_HW)
					out.println(w);

				// WLS_NAME
				out.println(node.WLS_Name.size());
				for (String w : node.WLS_Name)
					out.println(w);

				// Edges
				out.println(node.adjacent.size());
				for (Edge edge : node.adjacent)
					out.println(edge.to.name);

				// Channels
				out.println(node.channels.size());
				for (Channel c : node.channels)
					out.printf("%s %s\n", c.channel, c.probability);

				// Source & destination flags
				out.println(node.isSource + " " + node.isDestination);

				// Node properties
				out.println(node.averageSwitchingTime);
				out.println(node.averageNodalDelay);
				out.println(node.totalSwitches);

				// Destination node
				if (node.destination != null)
					out.println(node.destination.name);
				else
					out.println("Not_a_source_node");

				// FlowID
				out.println(node.flowID);

				if (node instanceof Primary) {
					// TODO saving activeDist, inactiveDist
				}
			}
			JOptionPane.showMessageDialog(null, "File saved successfully!");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
