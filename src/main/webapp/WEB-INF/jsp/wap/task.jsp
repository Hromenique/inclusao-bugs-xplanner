<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page import="java.text.NumberFormat,
                 java.text.DecimalFormat"%>
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBean id="task" type="com.technoetic.xplanner.domain.Task"
	oid='<%= new Integer(request.getParameter("oid")) %>' />
<db:useBean id="story" type="com.technoetic.xplanner.domain.UserStory" 
	oid='<%= new Integer(task.getStory().getId()) %>' />
<db:useBean id="iteration" type="com.technoetic.xplanner.domain.Iteration" 
	oid='<%= new Integer(story.getIterationId()) %>' />
<%
	DecimalFormat formatter = new DecimalFormat("0.0");
%>
<wml>
<card id="details" title="Task Details">
<do type="accept" label="Story">
  <go href="/xplanner/do/mobile/view/story?oid=<%=task.getStory().getId()%>"/>
</do>
<do type="accept" label="Iteration">
  <go href="/xplanner/do/mobile/view/iteration?oid=<%=story.getIterationId()%>"/>
</do>
<do type="accept" label="Project">
  <go href="/xplanner/do/mobile/view/project?oid=<%=iteration.getProjectId()%>"/>
</do>
<do type="accept" label="All Projects">
  <go href="/xplanner/do/mobile/view/projects"/>
</do>
<p>
<b>Name:</b> <bean:write name="task" property="name"/><br/>
<b>Status:</b> <%= task.isCompleted() ? "Complete" : "Open" %><br/>
<b>Act./Est.:</b> <%= formatter.format(task.getActualHours()) %>/<%= formatter.format(task.getEstimatedHours()) %><br/>
<logic:greaterThan name="task" property="acceptorId" value="0" >
<db:useBean id="acceptor" type="com.technoetic.xplanner.domain.Person" oid='<%= new Integer(task.getAcceptorId()) %>'/>
<b>Acceptor:</b> <a href="/xplanner/do/mobile/view/person?oid=<%= acceptor.getId()%>"><bean:write name="acceptor" property="initials"/></a><br/>
</logic:greaterThan>
<logic:equal name="task" property="acceptorId" value="0">
<b>Acceptor:</b> None<br/>
</logic:equal>
<b>Description:</b> <bean:write name="task" property="description"/><br/>
</p>
</card>
</wml>