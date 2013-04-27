package Data;

public class AccessPair implements Comparable<AccessPair>{
	public long timestamp;
	public Node node;
	public AccessPair(long timestamp, Node node){
		this.timestamp = timestamp;
		this.node = node;
	}
	@Override
	public int compareTo(AccessPair b) {
		return new Long(timestamp).compareTo(b.timestamp);
	}
}
