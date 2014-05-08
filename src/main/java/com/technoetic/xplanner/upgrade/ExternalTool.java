/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.upgrade;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

import org.apache.log4j.Logger;

public abstract class ExternalTool {
   XPlannerProperties properties = new XPlannerProperties();

   public static boolean isCaseSensitive = false;
   public static boolean readCaseSensitiveValueFromProperties = true;
   public static boolean printLog = true;
   private static final Logger LOG = Logger.getLogger(ExternalTool.class);
   protected Session session;

   protected void setUp() throws HibernateException {
      HibernateHelper.initializeHibernate();
      session = GlobalSessionFactory.get().openSession();
      ThreadSession.set(session);
   }

   protected void tearDown() {
      try {
         if (session != null && session.isOpen())
            session.close();
      } catch (HibernateException e) {
         LOG.error("Exception closing session", e);
      }
   }

   public void run() {
      try {
         setUp();
         doRun();
      } catch (Exception e) {
         LOG.error("Exception has been thrown", e);
      } finally {
         tearDown();
      }

   }

   protected abstract void doRun() throws Exception;

}
