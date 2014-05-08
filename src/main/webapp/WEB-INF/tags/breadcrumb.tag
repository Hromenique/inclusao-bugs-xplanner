<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<%@attribute name="objectType" required="false" %>
<%@attribute name="inclusive" required="false" %>
<%@attribute name="back" required="false" %>
<xplanner:navigation type="${objectType}" inclusive="${inclusive}" back="${back}"/>
<div class="breadcrumb">
	<c:forEach items="${pageScope.navigation}" var="navLink">
		<a href="${navLink.url}" >${navLink.text}</a>
	</c:forEach>
	<span class="globalActions">
	 <tiles:insert attribute="globalActions" ignore="true" />
	</span>
</div>