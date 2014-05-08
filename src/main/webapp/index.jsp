<%@ page import="com.technoetic.xplanner.domain.Project,
                 com.technoetic.xplanner.domain.Iteration"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<db:useBeans id="projects" type="com.technoetic.xplanner.domain.Project" where="object.hidden = false" />
<bean:size id="projectCount" name="projects"/>

<logic:equal name="projectCount" value="0" >
    <logic:redirect page="/do/view/projects"/>
</logic:equal>

<logic:greaterThan name="projectCount" value="1" >
    <logic:redirect page="/do/view/projects"/>
</logic:greaterThan>

<logic:equal name="projectCount" value="1" >
    <%
        Iteration iteration = ((Project)projects.iterator().next()).getCurrentIteration();
        if (iteration != null) {
            pageContext.setAttribute("iteration", iteration);
        }
    %>
    <logic:notPresent name="iteration">
        <logic:redirect page="/do/view/projects"/>
    </logic:notPresent>
    <logic:present name="iteration">
        <logic:redirect page='<%= \"/do/view/iteration?oid=\"+iteration.getId() %>'/>
    </logic:present>
</logic:equal>
