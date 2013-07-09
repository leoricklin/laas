package tw.com.cht.laas.hadoop;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
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
	static final String TABLENAME = "t1" ;
	static final String CFNAME = "fam1";
   static final byte[] family = Bytes.toBytes(HbaseUtil.CFNAME);
   static final byte[] qualAgent = Bytes.toBytes(Syslog.K_AGENT);
   static final byte[] qualFacility = Bytes.toBytes(Syslog.K_FACILITY);
   static final byte[] qualBody = Bytes.toBytes(Syslog.K_BODY);
   static final byte[] qualHost = Bytes.toBytes(Syslog.K_HOST);
   static final byte[] qualSeverity = Bytes.toBytes(Syslog.K_SEVERITY);
   static final byte[] qualTS = Bytes.toBytes(Syslog.K_TMSTAMP);
   static final DateFormat tmformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

   public static boolean init() {
      boolean result = false;
      
      return result;
   }
  
   public static List<Syslog> getLog(byte[] startRow) {
      List<Syslog> resultList = null;
      Configuration config = null;
      HTable table = null;
      Scan scan = null;
      ResultScanner rs = null;
      try {
         config = HBaseConfiguration.create();
         System.out.println(tmformat.format(new Date())+ " CONNECT TABLE["+HbaseUtil.TABLENAME+"]");
         table = new HTable(config, HbaseUtil.TABLENAME);
         scan =  HbaseUtil.prepareScan();
         if (startRow != null) {
            scan.setStartRow(startRow);
         }
         System.out.println(tmformat.format(new Date())+ " START SCAN STROW["+new String(startRow)+"]");
         rs = table.getScanner(scan);
         System.out.println(tmformat.format(new Date())+ " PREPARE RESULTS");
         resultList = HbaseUtil.prepareSyslog(rs);
         System.out.println(tmformat.format(new Date())+ " DONE!");
      } catch (IOException ioe) {
         
      } finally {
         if (rs !=null ) {
            rs.close();
         }
         if (table != null) {
            try {
               table.close();
            } catch (IOException ioe) {
               
            }
         }
      }
      return resultList;
   }
	public static List<Syslog> getLog(String[] hosts, long dtStart, long dtEnd) {	
		List<Syslog> resultList = null;
		Configuration config = null;
      HTable table = null;
      Scan scan = null;
      ResultScanner rs = null;
      try {   
         config = HBaseConfiguration.create();
         System.out.println(tmformat.format(new Date())+ " CONNECT TABLE["+HbaseUtil.TABLENAME+"]");
         table = new HTable(config, HbaseUtil.TABLENAME);
         scan =  HbaseUtil.prepareScan();
         // 設定 filter for HOST, 可能有多個
         FilterList filters = null;
         String hostlist = new String();
         if(hosts != null && hosts.length > 0) {
            filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            for(String host:hosts) {
               filters.addFilter(new SingleColumnValueFilter(family,qualHost,CompareOp.EQUAL,Bytes.toBytes(host)));
               hostlist.concat(host+",");
            }
            scan.setFilter(filters);
         }
         // 設定 filter for 時間區間
         scan.setTimeRange(dtStart, dtEnd);
         System.out.println(tmformat.format(new Date())+ " START SCAN FILTERS " + family + ":" + qualHost + CompareOp.EQUAL + "[" + hostlist + "]");
         rs = table.getScanner(scan);
         System.out.println(tmformat.format(new Date())+ " PREPARE RESULTS");
         resultList = HbaseUtil.prepareSyslog(rs);
         System.out.println(tmformat.format(new Date())+ " DONE!");
      } catch(Exception ex) {
         ex.printStackTrace();
      } finally {
         if (rs !=null ) {
            rs.close();
         }
         if (table != null) {
            try {
               table.close();
            } catch (IOException ioe) {
               
            }
         }
      }
      return resultList;
	}
	
	private static Scan prepareScan() {
      Scan scan = new Scan();
      scan.addColumn(family, qualAgent).
      addColumn(family, qualBody).
      addColumn(family, qualFacility).
      addColumn(family, qualHost).
      addColumn(family, qualSeverity).
      addColumn(family, qualTS);
      return scan;
	}
	
	private static List<Syslog> prepareSyslog(ResultScanner rs) {
      List<Syslog> resultList = null;
      Syslog syslog = null;
      byte[] val = null;
      if ( rs!=null ) {
         resultList = new ArrayList<Syslog>();
         for (Result rr : rs) {
            syslog = new Syslog();
            val = rr.getRow();
            if (val != null && val.length > 0) {
               syslog.setRowkey(new String(val));
            }
            val = rr.getValue(family, qualAgent);               
            if (val != null && val.length > 0) {
               syslog.setAgent(new String(val));
            }
            val = rr.getValue(family, qualBody);
            if (val != null && val.length > 0) {
               syslog.setBody(new String(val));
            }
            val = rr.getValue(family, qualFacility);
            if (val != null && val.length > 0) {
               syslog.setFacility(new String(val));
            }
            val = rr.getValue(family, qualHost);
            if (val != null && val.length > 0) {
               syslog.setHost(new String(val));
            }
            val = rr.getValue(family, qualSeverity);
            if (val != null && val.length > 0) {
               syslog.setSeverity(new String(val));
            }
            val = rr.getValue(family, qualTS);
            if (val != null && val.length > 0) {
               syslog.setTmstamp(new String(val));
            }
            resultList.add(syslog);
         }
      }
	   return resultList;
	}
}
