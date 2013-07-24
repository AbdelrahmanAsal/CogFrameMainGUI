package All;

import java.awt.Color;
import java.util.ArrayList;

public class Constants {
	public static final int MARGIN = 20;
	public int radius;
	public static int TEXT_WIDTH = 1;
	public static int TEXT_HEIGHT = 1;
	public static int TEXT_GAP = 10;
	public static int COMPONENTS_GAP = 30;
//	public static int ATTRIBUTES_PANEL_INIT_WIDTH = 500;
	public static int ATTRIBUTES_PANEL_INIT_WIDTH = 520;
	
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
	public static double EPS = 10e-5;
	
//	public static Color[] protocolPacketsColors = new Color[10];
//	protocolPacketsColors[0] = new Color();
	public static String[] colorValues = new String[] { 
        "FF0000", "00FF00", "0000FF", "FFFF00", /*"FF00FF", */"00FFFF", /*"000000", 
        "800000", "008000", "000080", "808000", */"800080", "008080", "808080", 
        "C00000", "00C000", "0000C0", "C0C000", "C000C0", "00C0C0", "C0C0C0", 
        "400000", "004000", "000040", "404000", "400040", "004040", "404040", 
        "200000", "002000", "000020", "202000", "200020", "002020", "202020", 
        "600000", "006000", "000060", "606000", "600060", "006060", "606060", 
        "A00000", "00A000", "0000A0", "A0A000", "A000A0", "00A0A0", "A0A0A0", 
        "E00000", "00E000", "0000E0", "E0E000", "E000E0", "00E0E0", "E0E0E0", 
    };
	
}
	
enum MobilityOption{GPS, STATIC, OTHER}
enum TopologyOption{LOCATION_BASED, STATIC, OTHER}
enum PrimaryOption{GAUSSIAN, STATIC, OTHER}
enum PolicyOption{C1_6_11, C3_6_13, OTHER}

