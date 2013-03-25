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

import Data.Primary;


public class PrimaryMaker extends TemplateMaker {
	public void parseTemplateFile(String templateFilePath, Primary thisNode) throws Exception {
		BufferedReader bfd = new BufferedReader(
				new FileReader(templateFilePath));
		int dotIndex = templateFilePath.lastIndexOf('.');
		String outputFilePath = templateFilePath.substring(0, dotIndex)
				+ "_intermediate" + templateFilePath.substring(dotIndex);
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFilePath));

		String s;
		while ((s = bfd.readLine()) != null) {
			out.write(s + "\n");
		}
		out.flush();
		out.close();
		bfd.close();
		templateValues = new TreeMap<String, String>();
		
		// Filling templateValues Map
		fillMap(thisNode);
		
		// Another Pass for replacement with values and formatting.
		fillFormat();
		
		replaceInTemplate(outputFilePath, thisNode);
	}
	
	private void fillMap(Primary pu) {
		templateValues.put("ACTIVE", "ACTIVE " + "\"" + pu.activeDist + "\"");
		templateValues.put("INACTIVE", "INACTIVE " + "\"" + pu.inactiveDist + "\"");
	}
	
}
