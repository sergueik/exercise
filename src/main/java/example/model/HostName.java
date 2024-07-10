package example.model;

import java.util.regex.Pattern;

public class HostName {
	private String hostName = null;
	private String hostPrefix = null;

	public HostName(String data) {
		hostName = data;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String data) {
		hostName = data;
	}

	public String getHostPrefix() {
		return hostPrefix;
	}

}
