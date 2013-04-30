package Data;

public class Interval {
	public int packetID;
	public Node fromNode, toNode;
	public long fromTime, toTime;
	public String message;
	public Interval(int packetID, Node fromNode, Node toNode, long fromTime, long toTime, String message){
		this.packetID = packetID;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.message = message;
	}
	public String toString(){
		return String.format("<%d, %d, %d, %s, %s>", packetID, fromTime, toTime, fromNode.name, toNode.name);
	}
}
