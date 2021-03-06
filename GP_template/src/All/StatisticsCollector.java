package All;

import java.awt.Point;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import Data.AccessPair;
import Data.Edge;
import Data.Event;
import Data.Interval;
import Data.Node;
import Data.PacketInfo;

public class StatisticsCollector {
	ArrayList<Node> listOfNodes;
	ArrayList<Interval> timeline;
	TreeMap<Integer, ArrayList<AccessPair>> packetAccesses;
	TreeMap<String,Node> nodes;
	public long minTimestamp = Long.MAX_VALUE, maxTimestamp = Long.MIN_VALUE;
	public ArrayList<Long> switchingTimeSet;
	public ArrayList<ArrayList<Double>> primaryActiveDist;
	public ArrayList<ArrayList<Double>> primaryInactiveDist;
	public ArrayList<String> primaryNames;
	public ArrayList<FallingPacketPosition> fallingPackets;
	public Node src, dest;

	public StatisticsCollector(ArrayList<Node> listOfNodes){
		this.timeline = new ArrayList<Interval>();
		this.packetAccesses = new TreeMap<Integer, ArrayList<AccessPair>>();
		this.listOfNodes = listOfNodes;
		nodes = new TreeMap<String, Node>();
		switchingTimeSet = new ArrayList<Long>();
		fallingPackets = new ArrayList<FallingPacketPosition>();

		for(Node node: listOfNodes){
			nodes.put(node.ETH_HW.toUpperCase(), node);
			for(String hw: node.WLS_HW) {
				nodes.put(hw.toUpperCase(), node);
			}
		}
		primaryActiveDist = new ArrayList<ArrayList<Double>>();
		primaryInactiveDist =  new ArrayList<ArrayList<Double>>();
		primaryNames = new ArrayList<String>();
	}
	public void parsePrimary(String fileName) {
		InputReader r = null;
		try {
			r = new InputReader(new FileReader(fileName));
		} catch(Exception ex) {

		}
		String name = r.next().toUpperCase();
		Node currentNode = null;
//		System.out.println(name);
		currentNode = nodes.get(name);
		primaryNames.add(currentNode.name);

		ArrayList<Double> active = new ArrayList<Double>();
		int n = r.nextInt();
		for (int i = 0; i < n; i++) {
//			long timestamp = r.nextLong();
			double value = r.nextDouble();
			active.add(value);
		}
		primaryActiveDist.add(active);
		ArrayList<Double> inactive = new ArrayList<Double>();
		n = r.nextInt();
		for (int i = 0; i < n; i++) {
//			long timestamp = r.nextLong();
			double value = r.nextDouble();
			inactive.add(value);
		}
		primaryInactiveDist.add(inactive);
	}

	public void parseMachine(String fileName){
		InputReader r = null;
		try {
			r = new InputReader(new FileReader(fileName));
		} catch(Exception ex) {

		}
		String name = r.next().toUpperCase(); // MAC
		Node currentNode = null;
		currentNode = nodes.get(name);
//		System.out.println(currentNode);
//		String role = r.next();

		while(true) {
			try {
				// flow number
				int flowNum = r.nextInt();
				
				// Sent packets table.
				int sentCount = r.nextInt();
				for (int i = 0; i < sentCount; i++) {
					int packetID = r.nextInt();
					long timeStamp = r.nextLong();
					String to = r.next().toUpperCase();
					Node toNode = nodes.get(to);

					currentNode.outPackets.put(packetID, new PacketInfo(
							packetID, timeStamp, toNode));
					insertAccessPair(new AccessPair(timeStamp, currentNode),
							packetID);
				}

				// Received packets table.
				int receviedCount = r.nextInt();
				for (int i = 0; i < receviedCount; i++) {
					int packetID = r.nextInt();
					long timeStamp = r.nextLong();

					currentNode.inPackets.put(packetID, new PacketInfo(
							packetID, timeStamp, currentNode));
					insertAccessPair(new AccessPair(timeStamp, currentNode),
							packetID);
				}

				int switchCount = r.nextInt();
				for (int i = 0; i < switchCount; i++) {
					String interfaceName = r.next();
					long timeStamp = r.nextLong();
					long switchingTime = r.nextLong();
					int channelFrom = r.nextInt();
					int channelTo = r.nextInt();

					switchingTimeSet.add(switchingTime);

					currentNode.totalSwitches++;
					currentNode.averageSwitchingTime += switchingTime;
				}
				Node.maxTotalSwitches = Math.max(Node.maxTotalSwitches,
						switchCount);
				currentNode.averageSwitchingTime /= switchCount;

				int noOfProtocolPackets = r.nextInt();
				for (int i = 0; i < noOfProtocolPackets; i++) {
					String message = r.next();
					String fromMac = r.next().toUpperCase();
					String toMac = r.next().toUpperCase();
					long fromTime = r.nextLong();
					long toTime = r.nextLong();
					minTimestamp = Math.min(fromTime, minTimestamp);
					maxTimestamp = Math.max(toTime, maxTimestamp);
					timeline.add(new Interval(-1, nodes.get(fromMac), nodes
							.get(toMac), fromTime, toTime, message));
				}
			} catch (Exception ex) {
				System.out.println("CATCH: Stats file ended");
				break;
			}
		}
	}

	private void insertAccessPair(AccessPair ap, int packetID) {
		if(!packetAccesses.containsKey(packetID))packetAccesses.put(packetID, new ArrayList<AccessPair>());

		ArrayList<AccessPair> accessList = packetAccesses.get(packetID);
		accessList.add(ap);
	}

	public void calculate(){
		// Generate the intervals for the simulation of the timeline.
		for(int packetID : packetAccesses.keySet()){
			ArrayList<AccessPair> accessList = packetAccesses.get(packetID);
			Collections.sort(accessList);
			minTimestamp = Math.min(accessList.get(0).timestamp, minTimestamp);
			maxTimestamp = Math.max(accessList.get(accessList.size() - 1).timestamp, maxTimestamp);
			for(int i = 0; i + 1 < accessList.size(); i++){
				AccessPair fromAP = accessList.get(i);
				AccessPair toAP = accessList.get(i + 1);
				timeline.add(new Interval(packetID, fromAP.node, toAP.node, fromAP.timestamp, toAP.timestamp, ""));
			}
//			AccessPair lastAccess = accessList.get(accessList.size()-1);
//			if(!lastAccess.node.isDestination) {
////				Node node = lastAccess.node;
////				PacketInfo info = node.outPackets.get(packetID);
////				timeline.add(new Interval(packetID, node, info.to, lastAccess.timestamp, -1, ""));
//
//				addLostPacketToTimeline(packetID, lastAccess);
//			}
		}

		for(Node node : listOfNodes){
//			System.out.println(node + " <->");
			if (node.isSource)
				src = node;
			if (node.isDestination) {
				System.out.println("NODE IS DESTINATION");
				dest = node;
			}
			for(int packetID : node.inPackets.keySet()){
				if(node.outPackets.containsKey(packetID)){
					long timeDiff = node.outPackets.get(packetID).timeStamp - node.inPackets.get(packetID).timeStamp;
					node.averageNodalDelay += timeDiff;
				}else{
					//loss!
				}
			}

			if(node.outPackets.size() == 0) node.averageNodalDelay = 0;
			else node.averageNodalDelay /= node.outPackets.size();

			Node.maxAverageNodalDelay = Math.max(Node.maxAverageNodalDelay, node.averageNodalDelay);
			//EOF
			for(Edge edge : node.adjacent)if(node == edge.from){
				System.out.println("Calculating the statistics for the " + edge.from.name + " -> " + edge.to.name);
				int toBeSent = 0, lost = 0;
				for(int packetID : edge.from.outPackets.keySet())if(edge.to == edge.from.outPackets.get(packetID).to){
					toBeSent++;
					edge.used = true;
					if(edge.to.inPackets.containsKey(packetID)){
						long timeDiff = edge.to.inPackets.get(packetID).timeStamp - edge.from.outPackets.get(packetID).timeStamp;
						timeDiff = Math.abs(timeDiff);
//						System.out.println("Adding to the link delay " + edge.from.name + " -> " + edge.to.name + ", " + timeDiff);
						edge.linkDelay += timeDiff;
					}else{
						lost++;
					}
				}

				edge.lossRatio = lost * 1.0 / toBeSent;
				
				
				if(toBeSent == 0 || toBeSent - lost == 0)edge.linkDelay = 0;
				else edge.linkDelay /= (toBeSent - lost);

				if(toBeSent - lost > 0)
					Edge.maxLinkDelay = Math.max(edge.linkDelay, Edge.maxLinkDelay);
				System.out.println("------>   " + edge.linkDelay);
				edge.throughput += toBeSent - lost;
				edge.packetCount = toBeSent - lost;
				// EOF
			}
		}

	}
	private void addLostPacketToTimeline(int packetID, AccessPair lastAccess) {
//		Node node = lastAccess.node;
//		PacketInfo info = node.outPackets.get(packetID);
//		int count = 1;
//		int iterations = 10;
//		int dx,dy;
//		int currX = node.x;
//		int currY = node.y;
//		long fromTime = lastAccess.timestamp;
//		int currDisp = 1;
//		while(iterations -- > 0) {
//			dx = count;
//			dy = count * count;
//			currX += dx;
//			currY += dy;
//			count++;
//			fallingPackets.add(new FallingPacketPosition(fromTime+currDisp, fromTime+currDisp+1, currX, currY));
//			System.out.println((fromTime+currDisp)+" "+(fromTime+currDisp+1));
//			currDisp+=2;
//		}
//		timeline.add(new Interval(packetID, node, info.to, lastAccess.timestamp, -1, ""));
	}
}

