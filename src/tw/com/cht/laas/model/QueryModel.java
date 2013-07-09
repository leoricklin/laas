package tw.com.cht.laas.model;

import java.util.List;
import tw.com.cht.laas.entity.Syslog;
import tw.com.cht.laas.hadoop.HbaseUtil;


public class QueryModel {
	private String[] hosts;
	private long dtStart;
	private long dtEnd;
	public List<Syslog> getSyslogs() {
		return HbaseUtil.getLog(hosts, dtStart, dtEnd);
	}
	public String[] getHosts() {
		String[] outs = {"NONE"};
		if (hosts.length>0) {
			outs = hosts;
		}
		return outs;
	}
	public void setHosts(String[] hosts) {
		this.hosts = hosts;
	}
	public long getDtStart() {
		return dtStart;
	}
	public void setDtStart(long start) {
		this.dtStart = start;
	}
	public long getDtEnd() {
		return this.dtEnd;
	}
	public void setDtEnd(long end) {
		this.dtEnd = end;
	}
}
