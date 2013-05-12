package All;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DynamicClassLoader extends ClassLoader{

	public Class Load(String filePath, String fileName) {
		try {
			URL myUrl = new URL("file:"+filePath);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			Class newClass =  defineClass(fileName, classData, 0, classData.length);
			return newClass;
		} catch (Exception e) {
			return null;
		} catch(NoClassDefFoundError e) {
			return null;
		}
	}
	
	

}