package com.technoetic.xplanner.acceptance.security;

import java.util.List;
import java.sql.SQLException;

import com.technoetic.xplanner.db.hibernate.*;
import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.security.auth.*;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;

import net.sf.hibernate.*;

public class PermissionQueryTestScript extends AbstractDatabaseTestScript {
    private Person person;
    private String permissionTester = "permissionTester";

    public PermissionQueryTestScript(String name) {
        super(name);
    }

    protected void tearDown() throws Exception {
       try {
          deletePersonIfExists(getSession());
       }
       finally {
          super.tearDown();
       }
    }

    public void testSimplePermissionQuery() throws Exception {
        person = (Person)getObject(getSession(), Person.class, permissionTester);
        deletePersonIfExists(getSession());

        getSession().delete("from object in class " + Permission.class.getName() + " where object.name like 'test%'");
        getSession().flush();
        getSession().clear();

        int projectId = 11;
        int personId = createPersonAndPermission(projectId);

        commitSession();

       AuthorizerQueryHelper authorizerQueryHelper = new AuthorizerQueryHelper();
       authorizerQueryHelper.setHibernateTemplate(new HibernateTemplateSimulation(getSession()));
       PrincipalSpecificPermissionHelper principalSpecificPermissionHelper = new PrincipalSpecificPermissionHelper();
       principalSpecificPermissionHelper.setAuthorizerQueryHelper(authorizerQueryHelper);
       AuthorizerImpl authorizer = new AuthorizerImpl();
       authorizer.setPrincipalSpecificPermissionHelper(principalSpecificPermissionHelper);
       authorizer.setAuthorizerQueryHelper(authorizerQueryHelper);
       SystemAuthorizer.set(authorizer);
       verifyPermission(projectId, personId);

    }

    private void verifyPermission(int projectId, int personId)
        throws AuthenticationException
    {
        boolean result = false;

        // exact match
        Authorizer authorizer = SystemAuthorizer.get();
        result = authorizer.
                hasPermission(projectId, personId, "system.project", 1, "testpermission");
        assertTrue("wrong result", result);

        // no match
        result = authorizer.
                hasPermission(projectId, personId, "system.project", 10, "testpermission");
        assertFalse("wrong result", result);

        // resource type wildcard, resourceId wildcard, permission name wildcard
        // viewer role
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 10, "read.test");
        assertTrue("wrong result", result);

        // resource type wildcard, resourceId exact match, permission name wildcard
        // editor role
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 10, "read.test");
        assertTrue("wrong result", result);

        // resource type wildcard, resourceId non match, permission name wildcard
        result = authorizer.
                hasPermission(projectId, personId, "system.project.iteration.story", 11, "write.test");
        assertFalse("wrong result", result);
    }

    private int createPersonAndPermission(int projectId)
        throws HibernateException
    {
        person = new Person(permissionTester);
        person.setName(permissionTester);
        person.setEmail("permission@tester");
        person.setInitials("pt");
        int personId = ((Integer)getSession().save(person)).intValue();
        getSession().save(new RoleAssociation(projectId, personId, getRoleId(getSession(), "admin")));
        addPermission(getSession(), personId, "system.project", 1, "testpermission");
        return personId;
    }

    private void deletePersonIfExists(Session session) throws HibernateException, SQLException
    {
        if (person != null) {
            session.delete("from object in " + Person.class + " where object.name = ?",  permissionTester, Hibernate.STRING);
            session.delete("from object in " + RoleAssociation.class + " where object.personId = ?", new Integer(person.getId()), Hibernate.INTEGER);
            commitSession();
        }
    }

    private int getRoleId(Session session, String roleName) throws HibernateException {
        return ((Role)getObject(session, Role.class, roleName)).getId();

    }

    private void addPermission(Session session, int personId, String resourceType, int resourceId, String permissionName)
            throws HibernateException {
        Permission permission = new Permission(resourceType, resourceId, personId, permissionName);
        session.save(permission);
    }

    private Object getObject(Session session, Class clazz, String name) throws HibernateException {
        List objects = session.find("from object in class " + clazz.getName() + " where object.name = ?",
                name, Hibernate.STRING);
        return objects.size() > 0 ? objects.get(0) : null;
    }
}
