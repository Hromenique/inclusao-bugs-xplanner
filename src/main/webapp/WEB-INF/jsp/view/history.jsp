<%@ page contentType = "text/html;charset=UTF-8"%>
 <%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>
<%@ page import="com.technoetic.xplanner.actions.AbstractAction,
                 com.technoetic.xplanner.history.HistorySupport,
                 com.technoetic.xplanner.history.HistoricalEvent,
                 org.apache.commons.beanutils.PropertyUtils,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.views.HistoryPage"%>

<xplanner:content>
<xplanner:contentTitle titleKey="history"/>
<%--<xplanner:isUserInRole role="admin">--%>
<%--</xplanner:isUserInRole>--%>

<h1><span class="title">
<bean:message key="history"/>:</span>
<span class="object_name"><bean:write name="domainContext" property="targetObject.name"/></span>
</h1>
<%
pageContext.setAttribute(HistoryPage.EVENT_TABLE_ID,
                         HistorySupport.getEvents(Integer.parseInt(request.getParameter("oid"))));
request.setAttribute(HistoryPage.EVENT_TABLE_ID,
                     HistorySupport.getEvents(Integer.parseInt(request.getParameter("oid"))));
%>


<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
 
 <bean:size id="historicalEventCount" name='<%=HistoryPage.EVENT_TABLE_ID%>'/>
 <logic:greaterThan name="historicalEventCount" value="0">
 <span class="title">
 <h1> <bean:message key="history.title"/> </h1> </span>
    <dt:table uid='<%=HistoryPage.EVENT_TABLE_ID%>' htmlId='<%=HistoryPage.EVENT_TABLE_ID%>' id="event"
              name='<%=HistoryPage.EVENT_TABLE_ID%>' styleClass="objecttable" requestURI="">
        <% HistoricalEvent historicalEvent = (HistoricalEvent)event;
           pageContext.setAttribute("historicalEvent", historicalEvent); %>

        <db:useBean id="who" type="com.technoetic.xplanner.domain.Person"
                oid='<%= new Integer(historicalEvent.getPersonId()) %>'/>


        <dt:setProperty name="paging.banner.placement" value="bottom" />
        <dt:column sortable="true" sortProperty="action" title='<%= messages.getMessage("history.tableheading.action") %>'>
          <bean:write name="historicalEvent" property="action"/>
        </dt:column>
        <dt:column title='<%= messages.getMessage("history.tableheading.by") %>'>
          <xplanner:link page="/do/view/person" paramId="oid" paramName="who" paramProperty="id">
            ${who.name}
          </xplanner:link>
        </dt:column>
        <dt:column sortable="true" sortProperty="when" title='<%= messages.getMessage("history.tableheading.when") %>'>
          <xplanner:formatDate name="historicalEvent" property="when"/>
        </dt:column>
        <dt:column sortable="true" sortProperty="description" title='<%= messages.getMessage("history.tableheading.description") %>'>
           <xplanner:twiki name="historicalEvent" property="description"/>
        </dt:column>
    </dt:table>
</logic:greaterThan>

<%
    pageContext.setAttribute(HistoryPage.CONTAINER_EVENT_TABLE_ID,
                             HistorySupport.getContainerEvents(Integer.parseInt(request.getParameter("oid"))));
    request.setAttribute(HistoryPage.CONTAINER_EVENT_TABLE_ID,
                         HistorySupport.getContainerEvents(Integer.parseInt(request.getParameter("oid"))));
%>

<bean:size id="containerEventCount" name='<%=HistoryPage.CONTAINER_EVENT_TABLE_ID%>' />
<logic:greaterThan name="containerEventCount" value="0">
  <span class="title"><h1><bean:message key="history.container.title"/></h1></span>
    <dt:table uid='<%=HistoryPage.CONTAINER_EVENT_TABLE_ID%>' htmlId='<%=HistoryPage.CONTAINER_EVENT_TABLE_ID%>'
              id="event" name='<%=HistoryPage.CONTAINER_EVENT_TABLE_ID%>' styleClass="objecttable" requestURI="" >
        <% final HistoricalEvent historicalEvent = (HistoricalEvent)event;
           pageContext.setAttribute("historicalEvent", historicalEvent); %>

        <db:useBean id="who" type="com.technoetic.xplanner.domain.Person"
                oid='<%= new Integer(historicalEvent.getPersonId()) %>'/>

        <% Object historicalObject = HistorySupport.getHistoricalObject(historicalEvent);
          if (historicalObject != null) {
             pageContext.setAttribute("historicalObject", historicalObject );
          } else {
             pageContext.removeAttribute("historicalObject");
          }
          %>


        <dt:setProperty name="paging.banner.placement" value="bottom" />
        <dt:column sortable="true" sortProperty="action" title='<%= messages.getMessage("history.tableheading.action") %>'>
          <bean:write name="historicalEvent" property="action"/>
        </dt:column>
        <dt:column title='<%= messages.getMessage("history.tableheading.by") %>'>
          <xplanner:link page="/do/view/person" paramId="oid" paramName="who" paramProperty="id">
            ${who.name}
          </xplanner:link>
        </dt:column>
        <dt:column sortable="true" sortProperty="when" title='<%= messages.getMessage("history.tableheading.when") %>'>
          <xplanner:formatDate name="historicalEvent" property="when"/>
        </dt:column>
        <dt:column sortable="true" sortProperty="objectType" title='<%= messages.getMessage("history.tableheading.type") %>'>
          <bean:write name="historicalEvent" property="objectType"/>
        </dt:column>
        <dt:column title='<%= messages.getMessage("history.tableheading.name") %>'>
          <logic:present name="historicalObject">
            <html:link page='<%= "/do/view/"+historicalEvent.getObjectType()%>'
              paramId="oid" paramName="historicalObject" paramProperty="id" >
                <% if (PropertyUtils.getPropertyDescriptor(historicalObject, "name") != null) { %>
                  <bean:write name="historicalObject" property="name"/>
                  <%}%>
            </html:link>
          </logic:present>
          <logic:notPresent name="historicalObject">
            <xplanner:twiki name="historicalEvent" property="description"/>
          </logic:notPresent>
        </dt:column>
   </dt:table>
  
</logic:greaterThan>
</xplanner:content>
