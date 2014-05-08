/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.acceptance.web;

import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;

public class IterationStatusIntegrationTestScript extends AbstractDatabaseTestScript {
   public void test() throws Exception {
      Project project = newProject();
      Iteration iteration = newIteration(project);
      commitCloseAndOpenSession();

      iteration.setStatus(IterationStatus.ACTIVE);
      getSession().save(iteration);
      assertEquals(IterationStatus.ACTIVE, iteration.getStatus());
      
      commitCloseAndOpenSession();

      Iteration toIt = (Iteration) getSession().load(Iteration.class, new Integer(iteration.getId()));
      assertNotSame(iteration, toIt);
      assertEquals(IterationStatus.ACTIVE, toIt.getStatus());
   }

}