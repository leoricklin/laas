package tw.com.cht.laas.alert;

import java.io.File;

import cht.paas.hiair.sms.sdk.SmsSDK;

public class SmsUtil {
   public static boolean sendSms(String phone, String strIsvAccount, String strSdkSecretKey, String strMsg)  {
      boolean result = false;
      System.out.println("SEND SMS START:");
      // �o�e²�T�iĵ
      //�إ� HiNet SMS SDK ����
      SmsSDK sdk = new SmsSDK();
      try {
         //�]�w�]�t�����󱱨�A�ȹh�D���s�u���
         File fCfg = new File("config.cfg");
         if(fCfg.exists()) {  //�Y�n�۩w config.cfg �s�u�Ѽƭ�
            String strCfgFilePath = fCfg.getAbsolutePath();
           /* �t�Τ����w�ѼƬ��G
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
         // ��l��
         sdk.initIsvAccount(strIsvAccount, strSdkSecretKey);
         String strMsgid  = null;
         // ������X
         String strMsisdn = phone;
         // �w���o�T�ɶ� (���w�ťժ�ߧY�o�T�A�榡�� \"YYYYMMDDhhmm\")
         String strSchTime = null;
         // �̪����e���������� (���w 15)
         int nExpiredTime = 15;
         //�e�X SMS �ܤ@�Ө��T����
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
