/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.actions;

import java.util.Map;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerTestSupport;

/**
 * User: mprokopowicz
 * Date: Feb 9, 2006
 * Time: 1:14:50 PM
 */
public class ActionTestCase extends AbstractUnitTestCase {
   protected Action action;
   protected XPlannerTestSupport support;

   protected void setUp() throws Exception {
      super.setUp();
      setUpRepositories();
      support = new XPlannerTestSupport();
      action.setServlet(support.actionServlet);
      if(action instanceof AbstractAction){
    	  AbstractAction abstractAction = (AbstractAction)action;
    	  abstractAction.setMetaRepository(mockMetaRepository);
    	  abstractAction.setEventBus(eventBus);
      }
   }

   public void verify() {
      super.verify();
   }

   public DynaActionForm createDynaActionForm(String formName, Map propertyDefinitionMap)
         throws IllegalAccessException, InstantiationException {
      FormBeanConfig cfg = new FormBeanConfig();
      cfg.setType(DynaActionForm.class.getName());
      cfg.setName(formName);
      propertyDefinitionMap.keySet();
      for (Iterator iterator = propertyDefinitionMap.keySet().iterator(); iterator.hasNext();) {
         String propertyName = (String) iterator.next();
         String className = ((Class) propertyDefinitionMap.get(propertyName)).getName();
         cfg.addFormPropertyConfig(new FormPropertyConfig(propertyName,
                                                                                   className, null));
      }
      return
            (DynaActionForm) DynaActionFormClass.createDynaActionFormClass(cfg)
                  .newInstance();

   }
}
