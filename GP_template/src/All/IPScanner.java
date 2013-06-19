package All;

import java.net.InetAddress;
import java.util.ArrayList;

public class IPScanner {

	public IPScanner() {

	}

	public String[] getReachableIP() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 256; i++) {
			String ip = String.format("10.0.0.%d", i);
			try {
				InetAddress address = InetAddress.getByName(ip);
				if (address.isReachable(5)) {
					System.out.println(ip);
					list.add(ip);
				}
			} catch (Exception e) {

			}
		}
		String[] arr = new String[list.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
}
