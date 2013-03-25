package ConfigurationMaker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import Data.Machine;
import Data.Node;

/**
 * 
 * Rules to write a module in the file:
 * ------------------------------------
 *	1. Start the module by #module_start in a single line.
 *	2. End the module by #module_end in a single line.
 *  3. The first line of the module has to be on format:
 *  	identifier :: ModuleName( parameters )
 *  4. The next lines contain all the corresponding outputs for the parameters. Note
 *  That the number of outputs has to be equal to the number of the parameters.
 *  Also, the outputs indices has to be in order, i.e. if we have 2 parameters
 *  then the output lines have to be:
 *  identifier[0] -> ...
 *  identifier[1] -> ...
 *  only and in the same order
 *  5. Any output can be distributed in many lines, i.e.
 *  identifier[0] -> ethC
 *                -> Discard;
 *
 */
public class NodeMaker extends TemplateMaker {


	static final String MODULE_PREFIX = "#module_start";
	static final String MODULE_SUFFIX = "#module_end";
	static final String COMMENT_SIGN = "//";
	static final String BROADCAST_HW = "ff:ff:ff:ff:ff:ff";

	private enum F2addressType {
		A, B
	};
	
	// This Map has to contain ALL variables names with their values,
	// e.g.(SRC_IP, 172.128.16.5)

	private String getF2Address(String mac, F2addressType type) {
		// ************************************ for testing only to handle N/A
//		mac = "00:27:10:a0:f6:b4"; // mac = BROADCAST_HW;

		String sp[] = mac.split(":");
		String ret = "";
		if (type.equals(F2addressType.A)) {
			ret = "0/" + sp[0] + sp[1] + " 2/" + sp[2] + sp[3] + " 4/" + sp[4]
					+ sp[5];
		} else {
			ret = "6/" + sp[0] + sp[1] + " 8/" + sp[2] + sp[3] + " 10/" + sp[4]
					+ sp[5];
		}
		return ret;
	}

	private void fillMap(Node src, Node dest, Node thisNode, Node[] hops) {
		// BROADCAST
		templateValues.put("BROADCAST_F1", BROADCAST_HW);
		templateValues.put("BROADCAST_F2_A",
				getF2Address(BROADCAST_HW, F2addressType.A));
		templateValues.put("BROADCAST_F2_B",
				getF2Address(BROADCAST_HW, F2addressType.B));

		// SRC
		templateValues.put("SRC_IP", src.ETH_IP);
		templateValues.put("SRC_ETH_HW_F1", src.ETH_HW);
		templateValues.put("SRC_ETH_HW_F2_A",
				getF2Address(src.ETH_HW, F2addressType.A));
		templateValues.put("SRC_ETH_HW_F2_B",
				getF2Address(src.ETH_HW, F2addressType.B));
		// 1-indexed
		for (int id = 1; id <= src.WLS_HW.size(); ++id) {
			templateValues.put("SRC_WLS_HW_" + id + "_F1",
					src.WLS_HW.get(id - 1));
			templateValues.put("SRC_WLS_HW_" + id + "_F2_A",
					getF2Address(src.WLS_HW.get(id - 1), F2addressType.A));
			templateValues.put("SRC_WLS_HW_" + id + "_F2_B",
					getF2Address(src.WLS_HW.get(id - 1), F2addressType.B));
		}
		// DEST
		templateValues.put("DEST_IP", dest.ETH_IP);
		templateValues.put("DEST_ETH_HW_F1", dest.ETH_HW);
		templateValues.put("DEST_ETH_HW_F2_A",
				getF2Address(dest.ETH_HW, F2addressType.A));
		templateValues.put("DEST_ETH_HW_F2_B",
				getF2Address(dest.ETH_HW, F2addressType.B));
		// 1-indexed
		for (int id = 1; id <= dest.WLS_HW.size(); ++id) {
			templateValues.put("DEST_WLS_HW_" + id + "_F1",
					dest.WLS_HW.get(id - 1));
			templateValues.put("DEST_WLS_HW_" + id + "_F2_A",
					getF2Address(dest.WLS_HW.get(id - 1), F2addressType.A));
			templateValues.put("DEST_WLS_HW_" + id + "_F2_B",
					getF2Address(dest.WLS_HW.get(id - 1), F2addressType.B));
		}

		// Hops
		for (int i = 0; i < hops.length; ++i) {
			templateValues.put("HOPS_IP" + "_hopID:" + i, hops[i].ETH_IP);
			templateValues
					.put("HOPS_ETH_HW_F1" + "_hopID:" + i, hops[i].ETH_HW);
			templateValues.put("HOPS_ETH_HW_F2_A" + "_hopID:" + i,
					getF2Address(hops[i].ETH_HW, F2addressType.A));
			templateValues.put("HOPS_ETH_HW_F2_B" + "_hopID:" + i,
					getF2Address(hops[i].ETH_HW, F2addressType.B));

			// 1-indexed
			for (int id = 1; id <= hops[i].WLS_HW.size(); ++id) {
				templateValues.put("HOPS_WLS_HW_" + id + "_F1" + "_hopID:" + i,
						hops[i].WLS_HW.get(id - 1));
				templateValues.put(
						"HOPS_WLS_HW_" + id + "_F2_A" + "_hopID:" + i,
						getF2Address(hops[i].WLS_HW.get(id - 1),
								F2addressType.A));
				templateValues.put(
						"HOPS_WLS_HW_" + id + "_F2_B" + "_hopID:" + i,
						getF2Address(hops[i].WLS_HW.get(id - 1),
								F2addressType.B));
			}
		}
		
		// THIS
		templateValues.put("THIS_IP", thisNode.ETH_IP);
		templateValues.put("THIS_ETH_HW_F1", thisNode.ETH_HW);
		templateValues.put("THIS_ETH_HW_F2_A",
				getF2Address(thisNode.ETH_HW, F2addressType.A));
		templateValues.put("THIS_ETH_HW_F2_B",
				getF2Address(thisNode.ETH_HW, F2addressType.B));
		// 1-indexed
		for (int id = 1; id <= thisNode.WLS_HW.size(); ++id) {
			templateValues.put("THIS_WLS_HW_" + id + "_F1",
					thisNode.WLS_HW.get(id - 1));
			templateValues.put("THIS_WLS_HW_" + id + "_F2_A",
					getF2Address(thisNode.WLS_HW.get(id - 1), F2addressType.A));
			templateValues.put("THIS_WLS_HW_" + id + "_F2_B",
					getF2Address(thisNode.WLS_HW.get(id - 1), F2addressType.B));
		}

		// TODO: PU_ID
	}

	// TODO: err handling for files that are not in the required format.
	public void parseTemplateFile(String templateFilePath, Node src, Node dest,
			Node thisNode, Node[] hops) throws Exception {
		BufferedReader bfd = new BufferedReader(
				new FileReader(templateFilePath));
		int dotIndex = templateFilePath.lastIndexOf('.');
		String outputFilePath = templateFilePath.substring(0, dotIndex)
				+ "_intermediate" + templateFilePath.substring(dotIndex);
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFilePath));

		String s;
		while ((s = bfd.readLine()) != null) {
			if (s.trim().equals(MODULE_PREFIX)) {
				s = bfd.readLine();
				while (s != null && !s.trim().equals(MODULE_SUFFIX)) {
					s = s.trim();
					// if empty line
					if (s.length() == 0) {
						s = bfd.readLine();
						continue;
					}
					// declaration line
					if (s.contains("::")) {
						// for handling case like: c2 :: Classifier() instead of
						// c2::Classifier()
						s = s.replaceAll("[ ]*::[ ]*", "::");

						// If the user wants to try a module without hops..
						if (!s.contains("#HOPS")) {
							out.write(s + "\n");
							s = bfd.readLine();
							while (s != null && !s.trim().equals(MODULE_SUFFIX)) {
								out.write(s + "\n");
								s = bfd.readLine();
							}
						} else {
							// c2
							String moduleIdentifier = s.substring(0,
									s.indexOf(":"));
							// Classifier
							String moduleName = s.substring(s.indexOf(":") + 2,
									s.indexOf("("));
							// like 111,111,#HOPS_IP,111
							String paramStr = s.substring(s.indexOf("(") + 1,
									s.indexOf(")"));
							String[] params = paramStr.split(",");
							ArrayList<String> newParams = new ArrayList<String>();
							for (int i = 0; i < params.length; ++i) {
								params[i] = params[i].trim();
								if (params[i].contains("#HOPS")) {
									int stHopIndex = params[i].indexOf("#HOPS"), k;
									int endHopIndex = params[i]
											.indexOf("#HOPS") + 3;
									// if(#HOPS_x) was followed by space then by
									// sth like #SRC_IP
									for (k = endHopIndex; k < params[i]
											.length(); ++k) {
										if (params[i].charAt(k) == ' ')
											break;
									}
									endHopIndex = k;
									String hopsStr = params[i].substring(
											stHopIndex, endHopIndex);
									String newParam = "";
									// J is the index of the hop in the Node[]
									// hops
									for (int j = 0; j < hops.length; ++j) {
										// test mwdooo3 substring(endHopIndex)
										newParam = params[i].substring(0,
												stHopIndex)
												+ hopsStr
												+ "_hopID:"
												+ j
												+ " "
												+ params[i]
														.substring(endHopIndex);
										newParams.add(newParam);
									}
								} else {
									newParams.add(params[i]);
								}
							}
							out.write(moduleIdentifier + "::" + moduleName);
							out.write("(");
							for (int i = 0; i < newParams.size() - 1; ++i) {
								out.write(newParams.get(i).trim() + ", ");
							}
							out.write(newParams.get(newParams.size() - 1)
									+ ")\n");

							ArrayList<String> mOutputsLinesL = new ArrayList<String>();
							s = bfd.readLine();
							while (s != null && !s.trim().equals(MODULE_SUFFIX)) {
								s = s.replace(" ", "");
								mOutputsLinesL.add(s);
								s = bfd.readLine();
							}
							for (int i = 0; i < mOutputsLinesL.size();) {
								String newStr = mOutputsLinesL.get(i);
								int tmp = i;
								while (i + 1 < mOutputsLinesL.size()
										&& !mOutputsLinesL.get(i + 1)
												.startsWith(moduleIdentifier)) {
									// not a comment
									if (!mOutputsLinesL.get(i + 1).startsWith(
											COMMENT_SIGN))
										newStr += mOutputsLinesL.get(i + 1);
									++i;
								}
								if (i == tmp)
									++i;
								mOutputsLinesL.remove(tmp);
								mOutputsLinesL.add(tmp, newStr);
							}
							for (int i = 0; i < mOutputsLinesL.size();) {
								if (!mOutputsLinesL.get(i).startsWith(
										moduleIdentifier)) {
									mOutputsLinesL.remove(i);
								} else
									++i;
							}
							String[] mOutputsLines = new String[mOutputsLinesL
									.size()];
							for (int i = 0; i < mOutputsLines.length; ++i) {
								mOutputsLines[i] = mOutputsLinesL.get(i);
							}
							int curIndex = 0;
							for (int i = 0; i < mOutputsLines.length; ++i) {
								if (mOutputsLines[i]
										.startsWith(moduleIdentifier)) {
									if (params[i].contains("#HOPS")) {
										for (int j = 0; j < hops.length; ++j) {
											int remInd = mOutputsLines[i]
													.indexOf("->");
											String remaining = mOutputsLines[i]
													.substring(remInd);
											out.write(moduleIdentifier + "["
													+ curIndex + "]"
													+ remaining + "\n");
											++curIndex;
										}
									} else {
										int remInd = mOutputsLines[i]
												.indexOf("->");
										String remaining = mOutputsLines[i]
												.substring(remInd);
										out.write(moduleIdentifier + "["
												+ curIndex + "]" + remaining
												+ "\n");
										++curIndex;
									}
								}
							}
						}
					} else {
						s = bfd.readLine();
					}
				}
			} else {
				out.write(s + "\n");
			}
		}
		out.flush();
		out.close();
		bfd.close();
		templateValues = new TreeMap<String, String>();
		// Filling templateValues Map
		fillMap(src, dest, thisNode, hops);
		// Another Pass for replacement with values and formatting.
		fillFormat();
		replaceInTemplate(outputFilePath, thisNode);
	}

	public void run() throws Exception {
		// Topology
		Node src = new Machine("SRC", 1, 2);
		src.name = "SRC";
		src.ETH_IP = "src_ip_192.172.1.1";
		src.ETH_HW = "00:26:9e:30:ac:a7";
		src.WLS_HW = new ArrayList<String>();
		src.WLS_HW.add("00:26:9e:30:ac:a9");

		Node dest = new Machine("DEST", 2, 3);
		dest.name = "DEST";
		dest.ETH_IP = "dest_ip_192.172.1.1";
		Node thisNode = new Machine("THIS", 2, 3);
		thisNode.name = "THISNO";
		thisNode.ETH_IP = "this_ip_192.172.1.1";
		Node hop1 = new Machine("hop1", 2, 3);
		hop1.name = "hop1";
		hop1.ETH_IP = "hop1_ip_192.172.1.1";
		Node hop2 = new Machine("hop2", 2, 3);
		hop2.name = "hop2";
		hop2.ETH_IP = "hop2_ip_192.172.1.1";

		Node hop3 = new Machine("hop2", 2, 3);
		hop3.name = "hop3";
		hop3.ETH_IP = "hop3_ip_192.172.1.1";

		// Node hops[] = new Node[]{hop1, hop2, hop3};
		Node hops[] = new Node[] { hop1 };
		hops[0].WLS_HW = new ArrayList<String>();
		hops[0].WLS_HW.add("00:26:9e:30:ac:a5");
		hops[0].WLS_HW.add("00:26:9e:30:ac:a6");

		// Method to call.
		String templateFilePath = "/media/D/ClickWork/launch_main.click";
		parseTemplateFile(templateFilePath, src, dest, thisNode, hops);
	}

	public static void main(String[] args) throws Exception {
		new NodeMaker().run();
	}

}
