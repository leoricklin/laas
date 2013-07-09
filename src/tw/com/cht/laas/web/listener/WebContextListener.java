package tw.com.cht.laas.web.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import tw.com.cht.laas.alert.ConnectionAlert;
import tw.com.cht.laas.alert.IAlert;

public class WebContextListener implements ServletContextListener {

   public static final String K_ALERT_INSTANCE_LIST = "ALERT_INSTANCES";
   public static final String K_ALERT_THREAD_LIST = "ALERT_THREADS";
   public void contextDestroyed(ServletContextEvent arg0) {
      List<IAlert> alerts = (List<IAlert>)arg0.getServletContext().getAttribute(K_ALERT_INSTANCE_LIST);
      { // stop alerts
         if (alerts != null && !alerts.isEmpty()) {
            for(IAlert alert : alerts) {
               alert.terminate();
            }
         }
      }
      // interrupt sleeping threads
      {
         List<Thread> threads = (List<Thread>)arg0.getServletContext().getAttribute(K_ALERT_THREAD_LIST);
         if (threads != null && !threads.isEmpty()) {
            for(Thread thread : threads) {
               thread.interrupt();
            }
         }
      }
      { // check alert status
         if (alerts != null && !alerts.isEmpty()) {
            for(IAlert alert : alerts) {
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
      IAlert connAlert = new ConnectionAlert();
      if (connAlert.initialize()) {
         { // manage alert instances
            List<IAlert> alerts = (List<IAlert>)arg0.getServletContext().getAttribute(K_ALERT_INSTANCE_LIST);
            if(alerts == null) {
               alerts = new ArrayList<IAlert>();
            }
            alerts.add(connAlert);
            arg0.getServletContext().setAttribute(K_ALERT_INSTANCE_LIST, alerts);
         }
         { // manage threads
            Thread t1 = new Thread(connAlert);
            t1.start();
            List<Thread> threads = (List<Thread>)arg0.getServletContext().getAttribute(K_ALERT_THREAD_LIST);
            if(threads == null) {
               threads = new ArrayList<Thread>();
            }
            threads.add(t1);
            arg0.getServletContext().setAttribute(K_ALERT_THREAD_LIST, threads);
         }
      }
   }

}
