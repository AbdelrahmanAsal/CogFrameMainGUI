package All;

public class Channel {

	public int channel;
	public double probability;

	public Channel(int channel, double probability) {
		this.channel = channel;
		this.probability = probability;
	}

	public String toString() {
		return String.format("%s:%s", channel, probability);
	}
}
