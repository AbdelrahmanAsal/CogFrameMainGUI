package Data;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import All.DrawingPanel;
import Distributions.ProbabilityDistribution;
import Distributions.UniformDistribution;


public class Primary extends Node{
	public ProbabilityDistribution activeDist, inactiveDist;
	public Primary(String name, int x, int y) {
		super(name, x, y);
		
		activeDist = new UniformDistribution();
		inactiveDist = new UniformDistribution();
	}
	
	@Override
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption) {
		if(drawingPanel.selectedIndex != -1 && this == drawingPanel.listOfNodes.get(drawingPanel.selectedIndex))g2d.setColor(Color.RED);//Color the selected node.
		else g2d.setColor(Color.MAGENTA);
		
//		g2d.setColor(new Color(c.getRed(), c.getGreen() , c.getBlue() - 20));
		Color color1 = Color.MAGENTA;
		Color color2 = new Color(59, 185, 255);
		g2d.setColor(color1);
		
		g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f));
		g2d.drawOval(x - 50, y - 50, 20 + 100, 20 + 100);
		
		g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g2d.fillOval(x, y, 20, 20);
		
		//Anchors.
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x - 10, y + 10, x + 30, y + 10);
		g2d.drawLine(x + 10, y - 10, x + 10, y + 30);
		g2d.drawOval(x, y, 20, 20);
		
	}
	
	@Override
	public String toString(){
		String ret = String.format("Name: \"%s\"\n", name);
		ret += "{\n";
		ret += String.format("    IP: \"%s\"\n", ETH_IP);
		ret += String.format("    ETH_HW: F1 = \"%s\", F2_A = \"%s\", F2_B = \"%s\"\n", getETH_HW(0, -1), getETH_HW(1, 0), getETH_HW(1, 1));
		for(int i = 0; i < WLS_HW.size(); i++)
			ret += String.format("    WLS_HW_%2d: F1 = \"%s\", F2_A = \"%s\", F2_B = \"%s\"\n", i, getWLS_HW(0, -1, i), getWLS_HW(1, 0, i), getWLS_HW(1, 1, i));
		ret += String.format("    Location: X = \"%d\", Y = \"%d\"\n", x, y);
		ret += String.format("    Active: %s\n", activeDist);
		ret += String.format("    Inactive: %s\n", inactiveDist);
		ret += "}\n";
		
		return ret;
	}
	
	@Override
	public String type(){
		return "Primary";
	}
}
