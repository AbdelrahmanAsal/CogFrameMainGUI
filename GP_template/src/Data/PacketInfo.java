package Data;

public class PacketInfo{
	public int ID;
	public long timeStamp;
	public Node to;
	public PacketInfo(int ID, long timeStamp, Node to){
		this.ID = ID;
		this.timeStamp = timeStamp;
		this.to = to;
	}
}