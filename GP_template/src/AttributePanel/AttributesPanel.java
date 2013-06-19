package AttributePanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import All.Constants;
import All.DrawingPanel;
import All.UI;


public class AttributesPanel extends JPanel implements ComponentListener {
	public MachineAttributePanel machineAP;
	public PrimaryAttributePanel primaryAP;
	public NullAttributePanel nullAP;
	public EdgeAttributePanel edgeAP;
	public CardLayout informationPanelCardLayout;
	
	JTabbedPane tabbedPane;
	JComponent visualizationTab;
	
	public JPanel informationPanel;
	public VisualizeAttributePanel visualizationPanel;
	ChartsPanel chartsPanel;
	
	public DrawingPanel drawingPanel;
	public AttributesPanel(DrawingPanel drawingPanel){
		this.drawingPanel = drawingPanel;
		addComponentListener(this);
		informationPanelCardLayout = new CardLayout();
		
		informationPanel = new JPanel();
		informationPanel.setLayout(informationPanelCardLayout);
		
		visualizationPanel = new VisualizeAttributePanel(drawingPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Information", informationPanel);
		
		add(tabbedPane);
		
		machineAP = new MachineAttributePanel(drawingPanel);
		primaryAP = new PrimaryAttributePanel(drawingPanel);
		nullAP = new NullAttributePanel();
		edgeAP = new EdgeAttributePanel();
		
		informationPanel.add(nullAP, Constants.nullAPCode);
		informationPanel.add(machineAP, Constants.machineAPCode);
		informationPanel.add(primaryAP, Constants.primaryAPCode);
		informationPanel.add(edgeAP, Constants.edgeAPCode);
		
		chartsPanel = new ChartsPanel(drawingPanel);
	}
	
	public void activeVisualization(){
		tabbedPane.addTab("Visualization", visualizationPanel);
		chartsPanel.panelDimension = informationPanel.getSize();
		tabbedPane.addTab("Charts", chartsPanel);
	}
	public void deactiveVisualization(){
		tabbedPane.remove(1);//Visualization.
		tabbedPane.remove(1);//Charts.
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.machineAP.setPreferredSize(new Dimension(drawingPanel.ui.getWidth() - drawingPanel.getWidth() - 100, this.getHeight() - 100));
		this.machineAP.revalidate();
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}

