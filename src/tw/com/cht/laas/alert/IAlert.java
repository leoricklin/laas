package tw.com.cht.laas.alert;

public interface IAlert extends Runnable {
   public enum STATUS {
      INITIATED,
      RUNNING,
      TERMINATED
   }
   public boolean initialize();
   public boolean terminate();
   public boolean reload();
   public boolean check(Object obj);
   public IAlert.STATUS getStatus();
}
