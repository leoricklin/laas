package tw.com.cht.laas.alert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import tw.com.cht.laas.entity.Syslog;
import tw.com.cht.laas.hadoop.HbaseUtil;

public class ConnectionAlert implements IAlert {
   // thread flag
   private boolean runflag = false;
   private IAlert.STATUS status = IAlert.STATUS.TERMINATED; 
   // properties
   private final String configFile = "conf/config.properties";
   private byte[] startRowKey = null;
   private int parseInterval = 30;     // 偵測間隔時間
   private String patternString = "";  // 比對字串
   private int patternMaxOccurs = 5;
   private String smsNo = "";          // 接收簡訊手機號碼
   private String isvAccount = "";     // 元件使用代號
   private String sdkSecretKey = "";   // 元件金鑰
   static final DateFormat tmformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

   public synchronized void setStatus(IAlert.STATUS status) {
      this.status = status;
   }

   public synchronized IAlert.STATUS getStatus() {
      return status;
   }

   public synchronized boolean isRunflag() {
      return runflag;
   }

   public synchronized void setRunflag(boolean runflag) {
      this.runflag = runflag;
   }

   public boolean terminate() {
      this.setRunflag(false);
      return true;
   }

   public boolean initialize() {
      this.reload();
      this.setRunflag(true);
      this.setStatus(IAlert.STATUS.INITIATED);
      return true;
   }

   public boolean reload() {
      boolean result = false;
      Properties prop = null;
      try {
         System.out.println(tmformat.format(new Date())+ " RELOAD PROPERTIES");
         prop = new Properties();
         prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile));
         this.startRowKey = (prop.getProperty("startRow", "rlog-1369664198872-d23d6ee0")).getBytes().clone();
         this.parseInterval = Integer.parseInt(prop.getProperty("interval", "30"));
         this.smsNo = new String(prop.getProperty("phone", "0919549873"));
         this.isvAccount = new String(prop.getProperty("isvAcct", "367f7deaa1ce47b185a0c91cb6d8f714"));
         this.sdkSecretKey = new String(prop.getProperty("sdkKey", "n+ABj+1w6e1Ht2A2ziBh0Q=="));
         this.patternString = new String(prop.getProperty("pattern", "password check failed for user"));
         this.patternMaxOccurs = Integer.parseInt(prop.getProperty("times", "5"));
         System.out.println(tmformat.format(new Date())+ " PATTERN STRING[" + patternString + "]");
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         prop.clear();
         prop = null;
      }
      return result;
   }
   
   public void run() {
      String strMsg = null;
      System.out.println(tmformat.format(new Date())+ " ConnAlert START");
      this.setStatus(IAlert.STATUS.RUNNING);
      while (this.isRunflag()) {
         if (check(HbaseUtil.getLog(this.startRowKey))) {
            strMsg = String.format("系統告警! 偵測到連續發生[%d]次的[%s]!", this.patternMaxOccurs, this.patternString);
            SmsUtil.sendSms(this.smsNo, this.isvAccount, this.sdkSecretKey, strMsg);
         }
         try {
            Thread.sleep(this.parseInterval * 1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      System.out.println(tmformat.format(new Date())+ " ConnAlert STOP");
      this.setStatus(IAlert.STATUS.TERMINATED);
   }
   
   public boolean check(Object obj) {
      List<Syslog> syslogs = (List<Syslog>)obj; 
      // 查log 並比對 body 是否連續出現 login fail 的錯誤
      boolean result = false;
      int matched = 0;
      Syslog syslog = null;
      Iterator<Syslog> ite = syslogs.iterator();
      while (ite.hasNext()) {
         syslog = ite.next();
         if (syslog.getBody().indexOf(this.patternString) >= 0) {
            matched++;
         }
         if (matched >= this.patternMaxOccurs) {
            result = true;
            matched = 0;
            System.out.println(tmformat.format(new Date())+ " PATTERN DETECTED");
         }
      }
      if (syslog != null && syslog.getRowkey() != null) {
         this.startRowKey = syslog.getRowkey().getBytes();
         System.out.println(tmformat.format(new Date())+ " LAST ROWKEY[" + new String(startRowKey)+"]");
      }
      return result;
   }
   
}
