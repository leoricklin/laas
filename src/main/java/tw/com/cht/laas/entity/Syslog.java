package tw.com.cht.laas.entity;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Syslog {
	private Map<String, String> map;
	public static final String K_AGENT = "agent";
	public static final String K_BODY = "body";
	public static final String K_FACILITY = "Facility";
	public static final String K_HOST = "host";
	public static final String K_SEVERITY = "Severity";
	public static final String K_TMSTAMP = "timestamp";

	public Syslog() {
		this.map = new HashMap<String, String>();
	}
	public Syslog(String agent, String body, String facility, String host, String severity, String tmstamp){
		this();
		this.map.put(Syslog.K_AGENT, agent);
		this.map.put(Syslog.K_BODY, body);
		this.map.put(Syslog.K_FACILITY, facility);
		this.map.put(Syslog.K_HOST, host);
		this.map.put(Syslog.K_SEVERITY, severity);
		this.map.put(Syslog.K_TMSTAMP, tmstamp);		
	}

	public String getAgent() {
		return this.map.get(Syslog.K_AGENT);
	}
	public void setAgent(String agent) {
		this.map.put(Syslog.K_AGENT, agent);
	}
	public String getBody() {
		return this.map.get(Syslog.K_BODY);
	}
	public void setBody(String body) {
		this.map.put(Syslog.K_BODY, body);
	}
	public String getFacility() {
		return this.map.get(Syslog.K_FACILITY);
	}
	public void setFacility(String facility) {
		this.map.put(Syslog.K_FACILITY, facility);
	}
	public String getHost() {
		return this.map.get(Syslog.K_HOST);
	}
	public void setHost(String host) {
		this.map.put(Syslog.K_HOST, host);
	}
	public String getSeverity() {
		return this.map.get(Syslog.K_SEVERITY);
	}
	public void setSeverity(String severity) {
		this.map.put(Syslog.K_SEVERITY, severity);
	}
	public String getTmstamp() {
		return this.map.get(Syslog.K_TMSTAMP);
	}
	public void setTmstamp(String tmstamp) {
		this.map.put(Syslog.K_TMSTAMP, tmstamp);
	}
	public String toString() {
		StringBuffer stb = new StringBuffer();
		stb.append(Syslog.K_AGENT+"["+this.getAgent()+"]");
		stb.append(Syslog.K_BODY+"["+this.getBody()+"]");
		stb.append(Syslog.K_FACILITY+"["+this.getFacility()+"]");
		stb.append(Syslog.K_HOST+"["+this.getHost()+"]");
		stb.append(Syslog.K_SEVERITY+"["+this.getSeverity()+"]");
		stb.append(Syslog.K_TMSTAMP+"["+this.getTmstamp()+"]");
		return stb.toString();
	}

}
