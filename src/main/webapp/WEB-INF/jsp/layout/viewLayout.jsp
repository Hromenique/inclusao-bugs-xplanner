<%@ page import="com.technoetic.xplanner.tags.TilesHelper"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page errorPage="/WEB-INF/jsp/common/unexpectedError.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String beanName = (String)TilesHelper.getAttribute("beanName",pageContext);
    String titlePrefix = (String)TilesHelper.getAttribute("titlePrefix",pageContext);
    boolean isScreenMode = !"print".equals(TilesHelper.getAttribute("displayMode",pageContext));
%>
<bean:define id="object" name='<%=beanName%>' toScope="request" type="com.technoetic.xplanner.domain.DomainObject" />

<head>
    <title>XPlanner <bean:message key='<%=titlePrefix%>'/> <%=object==null?"":object.getName()%></title>
    <link rel="stylesheet" media="screen" href="<html:rewrite page="/css/reset-min.css"/>" />
    <link rel="stylesheet" media="screen" href="<html:rewrite page="/css/fonts-min.css"/>" />
    <link rel="stylesheet" media="screen" href="<html:rewrite page="/css/base.css"/>" />
	<link rel="shortcut icon"  type="image/x-icon" href="<html:rewrite page="/images/favicon2.ico"/>" />
	

<!--[if IE 7]>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page="/css/ie7.css"/>" />
<![endif]-->
    <script type="text/javascript" src="<html:rewrite page="/form.js"/>"></script>

    <!--<tab:tabConfig />-->
</head>

<body>
  <div class="page">
    <% if (isScreenMode) { %>
    <div class="page_header"><tiles:insert attribute="header"/>
    <% } %>
    <tags:breadcrumb inclusive="true" />

        <% if ("true".equals(TilesHelper.getAttribute("showTitle",pageContext))) { %>
           <!-- 
           <span class="object_name"><%=object==null?"":object.getName()%>
           [id=<%=object==null?"":""+object.getId()%>]</span> -->
        <% } %>
        
    </div>
    <html:errors/>
    <div class="page_body">
      <span class="title">
        <h1><bean:message key='<%=titlePrefix%>'/> <a href="#" >${object.name}</a></h1>
        <tiles:getAsString name="titleSuffix"/>
      </span>
      <tiles:insert attribute="progress" />
      <% if (isScreenMode) { %>
	     <span class="links"> <tiles:insert attribute="actions" /></span>
	    <%}%>
      <tiles:insert attribute="body"/>
  <% if (object != null) {
        %>
        <jsp:include page="/WEB-INF/jsp/view/notes.jsp" flush="true">
            <jsp:param name="oid" value='<%= Integer.toString(object.getId()) %>'/>
        </jsp:include>
        <%
          }
    %>
    </div>
    <%
      if (isScreenMode) {
    %>
        <jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
    <% } %>
  </div>
</body>
</html>
