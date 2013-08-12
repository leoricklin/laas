<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.util.Map" %>
<%@ page import="tw.com.cht.laas.alert.IAlert" %>
<%@ page import="tw.com.cht.laas.alert.ConnectionAlert" %>
<%@ page import="tw.com.cht.laas.web.listener.WebContextListener" %>
<!DOCTYPE html>
<!--[if IE 7 ]>    <html class="ie7 oldie"> <![endif]-->
<!--[if IE 8 ]>    <html class="ie8 oldie"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html> <!--<![endif]-->

<head>
   <meta http-equiv="Content-Type" content="text/html; charset=BIG5">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Logging as a Service</title>

    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="css/nivo-slider.css" type="text/css" />
    <link rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" type="text/css" />

    <!--[if lt IE 9]>
       <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="js/jquery-1.6.1.min.js"><\/script>')</script>

    <script src="js/jquery.smoothscroll.js"></script>
    <script src="js/jquery.nivo.slider.pack.js"></script>
    <script src="js/jquery.easing-1.3.pack.js"></script>
    <script src="js/jquery.fancybox-1.3.4.pack.js"></script>
    <script src="js/init.js"></script>
</head>

<body>
<!-- header-wrap -->
<div id="header-wrap">
    <header>

        <hgroup>
            <h1><a href="index.html">Log as a service</a></h1>
            <h3>Log as a Service</h3>
        </hgroup>

        <nav>
            <ul>
                <li><a href="index.html">Home</a></li>
                <li><a href="service.html">Services</a></li>                
                <li><a href="explore.html">Explore Data</a></li>
                <li><a href="alertconn.jsp">DOS Attack Alert</a></li>
                <li><a href="alertblacklist.jsp">Blacklist Login Alert</a></li>
                <li><a href="dashboard.html">Dashboard</a></li>
                <li><a href="aboutus.html">About Us</a></li>
                <li><a href="#login">Login</a></li>
            </ul>
        </nav>

    </header>
</div>

<%!
   Properties prop = null;
   String configFile = "/WEB-INF/classes/conf/config.properties";
   int parseInterval = 30;
   String smsNo = "09xxxxxxxx";
   String patternString = "Failed password";
   int patternMaxOccurs = 5;
   String executeStr = IAlert.RUN_FLAG_ON;
%>
<%
   // load property
   try {
      prop = new Properties();
      prop.load(application.getResourceAsStream(configFile));
      this.parseInterval = Integer.parseInt(prop.getProperty("interval", "30")); 
      this.patternString = new String(prop.getProperty("pattern", "Failed password"));
      this.smsNo = new String(prop.getProperty("phone", "09xxxxxxxx"));
      this.patternMaxOccurs = Integer.parseInt(prop.getProperty("times", "5"));
   } catch (Exception e1) {
      e1.printStackTrace();
      prop.clear();
   }
   if(request.getParameter("action") != null) {
      // load request param
      String param = null;
      param = request.getParameter("intervalString");
      this.parseInterval = (param != null)?(Integer.parseInt(param)):(30);
      //
      param = request.getParameter("patternString");
      this.patternString = (param != null)?(param):("Failed password");
      //
      param = request.getParameter("phoneString");
      this.smsNo = (param != null)?(param):("09xxxxxxxx");
      //
      param = request.getParameter("timesString");
      this.patternMaxOccurs = (param != null)?(Integer.parseInt(param)):(5);
      //
      param = request.getParameter("executeFlag");
      this.executeStr = (param != null)?(param):(IAlert.RUN_FLAG_ON);
      // update property
      prop.setProperty("interval", String.valueOf(this.parseInterval));
      prop.setProperty("pattern", this.patternString);
      prop.setProperty("phone", this.smsNo);
      prop.setProperty("times", String.valueOf(this.patternMaxOccurs));
      prop.setProperty("executeFlag", this.executeStr);
      prop.store(new FileOutputStream(application.getRealPath(configFile)), null);
      // ask alert to reload
      IAlert alert = ((Map<String, IAlert>)application.getAttribute(WebContextListener.K_ALERT_INSTANCE_LIST)).get(ConnectionAlert.KEY);
      Thread t1 = ((Map<String,Thread>)application.getAttribute(WebContextListener.K_ALERT_THREAD_LIST)).get(ConnectionAlert.KEY);
      alert.reload();
      //
      if (IAlert.RUN_FLAG_OFF.equals(this.executeStr)) {
         // stop alert
         if (IAlert.STATUS.RUNNING == alert.getStatus()) {
            alert.terminate();
            t1.interrupt();
         }
      } else {
         // start alert
         if (IAlert.STATUS.TERMINATED == alert.getStatus()) {
            t1 = new Thread(alert); 
            t1.start();
            ((Map<String,Thread>)application.getAttribute(WebContextListener.K_ALERT_THREAD_LIST)).put(ConnectionAlert.KEY, t1);
         }
      }
   }
%>

<!-- content-wrap -->
<div class="content-wrap">


      <section id="contact" class="clearfix">

            <h1>設定您的告警條件</h1>
            <div class="primary">   
               <form method="post" action="alertconn.jsp" name="updateRule">

                    <div>
                    <label>1.Pattern
                    <input type="text" name="patternString" id="patternString" value="<%=this.patternString %>"></input>
                    </label>
                    </div>

                    <div>
                    <label>2.Interval
                    <input type="text" name="intervalString" id="intervalString" value="<%=this.parseInterval %>"></input>
                    </label>
                    </div>

                    <div>
                    <label>3.Occurrence
                    <input type="text" name="timesString" id="timesString" value="<%=this.patternMaxOccurs %>"></input>
                    </label>
                    </div>

                    <div>
                    <label>4.SMS No.
                    <input type="text" name="phoneString" id="phoneString" value="<%=this.smsNo %>"></input>
                    </label>
                    </div>

                    <div>
                    <label>5.
                     <% 
                     if (this.executeStr.equals(IAlert.RUN_FLAG_ON)) {
                     %>
                        <input type="radio" name="executeFlag" value="<%=IAlert.RUN_FLAG_ON %>" checked="checked">Start
                        <input type="radio" name="executeFlag" value="<%=IAlert.RUN_FLAG_OFF %>">Stop
                     <%
                     } else {
                     %>
                        <input type="radio" name="executeFlag" value="<%=IAlert.RUN_FLAG_ON %>">Start
                        <input type="radio" name="executeFlag" value="<%=IAlert.RUN_FLAG_OFF %>" checked="checked">Stop
                     <%
                     }
                     %>
                     </label>
                    </div>

                    <div>
                    <input type="hidden"  name="action" value="true">
                    <input type="submit"  value="Submit" class="button">
                    <input type="reset" value="Reset" class="button">
                    </div>

                </form>

            </div>

            <aside>

                    <h2>步驟說明</h2>
                    <dl >
                        <dt>1. 設定關鍵字</dt>
                        <dd>您可以設定任意關鍵字串, 若有多個關鍵字以";"隔開, 例如: Failed password;Accepted password for root</dd>
                        <dt>2. 檢查間隔時間</dt>
                        <dd>您可以設定關鍵字檢查間隔時間, 單位為秒, 例如: 300 (5分鐘)</dd>
                        <dt>3.重複出現次數</dt>
                        <dd>您可以設定間隔時間內關鍵字重複出現次數, 若超過次數就啟動告警通知</dd>
                        <dt>4.簡訊通知號碼</dt>
                        <dd>您可以設定接收告警簡訊的手機號碼</dd>
                        <dt>5.啟動/停止</dt>
                        <dd>您可以啟動或停止告警功能</dd>
                        <dt>6.送出要求</dt>
                    </ul>

            </aside>
     </section>

</div>

<!-- footer -->
<footer>
    <div class="footer-content">
        <ul class="footer-menu">
            <li><a href="#main">Home</a></li>
            <li><a href="#services">Services</a></li>
            <li><a href="#portfolio">Portfolio</a></li>
            <li><a href="#about-us">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <!-- <li class="rss-feed"><a href="#">RSS Feed</a></li> -->
        </ul>

        <p class="footer-text">Copyright chunghwa telecom. &nbsp;&nbsp;&nbsp; Designed by Team1 (leoricklin,iceC,bradcheng,yiyao,ylju)</p>
    </div>

</footer>
</body>
</html>