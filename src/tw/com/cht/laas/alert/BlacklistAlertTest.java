package tw.com.cht.laas.alert;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import tw.com.cht.laas.entity.Syslog;

public class BlacklistAlertTest {

   @Test
   public void testCheck() {
      BlacklistAlert alert = new BlacklistAlert();
      alert.initialize();
      List<Syslog> syslogs = new ArrayList<Syslog>();
      Syslog syslog = null; 
      syslog = new Syslog("agent","sshd[13057]: Accepted password for root from 10.144.30.161 port 3574 ssh2","80","host","6","1372215719870");
      syslogs.add(syslog);
      alert.setLogs(syslogs);
      assertTrue(alert.check());
   }

}
