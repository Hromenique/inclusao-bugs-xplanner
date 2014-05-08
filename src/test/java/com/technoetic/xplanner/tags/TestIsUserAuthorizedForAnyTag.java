package com.technoetic.xplanner.tags;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import junit.framework.TestCase;

import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.List;

public class TestIsUserAuthorizedForAnyTag extends TestCase {
    private IsUserAuthorizedForAnyTag tag;
    private XPlannerTestSupport support;
    private MockAuthorizer mockAuthorizer;
    private MockSessionFactory mockSessionFactory;

    public TestIsUserAuthorizedForAnyTag(String s) {
        super(s);
    }

    public static class MockObject {
        private ArrayList items = new ArrayList();

        public MockObject() {
            items.add(new Object());
            items.add(new Object());
        }

        public List getItems() {
            return items;
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        support.setUpSubject("user", new String[0]);
        mockAuthorizer = new MockAuthorizer();
        mockSessionFactory = new MockSessionFactory();
        mockSessionFactory.openSessionReturn = support.hibernateSession;
        GlobalSessionFactory.set(mockSessionFactory);
        SystemAuthorizer.set(mockAuthorizer);
        tag = new IsUserAuthorizedForAnyTag();
        tag.setPageContext(support.pageContext);
    }

    public void testWhenAuthorized() throws Exception {
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
        support.pageContext.setAttribute("object", new MockObject());
        tag.setName("object");
        tag.setProperty("items");
        tag.setPermissions("x,y");
        int result = tag.doStartTag();
        assertEquals("wrong tag result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testWhenAuthorizedWithCollection() throws Exception {
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        int result = tag.doStartTag();

        assertEquals("wrong tag result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testGetProjectIdFromContext() throws Exception {
        DomainContext context = new DomainContext();
        context.save(support.request);
        context.setProjectId(44);
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 44, mockAuthorizer.hasPermission2ProjectId);
    }

    public void testGetProjectIdFromResource() throws Exception {
        Project project = new Project();
        project.setId(55);
        ArrayList items = new ArrayList();
        items.add(project);
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(items);
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 55, mockAuthorizer.hasPermission2ProjectId);
    }

    public void testGetProjectIdFromRequestParameter() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"66"});
        mockAuthorizer.hasPermission2Returns = new Boolean[]{Boolean.TRUE};
        tag.setCollection(new MockObject().getItems());
        tag.setPermissions("x,y");

        tag.doStartTag();

        assertEquals("wrong project id", 66, mockAuthorizer.hasPermission2ProjectId);
    }

    public void testWhenNotAuthorized() throws Exception {
        mockAuthorizer.hasPermission2Returns = new Boolean[]{
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        support.pageContext.setAttribute("object", new MockObject());
        tag.setName("object");
        tag.setProperty("items");
        tag.setPermissions("x,y");

        int result = tag.doStartTag();

        assertEquals("wrong tag result", Tag.SKIP_BODY, result);
    }
}
