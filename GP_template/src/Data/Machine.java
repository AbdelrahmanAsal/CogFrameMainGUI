

package Data;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import All.Constants;
import All.DrawingPanel;


public class Machine extends Node{
	public Machine(String name, int x, int y) {
		super(name, x, y);
	}

	@Override
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption, boolean selectionOrNodeMode){
		if(drawingOption.equals("Init")){
			// Color the selected node.
			if(!selectionOrNodeMode)g2d.setColor(Constants.FROZEN_COLOR);
			else if(drawingPanel.ui.nodeSubset.contains(this) || this == drawingPanel.selectedNode)g2d.setColor(Constants.SELECTED_COLOR);
//			else if (this == drawingPanel.source) g2d.setColor(Constants.SOURCE_COLOR); // Color the source.
			else if (isSource) g2d.setColor(Constants.SOURCE_COLOR); // Color the source.
//			else if(this == drawingPanel.destination) g2d.setColor(Constants.DEST_COLOR); // Color the destination.
			else if(isDestination) g2d.setColor(Constants.DEST_COLOR); // Color the destination.
			else g2d.setColor(Constants.HOP_COLOR);
			
			if(topologyOption.equalsIgnoreCase("Location-based")){
				g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
				g2d.drawOval(x - 50, y - 50, 20 + 100, 20 + 100);
			}
		}else if(drawingOption.equals("NodalDelay")){
			System.out.println("Nodal Delay: "+name+" "+ averageNodalDelay + ", " + Node.maxAverageNodalDelay);
			g2d.setColor(new Color((int) (255 * (averageNodalDelay / Node.maxAverageNodalDelay)), (int) (255 * (1 - (averageNodalDelay / Node.maxAverageNodalDelay))), 0));
		}else if(drawingOption.equals("WirelessInterfaces")){
			System.out.println("WirelessInterfaces: "+name+" "+ totalSwitches);
			g2d.setColor(new Color(0, 0, (int) (255 * (totalSwitches * 1.0 / maxTotalSwitches))));
		}else{
			g2d.setColor(Constants.HOP_COLOR);
		}
		
		g2d.setStroke(new BasicStroke(1.0f,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g2d.fill(new Rectangle2D.Double(x, y, 20, 20));

		// Anchors.
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x - 10, y + 10, x + 30, y + 10);
		g2d.drawLine(x + 10, y - 10, x + 10, y + 30);
		g2d.drawRect(x, y, 20, 20);
	}
	

	
	
	@Override
	public String type(){
		return "Machine";
	}
}
