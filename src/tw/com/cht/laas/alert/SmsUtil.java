package tw.com.cht.laas.alert;

import java.io.File;

import cht.paas.hiair.sms.sdk.SmsSDK;

public class SmsUtil {
   public static boolean sendSms(String phone, String strIsvAccount, String strSdkSecretKey, String strMsg)  {
      boolean result = false;
      System.out.println("SEND SMS START:");
      // 發送簡訊告警
      //建立 HiNet SMS SDK 物件
      SmsSDK sdk = new SmsSDK();
      try {
         //設定包含的元件控制服務閘道的連線資料
         File fCfg = new File("config.cfg");
         if(fCfg.exists()) {  //若要自定 config.cfg 連線參數值
            String strCfgFilePath = fCfg.getAbsolutePath();
           /* 系統之內定參數為：
            *
            * [SRVMGR_HOST]
            * REST_SERVER_PROTOCOL=http
            * REST_SERVER_IP=api.hicloud.hinet.net
            * REST_SERVER_PORT=80
            */
            SmsSDK.initConfigFilePath(strCfgFilePath);
            System.out.println("SMS Config Property Path:" + strCfgFilePath);
         } else {
            System.out.println("Using SMS Default Setting");
         }
         System.out.println("HiNet SMS Gateway URL: " + SmsSDK.getRemoteServerBaseURL());
         // 初始化
         sdk.initIsvAccount(strIsvAccount, strSdkSecretKey);
         String strMsgid  = null;
         // 手機號碼
         String strMsisdn = phone;
         // 預約發訊時間 (內定空白表立即發訊，格式為 \"YYYYMMDDhhmm\")
         String strSchTime = null;
         // 最長重送期限分鐘數 (內定 15)
         int nExpiredTime = 15;
         //送出 SMS 至一個受訊門號
         strMsgid = sdk.getSendService(strMsisdn, strMsg, strSchTime, nExpiredTime);
         if(strMsgid != null) {
            System.out.println("\nSEND SMS OK, ID[" + strMsgid + "]");
            result = true;
         } else {
            System.out.println("\nSEND SMS NG, CODE[" + sdk.getResultCode() + "], MSG[" + sdk.getErrMsg() + "]");
         }
      } catch(Exception e) {
         e.printStackTrace();
      }
      return result;
   }
}
