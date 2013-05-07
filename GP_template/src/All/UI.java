package All;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import AttributePanel.AttributesPanel;
import Data.Edge;
import Data.Machine;
import Data.Node;
import Data.StringPair;

public class UI extends JFrame  {
	UI self;
	DrawingPanel drawingPanel;
	AttributesPanel attributesPanel;
	JSplitPane splitPane;

	//TO BE DELETED !!!!!!!.
	MobilityOption mobilityOptionChosen = MobilityOption.STATIC; 
	TopologyOption topologyOptionChosen = TopologyOption.STATIC;
	PrimaryOption primaryOptionChosen = PrimaryOption.STATIC;
	PolicyOption PrivacyOptionChosen = PolicyOption.C1_6_11;
	String ccc = "Ethernet";
	
	public UI(){
		this.self = this;
		setLayout(new BorderLayout());
		setTitle("CogFrame");
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

		drawingPanel = new DrawingPanel(this);
		attributesPanel = new AttributesPanel(drawingPanel);
		
		add(drawingPanel, BorderLayout.CENTER);
		add(attributesPanel, BorderLayout.EAST);
		
		setAllMenus();

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				drawingPanel, attributesPanel);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		splitPane.setDividerLocation(screenSize.width - Constants.ATTRIBUTES_PANEL_INIT_WIDTH);
		
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		add(splitPane);
		
		repaint();
		setLocationByPlatform(true);
		setSize(1000, 800);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private void setAllMenus() {
		//Set Menu bars.
		JMenuBar menuBar = new JMenuBar();

		//Mobility Options.
		JMenu fileOption = new JMenu("File");
		fileOption.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem fileSave = new JMenuItem("Save");
		fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		        ActionEvent.CTRL_MASK));
		fileSave.setMnemonic(KeyEvent.VK_S);
		fileSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
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
		});

		JMenuItem fileLoad = new JMenuItem("Load");
		fileLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
		        ActionEvent.CTRL_MASK));
		fileLoad.setMnemonic(KeyEvent.VK_L);
		fileLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					drawingPanel.listOfNodes.clear();

					String fileName = JOptionPane.showInputDialog(null, "Enter the name of the saved file.");

					InputReader r = new InputReader(new FileReader(fileName));
					HashMap<String, Node> map = new HashMap<String, Node>();
					ArrayList<StringPair> pairs = new ArrayList<StringPair>();
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
							pairs.add(new StringPair(nodeName, next));
						}
						node.channels = new ArrayList<Channel>();
						int channels = r.nextInt();
						for (int j = 0; j < channels; j++) {
							int channel = r.nextInt();
							double prob = r.nextDouble();
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
					for (StringPair e : pairs) {
						Edge edge = new Edge(map.get(e.n1), map.get(e.n2));
						map.get(e.n1).adjacent.add(edge);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				drawingPanel.repaint();
				JOptionPane.showMessageDialog(null, "File Loaded.");
			}

		});
		
		JMenuItem clearButton = new JMenuItem("Clear");
		clearButton.setMnemonic(KeyEvent.VK_C);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawingPanel.listOfNodes.clear();
				drawingPanel.repaint();
			}
		});
		
		JMenuItem fileExit = new JMenuItem("Exit");
		fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
		        ActionEvent.CTRL_MASK));
		fileExit.setMnemonic(KeyEvent.VK_X);
		fileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				//Just exit!.
				System.exit(0);
			}
		});
		fileOption.add(fileSave);
		fileOption.add(fileLoad);
		fileOption.add(clearButton);
		fileOption.add(fileExit);

		//Mobility Options.
		JMenu editOption = new JMenu("Cogframe Agent");
		editOption.setMnemonic(KeyEvent.VK_E);
		JMenuItem editSetSource = new JMenuItem("Set Source");
		editSetSource.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

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

		JMenuItem editSetDestination = new JMenuItem("Set Destination");
		editSetDestination.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

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
		JMenuItem editGenerate = new JMenuItem("Generate");
		editGenerate.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
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
		JMenuItem editVisualize = new JMenuItem("Visualize");
		editVisualize.addMouseListener(new MouseListener() {
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
		
		JMenuItem editDelete = new JMenuItem("Delete");
		editDelete.setActionCommand("del");
		editDelete.setMnemonic(KeyEvent.VK_D);
		editDelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		editDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // delete NODE
				if (drawingPanel.selectionMode.isSelected()) {
					if(drawingPanel.currentSelectedNode != null) { // delete node
						int result = JOptionPane.showConfirmDialog(drawingPanel,
								"Are you sure you want to delete this node?");
						
						if(result == 0){ // Yes delete the node.
							Node toDeleteNode = drawingPanel.currentSelectedNode;
							
							drawingPanel.listOfNodes.remove(toDeleteNode);
							
							for(Node rest : drawingPanel.listOfNodes)
								rest.adjacent.remove(toDeleteNode);
							
							System.out.println("Successfully deleted the node:\n" + toDeleteNode);
							drawingPanel.currentSelectedNode = null;
						}
					}
				}
				repaint();
			}
		});
		
		JMenuItem editGetInformation= new JMenuItem("Get Information");
		editGetInformation.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Node node : drawingPanel.listOfNodes){
					Socket echoSocket = null;
					PrintWriter out = null;
					BufferedReader in = null;

					try {
						echoSocket = new Socket(node.ETH_IP, 7);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));

						System.out.println("Getting information of the node");
						String command = "GET Information\n";
						out.println(command);

						node.name = in.readLine();
						node.ETH_HW = in.readLine();
						int numberOfWireless = Integer.parseInt(in.readLine());
						node.WLS_HW.clear();
						node.WLS_Name.clear();
						for(int i = 0; i < numberOfWireless; i++){
							node.WLS_Name.add(in.readLine());
							node.WLS_HW.add(in.readLine());
						}
						Collections.reverse(node.WLS_HW);
						Collections.reverse(node.WLS_Name);
						
						out.close();
						in.close();
						echoSocket.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
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

		JMenuItem editGetStatistics= new JMenuItem("Get Statistics");
		editGetStatistics.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Node node : drawingPanel.listOfNodes){
					Socket echoSocket = null;
					PrintWriter out = null;
					BufferedReader in = null;

					try {
						echoSocket = new Socket(node.ETH_IP, 7);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));
						PrintWriter fileWriter = new PrintWriter("Statistics_" + node.name + ".txt");

						System.out.println("Getting statistics of the node");
						String command = "GET Statistics";
						out.println(command);

						while(true){
							String line = in.readLine();
							if(line == null)break;

							fileWriter.println(line);
						}

						fileWriter.close();
						out.close();
						in.close();
						echoSocket.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
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

		JMenuItem editPostConfiguration = new JMenuItem("Post Configuration");
		editPostConfiguration .addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Node node : drawingPanel.listOfNodes){
					Socket echoSocket = null;
					PrintWriter out = null;
					BufferedReader in = null;

					try {
						echoSocket = new Socket(node.ETH_IP, 7);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));
						BufferedReader fileReader = new BufferedReader(new FileReader("Configuration_" + node.name + ".click"));

						System.out.println("Posting Configuration of the node");
						String command = "Post Configuration";
						out.println(command);

						while(true){
							String line = fileReader.readLine();
							if(line == null)break;

							out.println(line);
						}
						out.println();

						fileReader.close();
						out.close();
						in.close();
						echoSocket.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
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

		JMenuItem editPostModule = new JMenuItem("Post Module");
		editPostModule.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Node node : drawingPanel.listOfNodes){
					Socket echoSocket = null;
					PrintWriter out = null;
					BufferedReader in = null;

					try {
						echoSocket = new Socket(node.ETH_IP, 7);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));
						BufferedReader fileReader = new BufferedReader(new FileReader("Module_" + node.name + ".txt"));

						System.out.println("Posting Module of the node");
						String command = "Post Module";
						out.println(command);

						while(true){
							String line = fileReader.readLine();
							if(line == null)break;

							out.println(line);
						}
						out.println();

						fileReader.close();
						out.close();
						in.close();
						echoSocket.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
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

		JMenuItem editStartExperiment = new JMenuItem("Start Experiment");
		editStartExperiment.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for(Node node : drawingPanel.listOfNodes){
					Socket echoSocket = null;
					PrintWriter out = null;
					BufferedReader in = null;

					try {
						echoSocket = new Socket(node.ETH_IP, 7);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));

						System.out.println("Start Experiment");
						String command = "Start";
						out.println(command);

						out.close();
						in.close();
						echoSocket.close();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
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

		editOption.add(editDelete);
//		editOption.addSeparator();
//		editOption.add(editSetSource);
//		editOption.add(editSetDestination);
//		editOption.add(editGenerate);
//		editOption.add(editVisualize);
		editOption.addSeparator();
		editOption.add(editGetInformation);
		editOption.add(editGetStatistics);
		editOption.add(editPostConfiguration);
		editOption.add(editPostModule);
		editOption.add(editStartExperiment);

		//Mobility Options.
		JMenu mobilityOption = new JMenu("Mobility Option");
		mobilityOption.setMnemonic(KeyEvent.VK_M);
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
		policyOption.setMnemonic(KeyEvent.VK_P);
		JMenuItem privacyGaussian = new JMenuItem("C1-6-11");
		JMenuItem privacyStatic = new JMenuItem("C3-6-13");
		JMenuItem privacyOther = new JMenuItem("Other");
		policyOption.add(privacyGaussian);
		policyOption.add(privacyStatic);
		policyOption.add(privacyOther);

		//Settings.
		JMenu settingsOption = new JMenu("Settings");
		settingsOption.setMnemonic(KeyEvent.VK_S);
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
		settingsOption.add(ccc);
		
		JMenu helpOption = new JMenu("Help");
		helpOption.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem about = new JMenuItem("About");
		about.setMnemonic(KeyEvent.VK_A);
		helpOption.add(about);

		menuBar.add(fileOption);
		menuBar.add(editOption);
		menuBar.add(mobilityOption);
//		menuBar.add(topologyOption);
//		menuBar.add(primaryOption);
		menuBar.add(policyOption);
		menuBar.add(settingsOption);
		menuBar.add(helpOption);

		setJMenuBar(menuBar);
	}
}
