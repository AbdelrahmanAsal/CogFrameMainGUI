package All;

public class Channel {
	public int channel;
	public int probability;
	public Channel(int channel, int probability){
		this.channel = channel;
		this.probability = probability;
	}
	
	public String toString(){
		return String.format("%s:%s", channel, probability);
	}
}
