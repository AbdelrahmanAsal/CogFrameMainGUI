package AttributePanel;

import java.awt.Color;

public class PacketsColorRowEntry {
	public String type;
	Color color;
	public boolean show;
	
	public PacketsColorRowEntry(String type, Color color, boolean show){
		this.type = type;
		this.color = color;
		this.show = show;
	}
}
