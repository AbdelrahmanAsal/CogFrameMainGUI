package AttributePanel;
import java.awt.CardLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import All.Constants;
import All.DrawingPanel;


public class AttributesPanel extends JPanel{
	public MachineAttributePanel machineAP;
	public PrimaryAttributePanel primaryAP;
	public NullAttributePanel nullAP;
	public CardLayout informationPanelCardLayout;
	
	JTabbedPane tabbedPane;
	JComponent visualizationTab;
	
	public JPanel informationPanel;
	public VisualizeAttributePanel visualizationPanel;
	
	public DrawingPanel drawingPanel;
	public AttributesPanel(DrawingPanel drawingPanel){
		this.drawingPanel = drawingPanel;
		
		informationPanelCardLayout = new CardLayout();
		
		informationPanel = new JPanel();
		informationPanel.setLayout(informationPanelCardLayout);
		
		visualizationPanel = new VisualizeAttributePanel(drawingPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Information", informationPanel);
		
		add(tabbedPane);
		
		machineAP = new MachineAttributePanel();
		primaryAP = new PrimaryAttributePanel();
		nullAP = new NullAttributePanel();
		
		informationPanel.add(nullAP, Constants.nullAPCode);
		informationPanel.add(machineAP, Constants.machineAPCode);
		informationPanel.add(primaryAP, Constants.primaryAPCode);
	}
	
	public void activeVisualization(){
		tabbedPane.addTab("Visualization", visualizationPanel);
	}
	public void deactiveVisualization(){
		tabbedPane.remove(1);
	}
}

