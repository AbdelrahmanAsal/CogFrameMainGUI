package All;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ActionMapUIResource;

import AttributePanel.PacketsColorRowEntry;
import ConfigurationMaker.ModuleMaker;
import ConfigurationMaker.NodeMaker;
import ConfigurationMaker.PrimaryMaker;
import Data.Edge;
import Data.Flow;
import Data.Interval;
import Data.Machine;
import Data.Node;
import Data.Primary;


public class DrawingPanel extends JPanel implements MouseMotionListener,
		MouseListener, ComponentListener {
	public UI ui;
	public DrawingPanel drawingPanel;
	public Edge selectedEdge;
	public Node selectedNode, cursorNode;
	public Node fromNode, toNode;
	
	public ArrayList<Node> listOfNodes;
	public int offX, offY;
	
	public JButton setSourceButton, setDestinationButton, generate;
	public JToggleButton visualizeButton;
	public StatisticsCollector sc;
	
	public ButtonGroup modes;
	public JToggleButton selectionMode, machineMode, primaryMode, edgeMode;
	
	public boolean showGridFlag, showNodesNamesFlag, showFlowsNumsFlag;
	
	public int maxRange;
	TreeMap<String, Color> colorMap;
	
	ArrayList<Flow> listOfFlows;
	ArrayList<Node> sourceNodes, destNodes;
	
	Node currentSelectedNode = null;
	Node lastSelectedSourceNode;
	int currFlowID;
	
	
	class SelectionAction extends AbstractAction {
		public SelectionAction() {}
	    
	    public void actionPerformed(ActionEvent e) {
	    	selectionMode.setSelected(true);
	    	repaint();
	    }
	}
	
	public DrawingPanel(UI ui_){
		setBorder(BorderFactory.createTitledBorder("Network topology"));
		addComponentListener(this);
		this.drawingPanel = this;
		this.ui = ui_;
		colorMap = null;
		listOfNodes = new ArrayList<Node>();
		listOfFlows = new ArrayList<Flow>();
		sourceNodes = new ArrayList<Node>();
		destNodes = new ArrayList<Node>();
		
		currFlowID = 1;
		
//		Node node1 = new Machine("1", 200, 200);
//		Node node2 = new Machine("2", 300, 300);
//		Node node3 = new Primary("3", 400, 400);
//		node1.isSource = true;
//		node2.isDestination = true;
//		
//		node1.WLS_HW.add("08:11:96:8B:84:F4");
//		node1.WLS_Name.add("wlan0");
//		node1.ETH_IP = "00:11:22:33:44:55";
//		node1.ETH_HW = "08:11:22:33:44:55";
//		
//		node2.WLS_HW.add("08:11:96:8B:84:F3");
//		node2.WLS_Name.add("wlan1");
//		node2.ETH_IP = "00:11:22:33:44:56";
//		node2.ETH_HW = "08:11:22:33:44:56";
//		
//		node3.WLS_HW.add("08:11:96:8B:84:F2");
//		node3.WLS_Name.add("wlan1");
//		node3.ETH_IP = "00:11:22:33:44:57";
//		node3.ETH_HW = "08:11:22:33:44:57";
//		
//		source = node1;
//		destination = node2;
//		
//		listOfNodes.add(node1);
//		listOfNodes.add(node2);
//		listOfNodes.add(node3);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//Editing Modes.
		JPanel editingModes = new JPanel();
		editingModes.setBorder(BorderFactory.createTitledBorder(""));
		
		selectionMode = new JToggleButton("Select");
		primaryMode = new JToggleButton("Primary");
		machineMode = new JToggleButton("Machine");
		edgeMode = new JToggleButton("Edge");
		
		InputMap keyMap = new ComponentInputMap(selectionMode);
		keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "selectionAction");

		ActionMap actionMap = new ActionMapUIResource();
		actionMap.put("selectionAction", new SelectionAction());

		SwingUtilities.replaceUIActionMap(selectionMode, actionMap);
		SwingUtilities.replaceUIInputMap(selectionMode, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);

		selectionMode.setToolTipText("Normal mode");
		selectionMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		
		machineMode.setToolTipText("Add a machine node");
		machineMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		
		primaryMode.setToolTipText("Add a primary user node");
		primaryMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		
		edgeMode.setToolTipText("To add edge between 2 nodes, drag from a node to the other one");
		edgeMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		
		modes = new ButtonGroup();
		modes.add(selectionMode);
		modes.add(machineMode);
		modes.add(primaryMode);
		modes.add(edgeMode);
		selectionMode.setSelected(true);
		
		
		editingModes.add(selectionMode);
		editingModes.add(machineMode);
		editingModes.add(primaryMode);
		editingModes.add(edgeMode);
		
		add(editingModes);
		
		JPanel experimentTools = new JPanel();
		experimentTools.setBorder(BorderFactory.createTitledBorder(""));
		
		setSourceButton = new JButton("Set Source");
		setSourceButton.setToolTipText("Set the selected node as a source node");
		
		setDestinationButton = new JButton("Set Destination");
		setDestinationButton.setToolTipText("Set the selected node as a destination node");
		
		setSourceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null && selectedNode instanceof Machine){
					sourceNodes.add(selectedNode);
					lastSelectedSourceNode = selectedNode;
					selectedNode.setSource(true);
					repaint();
				}
			}
		});
		
		experimentTools.add(setSourceButton);
		
		setDestinationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode != null && selectedNode instanceof Machine){
					destNodes.add(selectedNode);
					selectedNode.setDestination(true);
					lastSelectedSourceNode.destination = selectedNode;
					listOfFlows.add(new Flow(currFlowID, lastSelectedSourceNode, selectedNode));
					lastSelectedSourceNode.setFlowID(currFlowID);
					selectedNode.setFlowID(currFlowID);
					++currFlowID;
					repaint();
				}
			}
		});
		experimentTools.add(setDestinationButton);
		
		generate = new JButton("Generate");
		generate.setToolTipText("Generate Configuration & module files for all nodes");
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(sourceNodes.isEmpty()){
					JOptionPane.showMessageDialog(drawingPanel, "Missing source node(s).");
					return;
				}
				
				if(destNodes.isEmpty()){
					JOptionPane.showMessageDialog(drawingPanel, "Missing destination node(s).");
					return;
				}
				
				final JFileChooser fc = new JFileChooser();
				
				int ret = fc.showDialog(drawingPanel, "Choose the source template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing source template.");
					return;
				}
				String sourceTemplate = fc.getSelectedFile().getAbsolutePath();
				
				ret = fc.showDialog(drawingPanel, "Choose the hop template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing hop template.");

					return;
				}
				String hopTemplate = fc.getSelectedFile().getAbsolutePath();
				
				ret = fc.showDialog(drawingPanel, "Choose the destination template");
				if(ret != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(drawingPanel, "Missing destination template.");
					return;
				}
				String destinationTemplate = fc.getSelectedFile().getAbsolutePath();

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
//							if(node == source){//Special treatment for the source node.
							if(node.isSource){//Special treatment for the source node.
//								nodeMaker.parseTemplateFile(ui, sourceTemplate, source, destination, node, adjacentNodes);
								// #SRC is not needed any more
								// #DEST is only in source template, and is for the destination corresponding to this source
								
								nodeMaker.parseTemplateFile(ui, sourceTemplate, node.destination, node, adjacentNodes);
//							}else if(node == destination){//Special treatment for the destination node.
							}else if(node.isDestination){//Special treatment for the destination node.
//								nodeMaker.parseTemplateFile(ui, destinationTemplate, destination, node, adjacentNodes);
								
//								nodeMaker.parseTemplateFile(ui, destinationTemplate, null, node, adjacentNodes);
								nodeMaker.parseTemplateFile(ui, destinationTemplate, node, node, adjacentNodes);
							}else{//Rest of the nodes treated as hops.
//								nodeMaker.parseTemplateFile(ui, hopTemplate, null, node, adjacentNodes);
								nodeMaker.parseTemplateFile(ui, hopTemplate, node, node, adjacentNodes);
							}
						}
						
						//Generate
						moduleMaker.generateModuleFile(node, listOfNodes, ui);
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
		visualizeButton.setToolTipText("Visualize experiment and show statistics - Requires getting statistics first");
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
//					JFileChooser fc = new JFileChooser();
//					int ret = fc.showDialog(drawingPanel, String.format("Choose Node:(%s) statistics file.", node.name));
//					if(ret != JFileChooser.APPROVE_OPTION){
//						JOptionPane.showMessageDialog(drawingPanel, "Missing statistics file.");
//						visualizeButton.setSelected(false);
//						return;
//					}
					// Clear old stats
					node.totalSwitches = 0;
//					node.maxTotalSwitches = 0;
					node.averageNodalDelay = 0;
					node.averageSwitchingTime = 0;
//					node.maxAverageNodalDelay = 0;
					ui.attributesPanel.visualizationPanel.packetsColorModel.current.clear();
					ui.attributesPanel.visualizationPanel.packetsColorTable.repaint();
					
					
//					String statisticsFile = fc.getSelectedFile().getAbsolutePath();
					String statisticsFile = "Statistics_"+node.name+".txt";
					if(node instanceof Machine) {
						sc.parseMachine(statisticsFile);
					}
					else if(node instanceof Primary) 
						if(!((Primary) (node)).isVirtual)
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

		if (showGridFlag) {
			int stX = 20;
			// Vertical.
			for (int d = 0; stX + d * 20 < width - 20; d++)
				g2d.drawLine(stX + d * 20, 115, stX + d * 20, height - 55);

			// Horizontal.
			for (int d = 2; 70 + d * 20 < height - 60; d++)
				g2d.drawLine(stX - 5, 80 + d * 20, width - 25, 80 + d * 20);
		}

		//Legend box.
		
		if(!visualizeButton.isSelected()) {
	        g2d.drawString("Source", 45, height - Constants.MARGIN);
	        g2d.drawString("Destination", 175, height - Constants.MARGIN);
	        g2d.drawString("Primary", 315, height - Constants.MARGIN);
	        g2d.drawString("Selected", 440, height - Constants.MARGIN);
	        g2d.setColor(Constants.SOURCE_COLOR);
	        g2d.fillRect(15, height - 35, 25, 20);
	        g2d.setColor(Constants.DEST_COLOR);
	        g2d.fillRect(145, height - 35, 25, 20);
	        g2d.setColor(Constants.PRIMARY_COLOR);
	        g2d.fillRect(285, height - 35, 25, 20);
	        g2d.setColor(Constants.SELECTED_COLOR);
	        g2d.fillRect(410, height - 35, 25, 20);
	        
//	        repaint();
		} else {
			Font normalFont = g2d.getFont();
			g2d.setFont(new Font("" + Font.SERIF, 0, 16));
			g2d.drawString("Min.", 110, height - Constants.MARGIN);
	        g2d.drawString("Max.", 655, height - Constants.MARGIN);
	        
	        g2d.setFont(normalFont);
	        
			ArrayList<JRadioButton> vap = ui.attributesPanel.visualizationPanel.buttons;
			int x = 145, y = height - 35, w = 500, h = 20;
			GradientPaint gradientPaint = null;
			if (vap.get(0).isSelected()) { // loss ratio
				gradientPaint = new GradientPaint(
						x, y + h / 2, new Color(0,255,0), x + w, y + h / 2, new Color(255,0,0));
			} else if (vap.get(1).isSelected()) { // Nodal delay
				gradientPaint = new GradientPaint(
						x, y + h / 2, new Color(0,255,0), x + w, y + h / 2, new Color(255,0,0));
			} else if (vap.get(2).isSelected()) { // link delay
				gradientPaint = new GradientPaint(
						x, y + h / 2, new Color(0,255,0), x + w, y + h / 2, new Color(255,0,0));
			} else if (vap.get(3).isSelected()) { // No. of switches
				gradientPaint = new GradientPaint(
						x, y + h / 2, new Color(0,0,0), x + w, y + h / 2, new Color(0,0,255));
			}
			g2d.setPaint(gradientPaint);
			g2d.fillRect(x, y, w, h);
			
//			repaint();
		}
        g2d.setStroke(new BasicStroke(1));
		
		String drawingOption = visualizeButton.isSelected() ? ui.attributesPanel.visualizationPanel.visualiztionOptions.getSelection().getActionCommand() : "Init";
		
		for (Node node : listOfNodes) {
			// Draw the Node itself.
			node.draw(g2d, this, drawingOption, selectionMode.isSelected() || (node instanceof Machine ? machineMode.isSelected() : primaryMode.isSelected()));
		}
		
		for(Node node : listOfNodes){
			if(node.topologyOption.equalsIgnoreCase("Location-based"))continue;
			
			// Draw all the edges.
			for (Edge edge : node.adjacent) {
				edge.draw(g2d, this, drawingOption, selectionMode.isSelected() || edgeMode.isSelected());
			}
		}
		
		
		//if an insertion mode is activated. Draw temp node for the user.
		if(cursorNode != null)
			cursorNode.draw(g2d, drawingPanel, drawingOption, true);
		
		// Draw simulation of the packets.
		if(!drawingOption.equals("Init")){
			long currentTime = ui.attributesPanel.visualizationPanel.currentSimulationTime;
			for(Interval interval : sc.timeline){
				String intervalMessage = interval.packetID == -1 ? interval.message : "WirelessPacket";

				if(interval.fromTime <= currentTime + sc.minTimestamp && currentTime + sc.minTimestamp <= interval.toTime && canShow(intervalMessage)){
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
					
					if(interval.packetID == -1) {
						// protocol related packet
						if(colorMap == null || !colorMap.containsKey(interval.message))
							continue;
						g2d.setColor(colorMap.get(interval.message));
						g2d.fillOval(packetX + 5, packetY + 5, 10, 10);
					} else {
						g2d.setColor(colorMap.get("WirelessPacket"));
						g2d.fillOval(packetX + 5, packetY + 5, 10, 10);
					}
					
//					g2d.setColor(Color.WHITE);
					g2d.drawOval(packetX + 5, packetY + 5, 10, 10);
				}
			}
			
//			for(FallingPacketPosition p: sc.fallingPackets){
//				if(p.fromTime <= sc.maxTimestamp && p.toTime <= sc.maxTimestamp && p.fromTime <= currentTime + sc.minTimestamp && currentTime + sc.minTimestamp <= p.toTime && canShow("WirelessPacket")){
//					g2d.setColor(colorMap.get("WirelessPacket"));
//					g2d.fillOval(p.x + 5, p.y + 5, 10, 10);
//					g2d.setColor(Color.BLACK);
//					g2d.drawOval(p.x + 5, p.y + 5, 10, 10);
//				}
//			}
			
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
	public void mouseMoved(MouseEvent mouse) {
		// if an insertion mode is activated. Draw temp node for the user.
		if(machineMode.isSelected()){
			if(!(cursorNode instanceof Machine))
				cursorNode = new Machine("", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
			
			cursorNode.x = cap(mouse.getX() - 10, 70, getWidth() - 90);
			cursorNode.y = cap(mouse.getY() - 10, 120, getHeight() - 90);
		}else if(primaryMode.isSelected()){
			if(!(cursorNode instanceof Primary))
				cursorNode = new Primary("", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
			
			cursorNode.x = cap(mouse.getX() - 10, 70, getWidth() - 90);
			cursorNode.y = cap(mouse.getY() - 10, 120, getHeight() - 90);

		}else{
			cursorNode = null;
		}
		
		repaint();
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
		currentSelectedNode = getSelectedNode(mouse);
		if(selectionMode.isSelected()){
			selectedNode = getSelectedNode(mouse);
			if(ui.nodeSelection) {
				ui.nodeSubset.add(selectedNode);
//				System.out.println(selectedNode.name);
				return;
			}
			if(selectedNode != null){
				offX = mouse.getX() - selectedNode.x;
				offY = mouse.getY() - selectedNode.y;
			}else{
				selectedEdge = getSelectedEdge(mouse);
			}
		}else if(machineMode.isSelected() || primaryMode.isSelected()){
			if(getSelectedNode(mouse) == null){
//				System.out.println("Creating and adding a new node");

				Node node;
				if(machineMode.isSelected()){
					// Create a machine node.
					node = new Machine("", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
				}else { // primaryMode.isSelected == true
					// Create a primary user.
					node = new Primary("", cap(mouse.getX() - 10, 70, getWidth() - 90), cap(mouse.getY() - 10, 120, getHeight() - 90));
				}
				
				listOfNodes.add(node);
				
//				System.out.println("Successfully created the node:\n" + node);
			}
		}else if(edgeMode.isSelected()){
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
		}else if(edgeMode.isSelected()){
			toNode = getSelectedNode(mouse);
			
			if(fromNode != null && toNode != null && fromNode != toNode){
				if(!fromNode.adjacent.contains(new Edge(fromNode, toNode))){
//					System.out.println("Adding a new edge between " + fromNode + " -> " + toNode);
					
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
				System.out.println(node.name +" " + node.x + " " + node.y);
				
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
					return edge;
				}
			}
		}
		return null;
	}
	
	private void fillColorMap() {
		colorMap = new TreeMap<String, Color>();
		int i = 0;
		for(Interval interval : sc.timeline){
			// Protocol Packet
			if(interval.packetID == -1) {
				if(!colorMap.containsKey(interval.message)) {
					Color c = new Color(Integer.parseInt(Constants.colorValues[i++], 16));
					colorMap.put(interval.message, c);
				}
			}
		}
		for (Entry<String, Color> e : colorMap.entrySet()) {
			ui.attributesPanel.visualizationPanel.packetsColorModel.current.add(new PacketsColorRowEntry(e.getKey(), e.getValue(), true));
			ui.attributesPanel.visualizationPanel.packetsColorTable.repaint();
		}
		colorMap.put("WirelessPacket", new Color(Integer.parseInt(Constants.colorValues[i], 16)));
		ui.attributesPanel.visualizationPanel.packetsColorModel.current.add(new PacketsColorRowEntry("WirelessPacket", 
				new Color(Integer.parseInt(Constants.colorValues[i++], 16)), true));
		
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

	private boolean canShow(String packetName) {
		ArrayList<PacketsColorRowEntry> rows = ui.attributesPanel.visualizationPanel.packetsColorModel.current;
		for(PacketsColorRowEntry r: rows) {
			if(r.type.equals(packetName))
				return r.show;
		}
		return false;
	}
	
}
