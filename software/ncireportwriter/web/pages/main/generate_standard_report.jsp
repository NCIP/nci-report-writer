<html>
 <body>
  <jsp:forward page="/pages/templates/template.jsp">
    <jsp:param name="content_title" value="NCI Report Writer"/>
    <jsp:param name="menu_bar" value="/pages/contents/menuBar.jsp"/>
    <jsp:param name="content_page" value="/pages/contents/_generate_standard_report.jsp"/>
  </jsp:forward>
 </body>
</html>