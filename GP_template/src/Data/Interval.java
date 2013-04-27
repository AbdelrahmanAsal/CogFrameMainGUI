package Data;

public class Interval {
	public int packetID;
	public Node fromNode, toNode;
	public long fromTime, toTime;
	public Interval(int packetID, Node fromNode, Node toNode, long fromTime, long toTime){
		this.packetID = packetID;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	public String toString(){
		return String.format("<%d, %d, %d, %s, %s>", packetID, fromTime, toTime, fromNode.name, toNode.name);
	}
}
