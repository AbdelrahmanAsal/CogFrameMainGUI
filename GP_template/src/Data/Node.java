package Data;


import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.TreeMap;

import All.Channel;
import All.DrawingPanel;


public abstract class Node{
	public int x, y;
	public boolean isSource, isDestination;
	public String name, ETH_IP, ETH_HW, mobilityOption, topologyOption;
	public ArrayList<String> WLS_HW;
	public ArrayList<String> WLS_IP;
	public ArrayList<Edge> adjacent;
	public ArrayList<Channel> channels;
	
	//Additional data to be added.
	public static double maxAverageNodalDelay; 
	public double averageSwitchingTime, averageNodalDelay;
	public int totalSwitches;
	public static int maxTotalSwitches = 0;
	public TreeMap<Integer, PacketInfo> inPackets, outPackets;
	public Node(String name, int x, int y){
		this.x = x;
		this.y = y;
		isSource = isDestination = false;
		
		this.name = name;
		
		this.ETH_IP = "10.0.0.21";
		this.ETH_HW = "11:12:13:14:15:16";
		this.WLS_HW = new ArrayList<String>();
		WLS_HW.add("11:12:13:14:15:16");
		WLS_HW.add("11:12:13:14:15:19");
		this.WLS_IP = new ArrayList<String>();
		WLS_IP.add("192.168.1.1");
		WLS_IP.add("192.168.1.2");
		
		this.adjacent = new ArrayList<Edge>();
		this.channels = new ArrayList<Channel>();
		channels.add(new Channel(1, 0.1));
		channels.add(new Channel(6, 0.2));
		channels.add(new Channel(11, 0.3));
		
		mobilityOption = "Static";
		topologyOption = "Static";
		
		inPackets = new TreeMap<Integer, PacketInfo>();
		outPackets = new TreeMap<Integer, PacketInfo>();
	}
	
	public String getIP(){
		return ETH_IP.trim();
	}
	
	public String getETH_HW(int format, int type){
		try{
			ETH_HW = ETH_HW.toUpperCase().trim();
			
			if(format == 0){
				return ETH_HW;
			}else if(format == 1){
				String[] bf = ETH_HW.split("[:]");
				
				String ret = "";
				for(int i = 0; i < bf.length; i += 2)
					if(i == 0)ret += (i + type * 6) + "/" + bf[i] + bf[i + 1];
					else ret += " " + (i + type * 6) + "/" + bf[i] + bf[i + 1];
				
				return ret;
			}else return "N/A";
		}catch(Exception ex){//Already showing N/A.
			return "N/A";
		}
	} 
	
	public String getWLS_IP(){
		String ret = "";
		for(int i = 0; i < WLS_IP.size(); i++){
			if(i == 0)ret += WLS_IP.get(i);
			else ret += "," + WLS_IP.get(i);
		}
		return ret;
	}
	
	public String getWLS_HW(int format, int type){
		String ret = "";
		for(int i = 0; i < WLS_HW.size(); i++){
			if(i == 0)ret += getWLS_HW(format, type, i);
			else ret += "," + getWLS_HW(format, type, i);
		}
		return ret;
	}
	
	public String getWLS_HW(int format, int type, int index){
		try{
			WLS_HW.set(index, WLS_HW.get(index).toUpperCase().trim());
			
			if(format == 0){
				return WLS_HW.get(index);
			}else if(format == 1){
				String[] bf = WLS_HW.get(index).split("[:]");
				
				String ret = "";
				for(int i = 0; i < bf.length; i += 2)
					if(i == 0)ret += (i + type * 6) + "/" + bf[i] + bf[i + 1];
					else ret += " " + (i + type * 6) + "/" + bf[i] + bf[i + 1];
				
				return ret;
			}else return "N/A";
		}catch(Exception ex){//Already showing N/A.
			return "N/A";
		}
	}
	
	public String getChannels(){
		String ret = "";
		
		boolean first = true;
		for(Channel channel: channels){
			if(!first)ret += ", ";
			first = false;
			
			ret += channel;
		}
		
		return ret;
	}
	
	public void addAdjacentEdge(Edge edge){
		adjacent.add(edge);
	}
	
	public String toString(){
		String ret = String.format("Name: \"%s\"\n", name);
		ret += "{\n";
		ret += String.format("    IP: \"%s\"\n", ETH_IP);
		ret += String.format("    ETH_HW: F1 = \"%s\", F2_A = \"%s\", F2_B = \"%s\"\n", getETH_HW(0, -1), getETH_HW(1, 0), getETH_HW(1, 1));
		for(int i = 0; i < WLS_HW.size(); i++)
			ret += String.format("    WLS_HW_%2d: F1 = \"%s\", F2_A = \"%s\", F2_B = \"%s\"\n", i, getWLS_HW(0, -1, i), getWLS_HW(1, 0, i), getWLS_HW(1, 1, i));
		ret += String.format("    Location: X = \"%d\", Y = \"%d\"\n", x, y);
		ret += "}\n";
		
		return ret;
	}
	
	public abstract void draw(Graphics2D g2d, DrawingPanel drawingPanel, String drawingOption);
	
	public String type(){
		return "N/A";
	}
}
