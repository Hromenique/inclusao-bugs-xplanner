/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.auth;
/**
 * User: mprokopowicz
 * Date: Mar 30, 2006
 * Time: 4:19:03 PM
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springframework.orm.hibernate.HibernateTemplate;
import junit.framework.TestCase;

import com.technoetic.xplanner.domain.Project;

public class TestAuthorizerQueryHelper extends TestCase {
   private AuthorizerQueryHelper authorizerQueryHelper;
   private MockControl mockHibernateTemplateControl;
   HibernateTemplate mockHibernateTemplate;
   private Permission permission1;
   private Permission permission2;

   protected void setUp() throws Exception {
      super.setUp();
      permission1 = new Permission();
      permission2 = new Permission();
      permission1.setId(1);
      permission2.setId(2);
      authorizerQueryHelper = new AuthorizerQueryHelper();
      mockHibernateTemplateControl = MockClassControl.createControl(HibernateTemplate.class);
      mockHibernateTemplate = (HibernateTemplate) mockHibernateTemplateControl.getMock();
      authorizerQueryHelper.setHibernateTemplate(mockHibernateTemplate);
   }

   public void testGetAllPermissions() throws Exception {
      Collection permissionCol = new ArrayList();
      permissionCol.add(permission1);
      mockHibernateTemplateControl.expectAndReturn(
            mockHibernateTemplate.findByNamedQuery("security.personal.permissions"), permissionCol);
      mockHibernateTemplateControl.expectAndReturn(
            mockHibernateTemplate.findByNamedQuery("security.role.permissions"),
            Arrays.asList(new Object[]{permission2}));
      mockHibernateTemplateControl.replay();
      Collection expectedPermissionCol = authorizerQueryHelper.getAllPermissions();
      mockHibernateTemplateControl.verify();
      assertEquals(2, expectedPermissionCol.size());
   }

   public void testGetAllPermissionsToPerson() throws Exception {
      mockHibernateTemplateControl.expectAndReturn(mockHibernateTemplate.findByNamedQuery("security.person.permissions"),
                                                   Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getAllPermissionsToPerson();
      mockHibernateTemplateControl.verify();
   }

   public void testGetRolesForPrincipalOnProject() throws Exception {
      mockHibernateTemplate.findByNamedQueryAndNamedParam("security.roles",
                                                          new String[]{
                                                                "personId",
                                                                "projectId",
                                                                "includeWildcardProject"},
                                                          new Object[]{new Integer(
                                                                1),
                                                                       new Integer(
                                                                             2),
                                                                       new Integer(
                                                                             1)});
      mockHibernateTemplateControl.setMatcher(MockControl.ARRAY_MATCHER);
      mockHibernateTemplateControl.setReturnValue(Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getRolesForPrincipalOnProject(1, 2, true);
      mockHibernateTemplateControl.verify();
   }

   public void testGetAllUnhidenProjects() throws Exception {
      mockHibernateTemplate.find("from project in " + Project.class + " where project.hidden = false");
      mockHibernateTemplateControl.setReturnValue(Collections.EMPTY_LIST);
      mockHibernateTemplateControl.replay();
      authorizerQueryHelper.getAllUnhidenProjects();
      mockHibernateTemplateControl.verify();
   }
}