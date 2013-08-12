<%@ page language="java" contentType="text/html; charset=big5" pageEncoding="big5"%>
<%@ page import ="java.util.*"%>
<%@ page import ="java.text.*"%>
<%@ page import ="tw.com.cht.laas.entity.*"%>
<%@ page import ="tw.com.cht.laas.model.*"%>
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
   <link rel="stylesheet" href="css/dataTables.css" type="text/css" media="screen" />

    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script src="js/jquery-1.6.1.min.js"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script>
        $(document).ready(function(){
            $("#sampletable").dataTable({"sPaginationType": "full_numbers"});
        });
    </script>
</head>

<body>
<%! 
QueryModel model = null;
StringBuffer stb = null;
String str = null;
Date dt = null;
final DateFormat tmformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<% 
model = new QueryModel();
try {
   model.setDtStart(tmformat.parse(request.getParameter("dtstart")+" 00:00:00").getTime());
   model.setDtEnd(tmformat.parse(request.getParameter("dtend")+" 23:59:59").getTime());
   model.setHosts(request.getParameterValues("loghostlist"));
} catch (ParseException e) {
   e.printStackTrace();
}
%>
<%
dt = new Date();
dt.setTime(model.getDtStart());
%>
<input type="hidden" value="<%=tmformat.format(dt) %>>" name="dtStart"><br>
<%
dt.setTime(model.getDtEnd());
%>
<input type="hidden" value="<%=tmformat.format(dt) %>>" name="dtEnd"><br>
<%
stb = new StringBuffer();
for(String host:model.getHosts()) {
   stb.append(host+",");
}
%>
<input type="hidden" value="<%= stb.toString() %>>" name="hosts"><br>
<%
List<Syslog> syslogs = model.getSyslogs();
%>
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

<!-- content-wrap -->
<div class="content-wrap">
        <div class="row no-bottom-margin">
            <section class="col">
                <h2>查詢結果</h2>
                <table id="sampletable" border="0" cellpadding="0" cellspacing="0" class="pretty" style="{font: 80%/1.45em Arial, Verdana, Helvetica, sans-serif;}">
                    <thead>
                        <tr>
                            <th style="width:150px;">Timestamp</th>
                            <th>Host</th>
                            <th>Facility</th>
                            <th>Severity</th>
                            <th>Body</th>
                        </tr>
                    </thead>
<%
if (syslogs !=null && syslogs.size()>0) {
%>
                    <tbody>
<%
   for(Syslog syslog:syslogs) {
      dt.setTime(Long.parseLong(syslog.getTmstamp()));
%>
        <tr>
            <td style="width:150px;"><%=tmformat.format(dt) %></td>
            <td><%=(syslog.getHost()!=null)?(syslog.getHost()):("") %></td>
            <td><%=(syslog.getFacility()!=null)?(syslog.getFacility()):("") %></td>
            <td><%=(syslog.getSeverity()!=null)?(syslog.getSeverity()):("") %></td>
            <td style="text-align: left;"><%=syslog.getBody() %></td>
        </tr>
<%      
   }
}
%>
                </table>
            </section>
        </div>
        <a class="back-to-top" href="explore.html">Back to Query</a>
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
