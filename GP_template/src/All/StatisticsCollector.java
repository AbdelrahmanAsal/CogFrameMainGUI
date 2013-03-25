package All;

import java.util.ArrayList;
import Data.Node;

public class StatisticsCollector {
	public StatisticsCollector(ArrayList<Node> listOfNodes){
		for(Node node: listOfNodes){
			
		}
	}
	public void parse(String fileName){
		InputReader r = new InputReader(System.in);
		
		String name = r.next();
//		String role = r.next();
		
		int sentCount = r.nextInt();
		for(int i = 0; i < sentCount; i++){
			int packetID = r.nextInt();
			long timeStamp = r.nextLong();
			String to = r.next();
			Node toNode = null;
			
//			for(Node node : listOfNodes){
//				if(node.name.equals(to))
//			}
		}
	}
	
	public void calculate(ArrayList<Node> listOfNodes){
	}
	
	class PacketInfo{
		int ID;
		long timeStamp;
		Node to;
	}
}

