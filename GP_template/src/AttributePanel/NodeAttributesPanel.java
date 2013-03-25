package AttributePanel;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicBorders;

import All.Channel;
import All.Constants;
import Data.Node;


public class NodeAttributesPanel extends AttributesPanel{
	public JLabel nameLabel, ETH_IPLabel, ETH_HWLabel, WLS_IPLabel, WLS_HWLabel, channelsLabel, mobilityOptionLabel, topologyOptionLabel;
	public JTextArea name, ETH_IP, ETH_HW, WLS_IP, WLS_HW, channels;
	public JComboBox mobilityOption, topologyOption;
	public JButton setData;
	public NodeAttributesPanel(Node node){
		setLayout(new FlowLayout());
		
		nameLabel = new JLabel("Name of the Node:");
		name = new JTextArea("");
		name.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		if(e.getModifiers() > 0) channels.transferFocusBackward();
		    		else ETH_IP.transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		name.setLineWrap(true);
		name.setBorder(BorderFactory.createLoweredBevelBorder());
		
		ETH_IPLabel = new JLabel("Ethernet IP of the Node:");
		ETH_IP = new JTextArea("");
		ETH_IP.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		if(e.getModifiers() > 0) name.transferFocusBackward();
		    		else ETH_HW.transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		ETH_IP.setLineWrap(true);
		ETH_IP.setBorder(BorderFactory.createLoweredBevelBorder());
		
		ETH_HWLabel = new JLabel("Ethernet hardware of the Node:");
		ETH_HW = new JTextArea("");
		ETH_HW.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		if(e.getModifiers() > 0) ETH_IP.transferFocusBackward();
		    		else WLS_HW.transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		ETH_HW.setLineWrap(true);
		ETH_HW.setBorder(BorderFactory.createLoweredBevelBorder());
		
		WLS_IPLabel = new JLabel("Wireless IPs of the Node:");
		WLS_IP = new JTextArea("", Constants.TEXT_HEIGHT, Constants.TEXT_WIDTH);
		WLS_IP.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		System.out.println(e.getModifiers());
		    		if(e.getModifiers() > 0) transferFocusBackward();
		    		else transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		WLS_IP.setLineWrap(true);
		WLS_IP.setBorder(BorderFactory.createLoweredBevelBorder());
		
		WLS_HWLabel = new JLabel("Wireless hardware of the Node:");
		WLS_HW = new JTextArea("", Constants.TEXT_HEIGHT, Constants.TEXT_WIDTH);
		WLS_HW.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		System.out.println(e.getModifiers());
		    		if(e.getModifiers() > 0) transferFocusBackward();
		    		else transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		WLS_HW.setLineWrap(true);
		WLS_HW.setBorder(BorderFactory.createLoweredBevelBorder());
		
		channelsLabel = new JLabel("Channels/Probabilities:");
		channels = new JTextArea("");
		channels.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_TAB) {
		    		System.out.println(e.getModifiers());
		    		if(e.getModifiers() > 0) transferFocusBackward();
		    		else transferFocus();	
		    		e.consume();
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		channels.setLineWrap(true);
		channels.setBorder(BorderFactory.createLoweredBevelBorder());

		mobilityOptionLabel = new JLabel("Mobility Option");
		mobilityOption = new JComboBox(new String[]{"GPS", "Static"});
		
		topologyOptionLabel = new JLabel("Topology Option");
		topologyOption = new JComboBox(new String[]{"Location-based", "Static"});
	}
	
	protected ArrayList<String> parseSeparatedString(String input) {
		String[] bf = input.trim().toUpperCase().split("[, ]+");
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i = 0; i < bf.length; i++)
			ret.add(bf[i]);
		
		return ret;
	}
	
	protected ArrayList<Channel> parseChannels(String input) {
		String[] bf = input.trim().toUpperCase().split("[, ]+");
		
		ArrayList<Channel> ret = new ArrayList<Channel>();
		
		for(int i = 0; i < bf.length; i++){
			String[] part = bf[i].split("[:]");
			
			int channel = Integer.parseInt(part[0]);
			int probability = Integer.parseInt(part[1]);
			
			ret.add(new Channel(channel, probability));
		}
		
		return ret;
	}
}

