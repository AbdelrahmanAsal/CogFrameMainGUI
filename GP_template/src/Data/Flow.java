package Data;

public class Flow {
	int flowID;
	Node srcName, destName;
	
	public Flow(int id, Node srcName, Node destName) {
		flowID = id;
		this.srcName = srcName;
		this.destName = destName;
	}
	
}
