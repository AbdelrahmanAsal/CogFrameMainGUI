package All;

import java.awt.Color;

public class Constants {
	public static final int MARGIN = 20;
	public int radius;
	public static int TEXT_WIDTH = 1;
	public static int TEXT_HEIGHT = 1;
	public static int TEXT_GAP = 10;
	public static int COMPONENTS_GAP = 30;
	public static Color SELECTED_COLOR = new Color(241, 68, 0); // red
	public static Color SOURCE_COLOR = new Color(151, 229, 27); // green
	public static Color DEST_COLOR = new Color(0, 156, 255); // blue
	public static Color PRIMARY_COLOR = new Color(194, 15, 232); // purple
	public static Color HOP_COLOR = new Color(80, 80, 80); // gray
//	public static Color EDGE_COLOR = new Color(70, 130, 180); // steel blue
	public static Color EDGE_COLOR = new Color(255, 140, 0); // dark orange
	public static Color FROZEN_COLOR = Color.white; // dark orange
	
	public static String machineAPCode = "MACHINE";
	public static String primaryAPCode = "PRIMARY";
	public static String nullAPCode = "NULL";
	public static String visualizeAPCode = "VISUALIZE";
	public static String edgeAPCode = "EDGE";
	
	public static double eps = 10;
}
	
enum MobilityOption{GPS, STATIC, OTHER}
enum TopologyOption{LOCATION_BASED, STATIC, OTHER}
enum PrimaryOption{GAUSSIAN, STATIC, OTHER}
enum PolicyOption{C1_6_11, C3_6_13, OTHER}

