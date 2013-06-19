package ConfigurationMaker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import All.Channel;
import Data.Edge;
import Data.Node;
import Data.Primary;


public class ModuleMaker {

	public void generateModuleFile(Node thisNode, ArrayList<Node> listOfNodes, String ccc) throws IOException {
		//NOTES:
		//	Assuming single ethernet interface for each node.
		
		//Counting.
		int numberOfNodes = listOfNodes.size(), wirelessNodes = 0, primaryWirelessNodes = 0, wirelessEdges = 0, ethernetEdges = 0, primaryUsers = 0;
		for(Node x : listOfNodes)
		for(Edge edge : x.adjacent){
			wirelessEdges += x.WLS_HW.size() * edge.to.WLS_HW.size();
			ethernetEdges += 1;
		}
		
		for(Node node : listOfNodes)
			if(node instanceof Primary){
				primaryUsers++;
				primaryWirelessNodes += node.WLS_HW.size();
			}
		
		for(Node node : listOfNodes)
			wirelessNodes += node.WLS_HW.size();
		
		
		PrintWriter out = new PrintWriter(String.format("Module_%s.txt", thisNode.name));
		
		out.printf("%s\n", thisNode.name);
		out.printf("%s\n", thisNode.type());
		
		if(thisNode instanceof Primary){
			out.printf("%s\n", ((Primary)thisNode).activeDist);
			out.printf("%s\n", ((Primary)thisNode).inactiveDist);
		}
		
		//Ethernet.
		out.printf("%d\n", 1);
		out.printf("%s %s\n", thisNode.ETH_HW, thisNode.ETH_IP);
		
		//Wireless.
		out.printf("%d\n", thisNode.WLS_HW.size());
		for(int i = 0; i < thisNode.WLS_HW.size(); i++)
			out.printf("%s %s\n", thisNode.WLS_HW.get(i), "192.168.1.1");

		//Channels.
		out.printf("%d\n", thisNode.channels.size());
		for(Channel c : thisNode.channels)
			out.printf("%d %f\n", c.channel, c.probability);
		
		//Location.
		out.printf("%d %d\n", thisNode.x, thisNode.y);
		
		
		out.printf("%s\n", thisNode.mobilityOption);
		out.printf("%s\n", thisNode.topologyOption);
//		if(thisNode.topologyOption == TopologyOption.LOCATION_BASED) // TODO.
		
		//Overall converter.
		out.printf("%d\n", wirelessNodes + numberOfNodes);
		for(Node node : listOfNodes){
			out.printf("%s %s\n", node.ETH_HW, node.ETH_IP);
			for(int i = 0; i < node.WLS_HW.size(); i++)
				out.printf("%s %s\n", node.WLS_HW.get(i), "192.168.1.1");
		}
		
		//Overall Topology.
		//Ethernet overall topology.
		out.printf("%d\n", ethernetEdges);
		for(Node x : listOfNodes)
		for(Edge edge : x.adjacent){
			out.printf("%s %s\n", x.ETH_HW, edge.to.ETH_HW);
		}
		
		//Wirless overall topology.
		out.printf("%d\n", wirelessEdges);
		for(Node x : listOfNodes)
		for(Edge edge : x.adjacent)
			for(String wlsX : x.WLS_HW)
			for(String wlsY : edge.to.WLS_HW){
				out.printf("%s %s\n", wlsX, wlsY);
			}
		
		//Location overall.
		out.printf("%d\n", wirelessNodes + numberOfNodes);
		for(Node node : listOfNodes){
			out.printf("%s %d %d\n", node.ETH_HW, node.x, node.y);
			for(String wls : node.WLS_HW)
				out.printf("%s %d %d\n", wls, node.x, node.y);
		}
		
		//Primary users wireless macs.
		out.printf("%d\n", primaryWirelessNodes);
		for(Node node : listOfNodes)if(node instanceof Primary)
			for(String wls : node.WLS_HW)
				out.printf("%s\n", wls);

		//Print the CCC.
		out.println(ccc);
		
		out.close();
	}
}
