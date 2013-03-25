package ConfigurationMaker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import Data.Node;


public class TemplateMaker {

	Map<String, String> templateValues = new TreeMap<String, String>();
	ArrayList<String> formatList1 = new ArrayList<String>();
	ArrayList<String> formatList2 = new ArrayList<String>();
	
	protected void fillFormat() throws IOException {
		// Lists used; order matters.
		formatList1 = new ArrayList<String>();
		formatList2 = new ArrayList<String>();
		formatList1.add("::");
		formatList2.add(" :: ");
		formatList1.add("->");
		formatList2.add(" -> ");
		formatList1.add("[ ]*,");
		formatList2.add(", ");
		formatList1.add("[ ]+");
		formatList2.add(" ");
	}


	protected void replaceInTemplate(String templateFilePath, Node thisNode) throws IOException {
		BufferedReader bfd = new BufferedReader(
				new FileReader(templateFilePath));
		int dotIndex = templateFilePath.lastIndexOf('.');
		String outputFilePath = templateFilePath.substring(0,
				templateFilePath.indexOf("_inter"))
				+ "_out_" + thisNode.name + templateFilePath.substring(dotIndex);
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFilePath));
		System.out.println("Starting replacement by values..");
		String s;
		while ((s = bfd.readLine()) != null) {
			for (Entry<String, String> en : templateValues.entrySet()) {
				s = s.replace("#" + en.getKey(), en.getValue());
			}
			for (int i = 0; i < formatList1.size(); ++i) {
				s = s.replaceAll(formatList1.get(i), formatList2.get(i));
			}
			out.write(s + "\n");
		}
		System.out.println("Ended replacement phase successfully!");
		bfd.close();
		out.close();
	}
}