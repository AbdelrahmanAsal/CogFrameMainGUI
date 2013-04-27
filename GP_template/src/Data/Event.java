package Data;

public class Event implements Comparable<Event>{
	public boolean sent;
	public long ID;
	public long timestamp;
	public Node fromNode, toNode;
	
	public Event(boolean sent, long ID, long timestamp, Node fromNode, Node toNode){
		this.sent = sent;
		this.ID = ID;
		this.timestamp = timestamp;
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	@Override
	public int compareTo(Event b) {
		return new Long(timestamp).compareTo(b.timestamp);
	}
}
