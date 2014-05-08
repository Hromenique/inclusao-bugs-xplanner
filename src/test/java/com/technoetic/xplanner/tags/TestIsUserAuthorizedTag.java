package com.technoetic.xplanner.tags;

import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.security.auth.MockAuthorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import junit.framework.TestCase;

import javax.servlet.jsp.tagext.Tag;
import java.util.Collections;

public class TestIsUserAuthorizedTag extends TestCase {
    private IsUserAuthorizedTag tag;
    private XPlannerTestSupport support;
    private MockAuthorizer mockAuthorizer;
    private MockSessionFactory mockSessionFactory;

    public TestIsUserAuthorizedTag(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        support = new XPlannerTestSupport();
        support.setUpSubject("user", new String[0]);
        mockAuthorizer = new MockAuthorizer();
        mockSessionFactory = new MockSessionFactory();
        mockSessionFactory.openSessionReturn = support.hibernateSession;
        support.hibernateSession.findReturn = Collections.EMPTY_LIST;
        GlobalSessionFactory.set(mockSessionFactory);
        SystemAuthorizer.set(mockAuthorizer);
        tag = new IsUserAuthorizedTag();
        tag.setPageContext(support.pageContext);
    }

    protected void tearDown() throws Exception {
        GlobalSessionFactory.set(null);
        SystemAuthorizer.set(null);
        super.tearDown();
    }

    public void testUserAuthorized() throws Exception {
        setUpTagWithAuthorization(Boolean.TRUE);

        int result = tag.doStartTag();

        assertTagResult(Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testUserNotAuthorizedWithAlloweUserOverride() throws Exception {
        setUpTagWithAuthorization(Boolean.FALSE);
        tag.setAllowedUser(XPlannerTestSupport.DEFAULT_PERSON_ID);

        int result = tag.doStartTag();

        assertEquals("wrong result", Tag.EVAL_BODY_INCLUDE, result);
    }

    public void testUserNotAuthorized() throws Exception {
        setUpTagWithAuthorization(Boolean.FALSE);

        int result = tag.doStartTag();

        assertTagResult(Tag.SKIP_BODY, result);
    }

    public void testUserAuthorizedForObject() throws Exception {
        Project project = setUpTagWithObjectAuthorization(Boolean.TRUE);

        int result = tag.doStartTag();

        assertTagResultForObject(Tag.EVAL_BODY_INCLUDE, result, project);
    }

    public void testUserNotAuthorizedForObject() throws Exception {
        Project project = setUpTagWithObjectAuthorization(Boolean.FALSE);

        int result = tag.doStartTag();

        assertTagResultForObject(Tag.SKIP_BODY, result, project);
    }

    public void testUserAuthorizedWithDefaultPrincipalId() throws Exception {
        mockAuthorizer.hasPermissionReturn = Boolean.TRUE;
        tag.setResourceType("system.project");
        tag.setProjectId(11);
        tag.setResourceId(2);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", XPlannerTestSupport.DEFAULT_PERSON_ID,
                mockAuthorizer.hasPermissionPersonId);
    }

    public void testUserAuthorizedWithObjectAttributeString() throws Exception {
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        Object mockObject = new UserStory();
        support.pageContext.setAttribute("x", mockObject);
        tag.setObject("x");
        tag.setProjectId(11);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", mockObject, mockAuthorizer.hasPermission2DomainObject);
    }

    public void testUserAuthorizedWhenObjectIsFromDefaultAttribute() throws Exception {
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        Object mockObject = new UserStory();
        support.pageContext.setAttribute("project", mockObject);
        tag.setProjectId(11);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", mockObject, mockAuthorizer.hasPermission2DomainObject);
    }

    public void testUserAuthorizedWhenProjectIdFromObject() throws Exception {
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        Project mockProject = new Project();
        mockProject.setId(11);
        tag.setObject(mockProject);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", 11, mockAuthorizer.hasPermission2ProjectId);
    }

    public void testUserAuthorizedWhenProjectIdFromRequestParam() throws Exception {
        support.request.setParameterValue("projectId", new String[]{"33"});
        mockAuthorizer.hasPermission2Return = Boolean.TRUE;
        UserStory mockStory = new UserStory();
        tag.setObject(mockStory);
        tag.setPermission("set");

        int result = tag.doStartTag();

        assertEquals(Tag.EVAL_BODY_INCLUDE, result);
        assertEquals("wrong param to authorizer", 33, mockAuthorizer.hasPermission2ProjectId);
    }

    private Project setUpTagWithObjectAuthorization(Boolean authorization) {
        Project project = new Project();
        project.setId(11);
        tag.setPrincipalId(1);
        tag.setObject(project);
        tag.setPermission("get");
        mockAuthorizer.hasPermission2Return = authorization;
        return project;
    }

    private void assertTagResultForObject(int expectedResult, int result, DomainObject object) {
        assertEquals("wrong result", expectedResult, result);
        assertEquals("wrong param to authorizer", 1, mockAuthorizer.hasPermission2PersonId);
        assertEquals("wrong param to authorizer", object, mockAuthorizer.hasPermission2DomainObject);
        assertEquals("wrong param to authorizer", "get", mockAuthorizer.hasPermission2Permission);
    }

    private void setUpTagWithAuthorization(Boolean authorized) {
        mockAuthorizer.hasPermissionReturn = authorized;
        tag.setProjectId(11);
        tag.setPrincipalId(1);
        tag.setResourceType("system.project");
        tag.setResourceId(2);
        tag.setPermission("set");
    }

    private void assertTagResult(int expectedResult, int result) {
        assertEquals("wrong result", expectedResult, result);
        assertEquals("wrong param to authorizer", 1, mockAuthorizer.hasPermissionPersonId);
        assertEquals("wrong param to authorizer", "system.project", mockAuthorizer.hasPermissionResourceType);
        assertEquals("wrong param to authorizer", 2, mockAuthorizer.hasPermissionResourceId);
        assertEquals("wrong param to authorizer", "set", mockAuthorizer.hasPermissionPermission);
    }
}
