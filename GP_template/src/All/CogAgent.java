package All;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import Data.Node;

public class CogAgent {
	Socket echoSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	Node node = null;
	ArrayList<Node> listOfNodes;
	String ip;
	
	public CogAgent(Node node) {
		this.node = node;
		try {
			echoSocket = new Socket(node.ETH_IP, 7);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public CogAgent(String ip, ArrayList<Node> listOfNodes) {
		try {
			this.ip = ip;
			echoSocket = new Socket(ip, 7);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
			this.listOfNodes = listOfNodes;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void setPrimaryUser(String channel, int state) {
		try {
			System.out.println("PU Function Agent");
			out.println("primaryuser "+channel+" "+state);
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getInformation() {
		try {
			System.out.println("Getting information of the node");
			String command = "GET Information\n";
			out.println(command);

			String name = in.readLine();
			if(node == null)
				node = getNodeByName(name);
			node.name = name;
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
			
			close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private Node getNodeByName(String name) {
		for(Node node:listOfNodes)
			if(node.name.equals(name)) {
				node.ETH_IP = ip;
				return node;
			}
		return null;
	}

	public void getStatistics() {
		try {
			
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
			close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void postConfiguration() {
		try {
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
			close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void postModule() {
		try {
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
			close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void startExperiment() {
		try {
			System.out.println("Start Experiment");
			String command = "Start";
			out.println(command);
			close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void stopExperiment() {
		try {
			System.out.println("Stop Experiment");
			String command = "Stop";
			out.println(command);
			close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void synch() {
		try {
			System.out.println("Synch Experiment");
			String command = "Synch";
			out.println(command);
			close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void close() throws IOException {
		out.close();
		in.close();
		echoSocket.close();
	}
}
