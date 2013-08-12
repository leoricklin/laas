package tw.com.cht.laas.web.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import tw.com.cht.laas.alert.BlacklistAlert;
import tw.com.cht.laas.alert.ConnectionAlert;
import tw.com.cht.laas.alert.IAlert;

public class WebContextListener implements ServletContextListener {

   public static final String K_ALERT_INSTANCE_LIST = "ALERT_INSTANCES";
   public static final String K_ALERT_THREAD_LIST = "ALERT_THREADS";
   public void contextDestroyed(ServletContextEvent arg0) {
      Map<String, IAlert> alerts = (Map<String, IAlert>)arg0.getServletContext().getAttribute(K_ALERT_INSTANCE_LIST);
      { // stop alerts
         if (alerts != null && !alerts.isEmpty()) {
            for(IAlert alert : alerts.values()) {
               alert.terminate();
            }
         }
      }
      // interrupt sleeping threads
      {
         Map<String,Thread> threads = (Map<String,Thread>)arg0.getServletContext().getAttribute(K_ALERT_THREAD_LIST);
         if (threads != null && !threads.isEmpty()) {
            for(Thread thread : threads.values()) {
               thread.interrupt();
            }
         }
      }
      { // check alert status
         if (alerts != null && !alerts.isEmpty()) {
            for(IAlert alert : alerts.values()) {
               while (!IAlert.STATUS.TERMINATED.equals(alert.getStatus())) {
                  try {
                     Thread.sleep(1000L);
                  } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            }
         }
      }
   }

   public void contextInitialized(ServletContextEvent arg0) {
      IAlert alert = null;
      Thread t1 = null;
      // manage alert instances
      Map<String,IAlert> alerts = (Map<String, IAlert>)arg0.getServletContext().getAttribute(K_ALERT_INSTANCE_LIST);
      if(alerts == null) {
         alerts = new HashMap<String, IAlert>();
      }
      // manage threads
      Map<String,Thread> threads = (Map<String,Thread>)arg0.getServletContext().getAttribute(K_ALERT_THREAD_LIST);
      if(threads == null) {
         threads = new HashMap<String,Thread>();
      }
      // init alert
      alert = new ConnectionAlert();
      if (alert.initialize()) {
            alerts.put(ConnectionAlert.KEY, alert);
            t1 = new Thread(alert);
            t1.start();
            threads.put(ConnectionAlert.KEY, t1);
      }
      // init alert
      alert = new BlacklistAlert();
      if (alert.initialize()) {
            alerts.put(BlacklistAlert.KEY, alert);
            t1 = new Thread(alert);
            t1.start();
            threads.put(BlacklistAlert.KEY, t1);
      }
      // persist alert instances
      arg0.getServletContext().setAttribute(K_ALERT_INSTANCE_LIST, alerts);
      // persist thread instances
      arg0.getServletContext().setAttribute(K_ALERT_THREAD_LIST, threads);
   }

}
