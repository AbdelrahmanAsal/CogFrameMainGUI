package All;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import AttributePanel.PacketsColorRowEntry;
import ConfigurationMaker.ModuleMaker;
import ConfigurationMaker.NodeMaker;
import ConfigurationMaker.PrimaryMaker;
import Data.Edge;
import Data.Interval;
import Data.Machine;
import Data.Node;
import Data.Primary;


public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener, ComponentListener{
	public UI ui;
	public DrawingPanel drawingPanel;
	public Edge selectedEdge;
	public Node selectedNode;
	public Node fromNode, toNode;
	
	public ArrayList<Node> listOfNodes;
	public Node source, destination;
	public int offX, offY;
	
	public JButton setSourceButton, setDestinationButton, generate;
	public JToggleButton visualizeButton, gridButton;
	public StatisticsCollector sc;
	
	public ButtonGroup modes;
	public JToggleButton selectionMode, nodesMode, edgesMode;
	
	public int maxRange;
	TreeMap<String, Color> colorMap;
	public DrawingPanel(UI ui_){
		setBorder(BorderFactory.createTitledBorder("Network topology"));
		addComponentListener(this);
		this.drawingPanel = this;
		this.ui = ui_;
		colorMap = null;
		listOfNodes = new ArrayList<Node>();
		
		Node node1 = new Machine("1", 200, 200);
		Node node2 = new Machine("2", 300, 300);
		Node node3 = new Primary("3", 400, 400);
		node1.isSource = true;
		node2.isDestination = true;
		
		node1.WLS_HW.add("08:11:96:8B:84:F4");
		node1.WLS_Name.add("wlan0");
		node2.WLS_HW.add("08:11:96:8B:84:F3");
		node2.WLS_Name.add("wlan1");
		node3.WLS_HW.add("08:11:96:8B:84:F2");
		node3.WLS_Name.add("wlan1");
		
		listOfNodes.add(node1);
		listOfNodes.add(node2);
		listOfNodes.add(node3);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//Editing Modes.
		JPanel editingModes = new JPanel();
		editingModes.setBorder(BorderFactory.createTitledBorder(""));
		
		selectionMode = new JToggleButton("Select");
		nodesMode = new JToggleButton("Node");
		edgesMode = new JToggleButton("Edge");
		
		modes = new ButtonGroup();
		modes.add(selectionMode);
		modes.add(nodesMode);
		modes.add(edgesMode);
		selectionMode.setSelected(true);
		
		editingModes.add(selectionMode);
		editingModes.add(nodesMode);
		editingModes.add(edgesMode);
		
		add(editingModes);
		
		JPanel experimentTools = new JPanel();
		experimentTools.setBorder(BorderFactory.createTitledBorder(""));
		
		setSourceButton = new JButton("Set Source");
		setDestinationButton = new JButton("Set Dest.");
		
		setSourceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null && selectedNode instanceof Machine){
					if(source != null)source.isSource = false;
					
					source = selectedNode;
					source.isSource = true;
					repaint();
				}
			}
		});
		experimentTools.add(setSourceButton);
		
		setDestinationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null && selectedNode instanceof Machine){
					if(destination != null)destination.isDestination = false;
					
					destination = selectedNode;
					destination.isDestination = true;
					repaint();
				}
			}
		});
		experimentTools.add(setDestinationButton);
		

		generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(source == null){
					JOptionPane.showMessageDialog(drawingPanel, "Missing source node.");
					return;
				}
				
				if(destination == null){
					JOptionPane.showMessageDialog(drawingPanel, "Missing destination node.");
					return;
				}
				
				final JFileChooser fc = new JFileChooser();
				
				int ret = fc.showDialog(drawingPanel, "Choose the source template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing source template.");
					return;
				}
				String sourceTemplate = fc.getSelectedFile().getAbsolutePath();
				
				ret = fc.showDialog(drawingPanel, "Choose the destination template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing destination template.");
					return;
				}
				String destinationTemplate = fc.getSelectedFile().getAbsolutePath();
				
				ret = fc.showDialog(drawingPanel, "Choose the hop template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing hop template.");
					return;
				}
				String hopTemplate = fc.getSelectedFile().getAbsolutePath();
				
				ret = fc.showDialog(drawingPanel, "Choose the primary template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing primary template.");
					return;
				}
				String primaryTemplate = fc.getSelectedFile().getAbsolutePath();
				
				NodeMaker nodeMaker = new NodeMaker();
				PrimaryMaker primaryMaker = new PrimaryMaker();
				ModuleMaker moduleMaker = new ModuleMaker();
				
				//Generate the files from templates.
				try{
					//Generate the files for all the nodes.
					for(Node node : listOfNodes){
						//Generate the configuration files for this node.
						if(node instanceof Primary){
							primaryMaker.parseTemplateFile(primaryTemplate, (Primary)node);
						} else {
							Node[] adjacentNodes = new Node[node.adjacent.size()];
							for(int i = 0; i < node.adjacent.size(); i++)
								adjacentNodes[i] = node.adjacent.get(i).to;
							if(node == source){//Special treatment for the source node.
								nodeMaker.parseTemplateFile(sourceTemplate, source, destination, node, adjacentNodes);
							}else if(node == destination){//Special treatment for the destination node.
								nodeMaker.parseTemplateFile(destinationTemplate, source, destination, node, adjacentNodes);
							}else{//Rest of the nodes treated as hops.
								nodeMaker.parseTemplateFile(hopTemplate, source, destination, node, adjacentNodes);
							}
						}
						
						//Generate
						moduleMaker.generateModuleFile(node, listOfNodes, ui.ccc);
					}
				}catch(Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(drawingPanel, "Error generating nodes files.");
					return;
				}
				
			}
		});
		experimentTools.add(generate);
		
		visualizeButton = new JToggleButton("Visualize");
		visualizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!visualizeButton.isSelected()){
					ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.nullAPCode);
					ui.attributesPanel.deactiveVisualization();
					return;
				}
				
				ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.visualizeAPCode);
				ui.attributesPanel.activeVisualization();
				
				sc = new StatisticsCollector(listOfNodes);
				
				for(Node node : listOfNodes){
					JFileChooser fc = new JFileChooser();
					int ret = fc.showDialog(drawingPanel, String.format("Choose Node:(%s) statistics file.", node.name));
					if(ret != JFileChooser.APPROVE_OPTION){
						JOptionPane.showMessageDialog(drawingPanel, "Missing statistics file.");
						visualizeButton.setSelected(false);
						return;
					}
					
					String statisticsFile = fc.getSelectedFile().getAbsolutePath();
					if(node instanceof Machine)
						sc.parseMachine(statisticsFile);
					else if(node instanceof Primary) 
						sc.parsePrimary(statisticsFile);
				}
				
				sc.calculate();
				fillColorMap();
				maxRange = (int) (sc.maxTimestamp - sc.minTimestamp);
				ui.attributesPanel.visualizationPanel.ticker.setMaximum(maxRange);
				ui.attributesPanel.visualizationPanel.ticker.setValue(0);
				
				ui.attributesPanel.visualizationPanel.ticker.setMajorTickSpacing(maxRange/5);
				ui.attributesPanel.visualizationPanel.ticker.setMinorTickSpacing(maxRange/50); 
				ui.attributesPanel.visualizationPanel.ticker.setPaintTicks(true);    
				ui.attributesPanel.visualizationPanel.ticker.setPaintLabels(true);   
				
			}
		});
		experimentTools.add(visualizeButton);
		
		gridButton = new JToggleButton("Show grid");
		gridButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		gridButton.setSelected(true);
		experimentTools.add(gridButton);
		
		add(experimentTools);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		float dash[] = { 10.0f };
		g2d.setStroke(new BasicStroke(0.1f, BasicStroke.CAP_BUTT,
			        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		
		int width = getWidth();
		int height = getHeight();

		if(gridButton.isSelected()) {
			int stX = 20;
			//Vertical.
			for(int d = 0; stX + d * 20 < width - 20; d++)
				g2d.drawLine(stX + d * 20, 115, stX + d * 20, height - 55);

			//Horizental.
			for(int d = 2; 70 + d * 20 < height - 60; d++)
				g2d.drawLine(stX - 5, 80 + d * 20, width - 25, 80 + d * 20);
		}

		//Legend box.
        g2d.drawString("Source", 45, height - Constants.MARGIN);
        g2d.drawString("Destination", 175, height - Constants.MARGIN);
        g2d.drawString("Selected", 315, height - Constants.MARGIN);
        g2d.setColor(Constants.SOURCE_COLOR);
        g2d.fillRect(15, height - 35, 25, 20);
        g2d.setColor(Constants.DEST_COLOR);
        g2d.fillRect(145, height - 35, 25, 20);
        g2d.setColor(Constants.SELECTED_COLOR);
        g2d.fillRect(285, height - 35, 25, 20);

        g2d.setStroke(new BasicStroke(1));
		
		String drawingOption = visualizeButton.isSelected() ? ui.attributesPanel.visualizationPanel.visualiztionOptions.getSelection().getActionCommand() : "Init";
		
		for (Node node : listOfNodes) {
			// Draw the Node itself.
			node.draw(g2d, this, drawingOption);
		}
		
		for(Node node : listOfNodes){
			// Draw all the edges.
			for (Edge edge : node.adjacent) {
				edge.draw(g2d, this, drawingOption);
			}
		}
		
		// Draw simulation of the packets.
		if(!drawingOption.equals("Init")){
			long currentTime = ui.attributesPanel.visualizationPanel.currentSimulationTime;
			System.out.println("Current Simulation time: " + currentTime);
			for(Interval interval : sc.timeline){
				if(interval.fromTime <= currentTime + sc.minTimestamp && currentTime + sc.minTimestamp <= interval.toTime){
					int fromX = interval.fromNode.x;
					int fromY = interval.fromNode.y;
					
					int toX = interval.toNode.x;
					int toY = interval.toNode.y;
					
					int dX = toX - fromX;
					int dY = toY - fromY;
					
					long currentDisp = currentTime + sc.minTimestamp - interval.fromTime;
					long totalTime = interval.toTime - interval.fromTime;
	
					int packetX = (int) (fromX + (currentDisp * 1.0 / totalTime) * dX);
					int packetY = (int) (fromY + (currentDisp * 1.0 / totalTime) * dY);
					
					System.out.println("Current Displacement: " + currentDisp);
					System.out.printf("Packet position: (%d, %d)\n", packetX, packetY);
					
					if(interval.packetID == -1) {
						// protocol related packet
						if(colorMap == null || !colorMap.containsKey(interval.message))
							continue;
						g2d.setColor(colorMap.get(interval.message));
						g2d.fillOval(packetX + 5, packetY + 5, 10, 10);
					} else {
						g2d.setColor(Constants.SELECTED_COLOR);
						g2d.fillOval(packetX + 5, packetY + 5, 10, 10);
					}
					
					g2d.setColor(Color.BLACK);
					g2d.drawOval(packetX + 5, packetY + 5, 10, 10);
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent mouse) {
		if(visualizeButton.isSelected()) return;
		
		if(selectedNode != null){
			selectedNode.x = cap(mouse.getX() - offX, 70, getWidth() - 100);
			selectedNode.y = cap(mouse.getY() - offY, 160, getHeight() - 120);
		}
		
		repaint();
	}

	private int cap(int i, int min, int max) {
		return Math.max(min, Math.min(i, max));
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent mouse) {
//		if(visualizeButton.isSelected())return;
		selectedNode = null;
		selectedEdge = null;
		if(selectionMode.isSelected()){
			selectedNode = getSelectedNode(mouse);
			if(selectedNode != null){
				offX = mouse.getX() - selectedNode.x;
				offY = mouse.getY() - selectedNode.y;
			}else{
				selectedEdge = getSelectedEdge(mouse);
			}
		}else if(nodesMode.isSelected()){
			if(getSelectedNode(mouse) == null){
				String[] options = new String[]{"Machine", "Primary"};
				int type = JOptionPane.showOptionDialog(null, "Choose type of the node", "", 1, 2, null, options, options[0]);
				
				System.out.println("Creating and adding a new node");

				Node node;
				if(type == 0){
					//Create a node with a random name.
					node = new Machine((int)(Math.random() * 100) + "", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
				}else if(type == 1){
					//Create a primary user with a random name.
					node = new Primary((int)(Math.random() * 100) + "", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
				}else{
					JOptionPane.showMessageDialog(null, "Not a correct node type.");
					return;
				}
				listOfNodes.add(node);
				
				System.out.println("Successfully created the node:\n" + node);
			}else{
				int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this node?");
				
				if(result == 0){//Yes delete the node.
					Node toDeleteNode = getSelectedNode(mouse);
					
					listOfNodes.remove(toDeleteNode);
					
					for(Node rest : listOfNodes)
						rest.adjacent.remove(toDeleteNode);
					
					System.out.println("Successfully deleted the node:\n" + toDeleteNode);
				}
			}
		}else if(edgesMode.isSelected()){
			fromNode = getSelectedNode(mouse);
		}
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent mouse) {
//		if(visualizeButton.isSelected())return;
		
		if(selectionMode.isSelected()){
			if(selectedNode != null){
				if(selectedNode instanceof Machine){
					ui.attributesPanel.machineAP.setInfo((Machine)selectedNode);
					ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.machineAPCode);
				}else if(selectedNode instanceof Primary){
					ui.attributesPanel.primaryAP.setInfo((Primary)selectedNode);
					ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.primaryAPCode);
				}else{
					System.out.println("Unidentified node type.");
				}
			}else if(selectedEdge != null){
				ui.attributesPanel.edgeAP.setInfo(selectedEdge);
				ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.edgeAPCode);
			}else{
				ui.attributesPanel.informationPanelCardLayout.show(ui.attributesPanel.informationPanel, Constants.nullAPCode);
			}
		}else if(edgesMode.isSelected()){
			toNode = getSelectedNode(mouse);
			
			if(fromNode != null && toNode != null && fromNode != toNode){
				if(fromNode.adjacent.contains(new Edge(fromNode, toNode))){
					int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this edge?");
					
					if(result == 0){ // Yes delete the edge.
						System.out.println("Removing the edge between " + fromNode + " -> " + toNode);
						
						fromNode.adjacent.remove(new Edge(fromNode, toNode));
						toNode.adjacent.remove(new Edge(toNode, fromNode));
					}
					
				}else{
					System.out.println("Adding a new edge between " + fromNode + " -> " + toNode);
					
					Edge edge1 = new Edge(fromNode, toNode);
					Edge edge2 = new Edge(toNode, fromNode);
					
					fromNode.addAdjacentEdge(edge1);
					toNode.addAdjacentEdge(edge2);
				}
			}
		}
		
		repaint();
	}
	
	private Node getSelectedNode(MouseEvent mouse){
		for(int i = 0; i < listOfNodes.size(); i++){
			Node node = listOfNodes.get(i);
			
			if(node.x <= mouse.getX() && mouse.getX() <= node.x + 20 &&
			   node.y <= mouse.getY() && mouse.getY() <= node.y + 20){
				return node;
			}
		}
		return null;
	}
	
	private Edge getSelectedEdge(MouseEvent mouse){
		for(int i = 0; i < listOfNodes.size(); i++){
			Node node = listOfNodes.get(i);
			for(Edge edge : node.adjacent){
				Line2D line = edge.getRepLine();
				double ptToLine = line.ptSegDist(mouse.getX(), mouse.getY());
				
				if(ptToLine <= Constants.eps){
					System.out.println("Selected an Edge");
					return edge;
				}
			}
		}
		return null;
	}
	
	private void fillColorMap() {
		colorMap = new TreeMap<String, Color>();
		Random rand = new Random();
		for(Interval interval : sc.timeline){
			// Protocol Packet
			if(interval.packetID == -1) {
				if(!colorMap.containsKey(interval.message)) {
					int red = rand.nextInt(256);
					int green = rand.nextInt(256);
					int blue = rand.nextInt(256);
					Color c = new Color(red, green, blue);
					colorMap.put(interval.message, c);
				}
			}
		}
		for (Entry<String, Color> e : colorMap.entrySet()) {
			ui.attributesPanel.visualizationPanel.packetsColorModel.current.add(new PacketsColorRowEntry(e.getKey(), e.getValue()));
			ui.attributesPanel.visualizationPanel.packetsColorTable.repaint();
		}
		ui.attributesPanel.visualizationPanel.packetsColorModel.current.add(new PacketsColorRowEntry("WirelessPacket", Constants.SELECTED_COLOR));
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		ui.attributesPanel.machineAP.setPreferredSize(new Dimension(ui.getWidth() - ui.drawingPanel.getWidth() - 100, ui.attributesPanel.getHeight() - 100));
		ui.attributesPanel.machineAP.revalidate();
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
