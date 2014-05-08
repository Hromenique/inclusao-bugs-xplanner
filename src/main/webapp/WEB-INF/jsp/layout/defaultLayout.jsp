<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<head>
   <meta name="description" content="XPlanner+ is an open source, free, project planning and tracking tool for agile XP and scrum teams." />
    
   <meta name="keywords" content="XPlanner Plus, XPlanner Plus demo, XPlanner-Plus demo, XPlanner+ free demo, Open Source Scrum tool, Free Project Management tool, Free Scrum tool, XPlanner+ demo, XPlanner+ burn down chart." />
    
 
    <title><tiles:getAsString name="title"/></title>
    <link rel="stylesheet" href="<html:rewrite page="/css/reset-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/css/fonts-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/css/base.css"/>" />
	<link rel="shortcut icon"  type="image/x-icon" href="<html:rewrite page="/images/favicon2.ico"/>" />
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page="/css/ie7.css"/>" />
<![endif]-->
    <link rel="stylesheet" href="<html:rewrite page="/calendar/calendar-blue.css"/>" />
    <script type="text/javascript" src="<html:rewrite page="/toggle.js"/>"></script>
    <script type="text/javascript" src="<html:rewrite page="/form.js"/>"></script>
    <script type="text/javascript" src="<html:rewrite page="/js/nifty.js"/>"></script>
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
    <div class="page_header"><tiles:insert attribute="header"/>
        <tags:breadcrumb />
    </div>
    <div class="page_body"><tiles:insert attribute="body"/></div>
    <tiles:insert attribute="footer"/>
  </div>
</body>
</html>
