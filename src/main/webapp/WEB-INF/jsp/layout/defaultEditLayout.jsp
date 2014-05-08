<%@ page errorPage="/WEB-INF/jsp/common/unexpectedError.jsp"%>
<%@ page import="com.technoetic.xplanner.tags.TilesHelper"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<%
    String titlePrefix = (String)TilesHelper.getAttribute("titlePrefix",pageContext);
    String titleSuffix = (String)TilesHelper.getAttribute("titleSuffix",pageContext);
    boolean showTitleSuffix = "true".equals(TilesHelper.getAttribute("showTitleSuffix",pageContext));
    String editMessage = (String)TilesHelper.getAttribute("editMessage",pageContext);
    String objectType = (String)TilesHelper.getAttribute("objectType",pageContext);
    boolean specialNavigation = "true".equals(TilesHelper.getAttribute("specialNavigation",pageContext));
%>
<head>
    <title>XPlanner - <bean:message key='<%=titlePrefix%>'/>
        <% if (showTitleSuffix) { %>
    <%=titleSuffix%>
        <% } %>
    </title>
    <link rel="stylesheet" href="<html:rewrite page="/css/reset-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/css/fonts-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/css/base.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/calendar/calendar-blue.css"/>" />
    <link rel="shortcut icon"  type="image/x-icon" href="<html:rewrite page="/images/favicon2.ico"/>" />
<!--[if IE 7]>
<link rel="stylesheet"  href="<html:rewrite page="/css/ie7.css"/>" />
<![endif]-->
    <script type="text/javascript" src="<html:rewrite page="/js/nifty.js"/>"></script>
    <script type="text/javascript">
			window.onload=function(){
			  if(!NiftyCheck())
			      return;
			  Rounded("div#editObject","#FFFFFF","#5583AC",8,8);
			}
		</script>
    
</head>
<body>
  <div class="page">
    <div class="page_header"><tiles:insert attribute="header"/></div>
    <tags:breadcrumb inclusive="true" />
    <div class="page_body">
	    <span class="title"><h1><bean:message key='<%=editMessage%>'/></h1></span>
	    <%@ include file="/WEB-INF/jsp/common/formattingHelpJS.jsp" %>
	    <html:errors/>
      <tiles:insert attribute="body"/>
    </div>
    <jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
  </div>
</body>
</html>
