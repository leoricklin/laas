<%@ page language="java" import="java.util.*" pageEncoding="BIG5"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <base href="<%=basePath%>">
   <title>My JSP 'index.jsp' starting page</title>
   <meta http-equiv="pragma" content="no-cache">
   <meta http-equiv="cache-control" content="no-cache">
   <meta http-equiv="expires" content="0">    
   <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
   <meta http-equiv="description" content="This is my page">
   <meta charset="utf-8">
   <!--
   <link rel="stylesheet" type="text/css" href="styles.css">
   -->
   <link href="css/start/jquery-ui-1.10.3.custom.css" rel="stylesheet">
   <script src="js/jquery-1.9.1.js"></script>
   <script src="js/jquery-ui-1.10.3.custom.js"></script>
   <script>
   $(function() {
      
      $( "#accordion" ).accordion();
      
      $( "#button" ).button();
      $( "#radioset" ).buttonset();
      
      
      $( "#tabs" ).tabs();

      
      $( "#datepicker" ).datepicker({

      });
      $( "#selectable" ).selectable();

      
      $( "#slider" ).slider({
         range: true,
         values: [ 17, 67 ],
         slide: function( event, ui ) {
            $( "#duration" ).val(  ui.values[ 0 ] + "hr - " + ui.values[ 1 ] +"hr" );
            }
      });
      $( "#duration" ).val( $( "#slider" ).slider( "values", 0 ) +"hr - " + $( "#slider" ).slider( "values", 1 ) +"hr");
      
   });
   </script>
   <style>
   body{
      font: 80% "Trebuchet MS", sans-serif;
      margin: 50px;
   }
   select {
    height:100px;
    width:200px;
    font-size:14px;
   }
   .demoHeaders {
      margin-top: 2em;
   }
   #dialog-link {
      padding: .4em 1em .4em 20px;
      text-decoration: none;
      position: relative;
   }
   #dialog-link span.ui-icon {
      margin: 0 5px 0 0;
      position: absolute;
      left: .2em;
      top: 50%;
      margin-top: -8px;
   }
   #icons {
      margin: 0;
      padding: 0;
   }
   #icons li {
      margin: 2px;
      position: relative;
      padding: 4px 0;
      cursor: pointer;
      float: left;
      list-style: none;
   }
   #icons span.ui-icon {
      float: left;
      margin: 0 4px;
   }
   .fakewindowcontain .ui-widget-overlay {
      position: absolute;
   }
     #selectable .ui-selecting { background: #FECA40; }
     #selectable .ui-selected { background: #F39814; color: white; }
     #selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
     #selectable li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }
   </style>
  </head>
  
<body>
<!-- Tabs -->
<h2 class="demoHeaders">Tabs</h2>
<div id="tabs">
   <ul>
      <li><a href="#tabs-1">Query</a></li>
      <li><a href="#tabs-2">Hosts</a></li>
      <li><a href="#tabs-3">Dashboard</a></li>
   </ul>
   <!-- Query -->
   <div id="tabs-1">
<form method="get" action="querylog.jsp" name="queryform">
<table cellspacing="0" cellpadding="1" border="1" width="80%">
<tbody>
<tr>
<td width="40%">Select Log Type</td>
<td width="60%"><select multiple="multiple" size="1" name="logtypelist">
<option value="syslog">Syslog</option>
<option value="apachelog">Apache Web Log</option>
<option value="log4j">Log4j</option>
</select></td></tr>
<tr>
<td>Select Log Host</td>
<td><select multiple="multiple" size="1" name="loghostlist">
<option selected value="d23d6ee0">d23d6ee0</option>
<option value="d24128b7">d24128b7</option>
<option value="d2412e8f">d2412e8f</option>
</select></td></tr>
<tr>
<td valign="top">Select Time Range</td>
<td valign="top">start:<input type="text" name="dtstart" value="2013/05/27">
 end:<input type="text" value="2013/05/28" name="dtend"><input type="submit" value="submit" name="submit"></td></tr>
</tbody></table> 
</form>
   </div>
   <!-- Host -->
   <div id="tabs-2">
   Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
   </div>
   <!-- Dashboard -->
   <div id="tabs-3">
   Phasellus mattis tincidunt nibh. Cras orci urna, blandit id, pretium vel, aliquet ornare, felis. Maecenas scelerisque sem non nisl. Fusce sed lorem in enim dictum bibendum.
   </div>
</div>

</body>
</html>
