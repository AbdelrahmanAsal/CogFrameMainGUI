package All;

public class FallingPacketPosition {

	int x,y;
	long fromTime, toTime;
	
	public FallingPacketPosition(long fromTime, long toTime, int x, int y) {
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.x = x;
		this.y = y;
	}
}
