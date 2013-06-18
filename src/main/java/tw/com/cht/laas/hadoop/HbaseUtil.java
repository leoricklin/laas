package tw.com.cht.laas.hadoop;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import tw.com.cht.laas.entity.Syslog;

public class HbaseUtil {
	public static final String TABLENAME = "t1" ;
	public static final String CFNAME = "fam1";
	public static List<Syslog> getLog(String[] hosts, long dtStart, long dtEnd) throws IOException
	{	
		byte[] family = Bytes.toBytes(HbaseUtil.CFNAME);
		byte[] qualAgent = Bytes.toBytes(Syslog.K_AGENT);
		byte[] qualFacility = Bytes.toBytes(Syslog.K_FACILITY);
        byte[] qualBody = Bytes.toBytes(Syslog.K_BODY);
        byte[] qualHost = Bytes.toBytes(Syslog.K_HOST);
        byte[] qualSeverity = Bytes.toBytes(Syslog.K_SEVERITY);
        byte[] qualTS = Bytes.toBytes(Syslog.K_TMSTAMP);
        // 查詢結果
		List<Syslog> resultList = new ArrayList<Syslog>();
		// connect config
		Configuration config = HBaseConfiguration.create();
		// 設定 zookeeper 主機
		//config.set("hbase.zookeeper.quorum","0a908189.cht.local"); // TL OA		
		//config.set("hbase.zookeeper.quorum","d241288b.cht.local"); // hicloud Server
		// 設定 port
		//config.set("hbase.zookeeper.property.clientPort", "2181"); 
		// 連線到 table
		HTable table = new HTable(config, HbaseUtil.TABLENAME);
		// define scan
        Scan scan = new Scan();
        scan.addColumn(family, qualAgent).
        addColumn(family, qualFacility).
        addColumn(family, qualBody).
        addColumn(family, qualHost).
        addColumn(family, qualSeverity).
        addColumn(family, qualTS);
		System.out.println("START SCAN:table["+HbaseUtil.TABLENAME+"]family["+HbaseUtil.CFNAME+"]");
		// define filter
		FilterList filters = null;
        // 設定 filter for K_BODY
//        FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE);
//        SingleColumnValueFilter filter = new SingleColumnValueFilter(
//        	family,
//        	qualBody,
//        	CompareOp.NOT_EQUAL,
//        	Bytes.toBytes("sshd[6209]: pam_unix(sshd:session): session closed for user root")
//        	);
//        list.addFilter(filter);
//        scan.setFilter(list);
        // 設定 filter for HOST, 可能有多個
        if(hosts != null && hosts.length > 0)
        {
            filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for(String host:hosts)
            {
                SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
                    	family,
                    	qualHost,
                    	CompareOp.EQUAL,
                    	Bytes.toBytes(host)
                    	);
                filters.addFilter(filter1);        	
            }
            scan.setFilter(filters);
        }
        // 設定 filter for 時間區間
        // TODO 將傳入的時間區間轉換成 timestamp
        //scan.setTimeRange(1369811710773L, 1369811725545L);
        scan.setTimeRange(dtStart, dtEnd);
        ResultScanner rs = table.getScanner(scan);
        System.out.println("Finish Scan");
        try 
        {	
        	Syslog syslog = null;
            for (Result rr : rs) 
            {
                syslog = new Syslog();
                syslog.setAgent(new String(rr.getValue(family, qualAgent)));
                syslog.setBody(new String(rr.getValue(family, qualBody)));
                syslog.setFacility(new String(rr.getValue(family, qualFacility)));
                syslog.setHost(new String(rr.getValue(family, qualHost)));
                syslog.setSeverity(new String(rr.getValue(family, qualSeverity)));
                syslog.setTmstamp(new String(rr.getValue(family, qualTS)));
                resultList.add(syslog);
            }
        }
        catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
        finally 
        {
        	// Make sure you close your scanners when you are done!
            // Thats why we have it inside a try/finally clause
        	rs.close();
        }
        table.close();
        System.out.println("DONE!");
        return resultList;
	}
}
