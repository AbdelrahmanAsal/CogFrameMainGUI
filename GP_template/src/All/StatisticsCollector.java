package All;

import java.util.ArrayList;

import Data.Edge;
import Data.Node;
import Data.PacketInfo;

public class StatisticsCollector {
	ArrayList<Node> listOfNodes;
	public StatisticsCollector(ArrayList<Node> listOfNodes){
		this.listOfNodes = listOfNodes;
		for(Node node: listOfNodes){
			
		}
	}
	public void parse(String fileName){
		InputReader r = new InputReader(System.in);
		
		String name = r.next();
		Node currentNode = null;
		
		for(Node node : listOfNodes){
			if(node.name.equals(name)){
				currentNode = node;
				break;
			}
		}
//		String role = r.next();
		
		//Sent packets table.
		int sentCount = r.nextInt();
		for(int i = 0; i < sentCount; i++){
			int packetID = r.nextInt();
			long timeStamp = r.nextLong();
			String to = r.next();
			Node toNode = null;
			
			for(Node node : listOfNodes){
				if(node.name.equals(to)){
					toNode = node;
					break;
				}
			}
			
			currentNode.outPackets.put(packetID, new PacketInfo(packetID, timeStamp, toNode));
		}
		
		//Received packets table.
		int receviedCount = r.nextInt();
		for(int i = 0; i < receviedCount; i++){
			int packetID = r.nextInt();
			long timeStamp = r.nextLong();
			
			currentNode.inPackets.put(packetID, new PacketInfo(packetID, timeStamp, currentNode));			
		}
		
		int switchCount = r.nextInt();
		for(int i = 0; i < switchCount; i++){
			String interfaceName = r.next();
			long timeStamp = r.nextLong();
			long switchingTime = r.nextLong();
			int channelFrom = r.nextInt();
			int channelTo = r.nextInt();
			
			currentNode.totalSwitches++;
			currentNode.averageSwitchingTime += switchingTime;
		}
		
		currentNode.averageSwitchingTime /= switchCount;
	}
	
	public void calculate(){
		for(Node node : listOfNodes){
			for(int packetID : node.inPackets.keySet()){
				if(node.outPackets.containsKey(packetID)){
					long timeDiff = node.outPackets.get(packetID).timeStamp - node.inPackets.get(packetID).timeStamp;
					node.averageNodalDelay += timeDiff;
				}else{
					//loss!
				}
			}
			node.averageNodalDelay /= node.outPackets.size();
			
			for(Edge edge : node.adjacent)if(node == edge.from){
				int toBeSent = 0, lost = 0;
				for(int packetID : edge.from.outPackets.keySet())if(edge.to == edge.from.outPackets.get(packetID).to){
					long timeDiff = edge.to.inPackets.get(packetID).timeStamp - edge.from.outPackets.get(packetID).timeStamp;
					toBeSent++;
					if(edge.to.inPackets.containsKey(packetID)){
						edge.linkDelay += timeDiff;
					}else{
						lost++;
					}
				}
				
				edge.lossRatio = lost * 1.0 / toBeSent;
				edge.linkDelay /= toBeSent - lost;
				edge.throughput += toBeSent - lost;
				edge.packetCount = toBeSent - lost;
			}
		}
		
		
	}
	
}

