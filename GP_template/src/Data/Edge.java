package Data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import All.Constants;
import All.DrawingPanel;

public class Edge {
	public Node from, to;
	//Additional data to be added.
	public double linkDelay, lossRatio, throughput;
	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
		
		linkDelay = Math.random();
		lossRatio = Math.random();
		throughput = Math.random();
	}
	
	public boolean equals(Object e){
		Edge o = (Edge)(e);
		return from == o.from && to == o.to;
	}
	
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption){
		if(drawingOption.equals("Init")){
			g2d.setColor(Constants.EDGE_COLOR);
		}else if(drawingOption.equals("LossRatio")){
			g2d.setColor(new Color((int) (256 * lossRatio), (int) (256 * (1 - lossRatio)), 0));
		}else if(drawingOption.equals("LinkDelay")){
			g2d.setColor(new Color((int) (256 * linkDelay), (int) (256 * (1 - linkDelay)), 0));
		}else if(drawingOption.equals("Throughput")){
			g2d.setColor(new Color((int) (256 * throughput), (int) (256 * (1 - throughput)), 0));
		}else{
			g2d.setColor(Constants.EDGE_COLOR);
		}
		
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(from.x+10, from.y+10, to.x+10, to.y+10);
		g2d.setStroke(new BasicStroke(1));
	}
}