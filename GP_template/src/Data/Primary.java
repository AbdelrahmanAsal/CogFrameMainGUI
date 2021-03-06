package Data;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import All.Constants;
import All.DrawingPanel;
import Distributions.ProbabilityDistribution;


public class Primary extends Node{
	public ProbabilityDistribution activeDist, inactiveDist;
	public boolean isVirtual, isON;
	public String activeChannel;
	public boolean virtualState;
	
	public Primary(String name, int x, int y) {
		super(name, x, y);
		isVirtual = false;
		isON = false;
//		activeDist = new UniformDistribution();
//		inactiveDist = new UniformDistribution();
	}
	
	@Override
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption, boolean selectionOrNodeMode) {
		if(!selectionOrNodeMode)g2d.setColor(Constants.FROZEN_COLOR);
		else if(this == drawingPanel.selectedNode)g2d.setColor(Color.RED);//Color the selected node.
		else {
			if(isON)
				g2d.setColor(Color.GREEN);
			else
				g2d.setColor(Color.MAGENTA);
		}
		
		if(topologyOption.equalsIgnoreCase("Location-based")){
			g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
			g2d.drawOval(x - 50, y - 50, 20 + 100, 20 + 100);
		}
		
		g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g2d.fillOval(x, y, 20, 20);
		
		// Nodes names
		g2d.setColor(Color.BLACK);
		int centerizingFactor = 0;
		if (name.length() > 3)
			centerizingFactor = (name.length() - 3) / 2;
		if(drawingPanel.showNodesNamesFlag)
			g2d.drawString(name, x - centerizingFactor * 7, y - 25);
		
		
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
