package tw.com.cht.laas.alert;

public interface IAlert extends Runnable {
   public enum STATUS {
      INITIATED,
      RUNNING,
      TERMINATED
   }
   public final String RUN_FLAG_ON = "true";
   public final String RUN_FLAG_OFF = "false";
   public boolean initialize();
   public boolean terminate();
   public boolean reload();
   public boolean check();
   public IAlert.STATUS getStatus();
}
