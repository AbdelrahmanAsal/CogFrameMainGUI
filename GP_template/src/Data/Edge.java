package Data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.*;

import All.Constants;
import All.DrawingPanel;

public class Edge {
	public Node from, to;
	//Additional data to be added.
	public double linkDelay, lossRatio, throughput;
	public int packetCount;
	public static double maxLinkDelay;
	public boolean used;
	
	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
	}
	
	public boolean equals(Object e){
		Edge o = (Edge)(e);
		return from == o.from && to == o.to;
	}
	
	public void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption){
		if(drawingOption.equals("Init")){
			if(this == drawingPanel.selectedEdge)g2d.setColor(Constants.SELECTED_COLOR);
			else g2d.setColor(Constants.EDGE_COLOR);
		}else if(drawingOption.equals("LossRatio")){
			if (!used)
				return;
			System.out.println("Loss Ratio: "+ lossRatio);
			g2d.setColor(new Color((int) (255 * lossRatio), (int) (255 * (1 - lossRatio)), 0));
		}else if(drawingOption.equals("LinkDelay")){
			if (!used)
				return;
			System.out.println("Link Delay: "+ from.name + " -> " + to.name + ": " + linkDelay + ", " + maxLinkDelay);
			g2d.setColor(new Color((int) (255 * (linkDelay / maxLinkDelay)), (int) (255 * (1 - ((linkDelay) / maxLinkDelay))), 0));
		}else if(drawingOption.equals("Throughput")){
//			g2d.setColor(new Color((int) (255 * throughput), (int) (255 * (1 - throughput)), 0));
		}else{
			System.out.println("EDGE");
			g2d.setColor(Constants.EDGE_COLOR);
		}
		
		AffineTransform tx = new AffineTransform();
		
		Polygon arrowHead = new Polygon();  
		arrowHead.addPoint( 0,5);
		arrowHead.addPoint( -5, -5);
		arrowHead.addPoint( 5,-5);
		  
	    tx.setToIdentity();
	    Line2D.Double line = getRepLine();
		double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
	    tx.translate(line.x2, line.y2);
	    tx.rotate((angle-Math.PI/2d));  

	    g2d.setTransform(tx);   
	    g2d.fill(arrowHead);
		tx.setToIdentity();
	    g2d.setTransform(tx);
	    
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine((int)line.x1, (int)line.y1, (int)line.x2, (int)line.y2);
		g2d.setStroke(new BasicStroke(1));
	}
	
	public Line2D.Double getRepLine(){
		double dx = ((to.x - from.x) / from.distance(to));
		double dy = ((to.y - from.y) / from.distance(to));
		
		double dx2 = -dy;
		double dy2 = dx; 
		
		dx *= 60;
		dy *= 60;
		
		dx2 *= 10;
		dy2 *= 10;
		
		return new Line2D.Double(from.x + 10 + dx2 + dx, from.y + 10 + dy2 + dy, to.x+10+dx2 - dx , to.y + 10 + dy2 - dy);
	}
}