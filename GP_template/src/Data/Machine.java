package Data;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import All.DrawingPanel;


public class Machine extends Node{
	public Machine(String name, int x, int y) {
		super(name, x, y);
	}

	@Override
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption){
		
		if(drawingOption.equals("Init")){
			if(drawingPanel.selectedIndex != -1 && this == drawingPanel.listOfNodes.get(drawingPanel.selectedIndex))g2d.setColor(Color.RED);//Color the selected node.
			else if(this == drawingPanel.source) g2d.setColor(Color.GREEN);//Color the source.
			else if(this == drawingPanel.destination) g2d.setColor(Color.BLUE);//Color the destination.
			else g2d.setColor(Color.BLACK);
			
			g2d.drawOval(x - 50, y - 50, 20 + 100, 20 + 100);
			
		}else if(drawingOption.equals("NodalDelay")){
			g2d.setColor(new Color((int) (256 * nodalDelay), (int) (256 * (1 - nodalDelay)), 0));
		}else if(drawingOption.equals("WirelessInterfaces")){
			g2d.setColor(new Color(0, 0, (int) (256 * (totalSwitches) / 100)));
		}else{
			g2d.setColor(Color.BLACK);
		}
		
		g2d.fillRect(x, y, 20, 20);
		//Anchors.
		g2d.drawLine(x - 10, y + 10, x + 30, y + 10);
		g2d.drawLine(x + 10, y - 10, x + 10, y + 30);
	}
	

	
	
	@Override
	public String type(){
		return "Machine";
	}
}
