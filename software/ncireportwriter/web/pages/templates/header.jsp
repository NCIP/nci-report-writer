<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String headerImagesPath = FormUtils.getImagesPath(request);
%>
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=headerImagesPath%>/logotype.gif"
      width="556" height="39" border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=headerImagesPath%>/spacer.gif"
      width="60" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank">
    <img src="<%=headerImagesPath%>/tagline_nologo.gif"
      width="219" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=headerImagesPath%>/cancer-gov.gif"
      width="125" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
</div>



