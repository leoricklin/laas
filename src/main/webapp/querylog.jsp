<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ page import ="java.util.*"%>
<%@ page import ="java.text.*"%>
<%@ page import ="tw.com.cht.laas.entity.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=BIG5">
   <title>Log Query</title>
   <link href="css/site_jui.css" rel="stylesheet">
   <link href="css/demo_table_jui.css" rel="stylesheet">
   <link href="css/start/jquery-ui-1.10.3.custom.css" rel="stylesheet">
   <script src="js/jquery-1.9.1.js"></script>
   <script src="js/jquery-ui-1.10.3.custom.js"></script>
   <script src="js/jquery.dataTables.js"></script>
   <script>
   $(function() {
      
      $("#sampletable").dataTable({
         "bJQueryUI": true,
         "sPaginationType": "full_numbers"
      });
   });
   </script>
</head>
<body>
<%! 
StringBuffer stb = null;
String str = null;
Date dt = null;
final DateFormat dtformat = new SimpleDateFormat("yyyy/MM/dd");
final DateFormat tmformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<jsp:useBean id="model" class="tw.com.cht.laas.model.QueryModel" scope="page"/>
<% 
try {

   model.setDtStart(dtformat.parse(request.getParameter("dtstart")).getTime());
   model.setDtEnd(dtformat.parse(request.getParameter("dtend")).getTime());
   model.setHosts(request.getParameterValues("loghostlist"));
} catch (ParseException e) {
   e.printStackTrace();
}
%>
<%
dt = new Date();
dt.setTime(model.getDtStart());
%>
<input type="hidden" value="<%=dtformat.format(dt) %>>" name="dtStart"><br>
<%
dt.setTime(model.getDtEnd());
%>
<input type="hidden" value="<%=dtformat.format(dt) %>>" name="dtEnd"><br>
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
<div class="full_width" >
<table id="sampletable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%" >
    <thead>
        <tr>
            <th>Timestamp</th>
            <th>Host</th>
            <th>Facility</th>
            <th>Severity</th>
            <th>Body</th>
        </tr>
    </thead>
<%
if (syslogs.size()>0) {
%>
    <tbody>
<%
   for(Syslog syslog:syslogs) {
      dt.setTime(Long.parseLong(syslog.getTmstamp()));
%>
        <tr>
            <td><%=tmformat.format(dt) %></td>
            <td><%=syslog.getHost() %></td>
            <td><%=syslog.getFacility() %></td>
            <td><%=syslog.getSeverity() %></td>
            <td><%=syslog.getBody() %></td>
        </tr>
<%      
   }
}
%>
</table>
</div>

</body>
</html>