package All;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import AttributePanel.AttributesPanel;
import AttributePanel.NullAttributePanel;
import Data.Edge;
import Data.Machine;
import Data.Node;
import Data.Pair;

public class UI extends JFrame{
	UI self;
	DrawingPanel drawingPanel;
	AttributesPanel attributesPanel;

	//TO BE DELETED !!!!!!!.
	MobilityOption mobilityOptionChosen = MobilityOption.STATIC; 
	TopologyOption topologyOptionChosen = TopologyOption.STATIC;
	PrimaryOption primaryOptionChosen = PrimaryOption.STATIC;
	PolicyOption PrivacyOptionChosen = PolicyOption.C1_6_11;
	
	String ccc = "Ethernet";
	
	public UI(){
		this.self = this;
		setLayout(new GridLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
		
		attributesPanel = new NullAttributePanel();
		
		drawingPanel = new DrawingPanel(this);
		drawingPanel.setBorder(BorderFactory.createTitledBorder("Network topology"));
		
		add(drawingPanel);
		add(attributesPanel);
		
		setAllMenus();
		
		setResizable(false);
		setLocationByPlatform(true);
		setSize(800, 705);
		setVisible(true);
	}
	
	private void setAllMenus() {
		//Set Menu bars.
		JMenuBar menuBar = new JMenuBar();
		
		//Mobility Options.
		JMenu fileOption = new JMenu("File");
		JMenuItem fileSave = new JMenuItem("Save");
		fileSave.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					String fileName = JOptionPane.showInputDialog(null, "Enter the name for the file");
					
					if(fileName == null){
						System.out.println("Saving cancelled");
						return;
					}
					
					PrintWriter out = new PrintWriter(new FileWriter(fileName));
					out.println(drawingPanel.listOfNodes.size());
					for (Node n : drawingPanel.listOfNodes) {
						out.println(n.name + " " + n.x + " " + n.y);
						out.println(n.ETH_IP);
						out.println(n.ETH_HW);
						out.println(n.WLS_HW.size());
						for (String w : n.WLS_HW)
							out.println(w);
						out.println(n.adjacent.size());
						for (Edge edge: n.adjacent)
							out.println(edge.to.name);
						out.println(n.channels.size());
						for (Channel c : n.channels)
							out.printf("%s %s\n", c.channel, c.probability);
						out.println(n.isSource + " " + n.isDestination);
					}
					out.close();
				} catch (Exception e) {
					e.printStackTrace();

				}
				JOptionPane.showMessageDialog(null, "File saved.");
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		
		JMenuItem fileLoad = new JMenuItem("Load");
		fileLoad.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					drawingPanel.listOfNodes.clear();
					
					String fileName = JOptionPane.showInputDialog(null, "Enter the name of the saved file.");
					
					InputReader r = new InputReader(new FileReader(fileName));
					HashMap<String, Node> map = new HashMap<String, Node>();
					ArrayList<Pair> pairs = new ArrayList<Pair>();
					int noOfNodes = r.nextInt();
					for (int i = 0; i < noOfNodes; i++) {
						String nodeName = r.next();
						int nodeX = r.nextInt();
						int nodeY = r.nextInt();
						Node node = new Machine(nodeName, nodeX, nodeY);
						map.put(nodeName, node);
						String ip = r.next();
						node.ETH_IP = ip;
						String eth_hw = r.next();
						node.ETH_HW = eth_hw;
						node.WLS_HW = new ArrayList<String>();
						int wls_hw_count = r.nextInt();
						for (int j = 0; j < wls_hw_count; j++) {
							String wls_hw = r.next();
							node.WLS_HW.add(wls_hw);
						}
						node.adjacent = new ArrayList<Edge>();
						int adjacent = r.nextInt();
						for (int j = 0; j < adjacent; j++) {
							String next = r.next();
							pairs.add(new Pair(nodeName, next));
						}
						node.channels = new ArrayList<Channel>();
						int channels = r.nextInt();
						for (int j = 0; j < channels; j++) {
							int channel = r.nextInt();
							int prob = r.nextInt();
							node.channels.add(new Channel(channel, prob));
						}
						String isSource = r.next();
						String isDest = r.next();
						if (isSource.equals("true")){
							drawingPanel.source = node;
							node.isSource = true;
						}
						if (isDest.equals("true")){
							drawingPanel.destination = node;
							node.isDestination = true;
						}
						drawingPanel.listOfNodes.add(node);
					}
					for (Pair e : pairs) {
						Edge edge = new Edge(map.get(e.n1), map.get(e.n2));
						map.get(e.n1).adjacent.add(edge);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				drawingPanel.repaint();
				JOptionPane.showMessageDialog(null, "File Loaded.");
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		JMenuItem clearButton = new JMenuItem("Clear");
		clearButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				drawingPanel.listOfNodes.clear();
				drawingPanel.repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//Just exit!.
				System.exit(0);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		fileOption.add(fileSave);
		fileOption.add(fileLoad);
		fileOption.add(clearButton);
		fileOption.add(fileExit);
		
		//Mobility Options.
		JMenu mobilityOption = new JMenu("Mobility Option");
		JMenuItem mobilityStatic = new JMenuItem("Static");
		JMenuItem mobilityCentralized = new JMenuItem("Centralized node");
		JMenuItem mobilityBroadcast = new JMenuItem("Broadcast message");
		mobilityOption.add(mobilityStatic);
		mobilityOption.add(mobilityCentralized);
		mobilityOption.add(mobilityBroadcast);
//		
//		//Topology Options.
//		JMenu topologyOption = new JMenu("Topology Option");
//		JMenuItem topologyLocation = new JMenuItem("Location-based");
//		JMenuItem topologyStatic = new JMenuItem("Static");
//		JMenuItem topologyOther = new JMenuItem("Other");
//		topologyOption.add(topologyLocation);
//		topologyOption.add(topologyStatic);
//		topologyOption.add(topologyOther);
//		
//		//Primary Options.
//		JMenu primaryOption = new JMenu("Primary Option");
//		JMenuItem primaryGaussian = new JMenuItem("Gaussian");
//		JMenuItem primaryStatic = new JMenuItem("Static");
//		JMenuItem primaryOther = new JMenuItem("Other");
//		primaryOption.add(primaryGaussian);
//		primaryOption.add(primaryStatic);
//		primaryOption.add(primaryOther);
		
		//Policy Options.
		JMenu policyOption = new JMenu("Policy Option");
		JMenuItem privacyGaussian = new JMenuItem("C1-6-11");
		JMenuItem privacyStatic = new JMenuItem("C3-6-13");
		JMenuItem privacyOther = new JMenuItem("Other");
		policyOption.add(privacyGaussian);
		policyOption.add(privacyStatic);
		policyOption.add(privacyOther);
		
		//Settings.
		JMenu settingsOption = new JMenu("Settings");
		JMenuItem ccc = new JMenuItem("CCC settings");
		ccc.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				CCCSettings c = new CCCSettings(self);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		JMenuItem help = new JMenuItem("Help");
		settingsOption.add(ccc);
		settingsOption.add(help);
		
		menuBar.add(fileOption);
		menuBar.add(mobilityOption);
//		menuBar.add(topologyOption);
//		menuBar.add(primaryOption);
		menuBar.add(policyOption);
		menuBar.add(settingsOption);
		
		
		setJMenuBar(menuBar);
	}
	
}
