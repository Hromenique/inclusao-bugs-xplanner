/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.filters;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import com.tacitknowledge.util.discovery.ClassDiscoveryUtil;
import com.tacitknowledge.util.discovery.WebAppResourceListSource;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;

import com.technoetic.xplanner.upgrade.XPlannerMigrationLauncherFactory;
import com.technoetic.xplanner.util.LogUtil;

/**
 * Launches the migration process upon application context creation.  This class
 * is intentionally fail-fast, meaning that it throws a RuntimeException if any
 * problems arise during migration and will prevent the web application from
 * being fully deployed.
 * <p>
 * Below is an example of how this class can be configured in web.xml:
 * <pre>
 *   ...
 *   &lt;listener&gt;
 *       &lt;listener-class&gt;
 *           com.technoetic.xplanner.filters.XPlannerMigrationLauncher
 *       &lt;/listener-class&gt;
 *   &lt;/listener&gt;
 *   ...
 * </pre>
 *
 * @noinspection UseOfSystemOutOrSystemErr,NestedTryStatement
 */
public class XPlannerMigrationLauncher implements ServletContextListener {

   private static final Logger LOG = LogUtil.getLogger();

   private static boolean firstRun = true;
   private JdbcMigrationLauncher launcher;

   public JdbcMigrationLauncher getLauncher() throws MigrationException {
      if (launcher == null) {
         launcher = new XPlannerMigrationLauncherFactory().createMigrationLauncher();
      }
      return launcher;
   }

   public void setLauncher(JdbcMigrationLauncher launcher) {
      this.launcher = launcher;
   }


   public void contextInitialized(ServletContextEvent sce) {
      try {
         LOG.debug("Run autopatch");
         addWebInfToResourceList(sce);

         try {

            launcher = getLauncher();
//            AutopatchSupport support = new AutopatchSupport(launcher);
//            support.setUpInitialLevel();
            launcher.doMigrations();
         }
         catch (MigrationException e) {
            throw new RuntimeException("Migration exception caught during migration", e);
         }
      }
      catch (RuntimeException e) {
         LOG.error(e);
         System.out.println(e.getMessage());
         e.printStackTrace(System.out);
         System.err.println(e.getMessage());
         e.printStackTrace(System.err);

         throw e;
      }
   }

   protected void addWebInfToResourceList(ServletContextEvent sce) {
      if (firstRun) {
         ClassDiscoveryUtil.addResourceListSource(
               new WebAppResourceListSource(sce.getServletContext().getRealPath("/WEB-INF")));
      }
      firstRun = false;
   }

   public void contextDestroyed(ServletContextEvent sce) {
      launcher = null;
      LOG.debug("context is being destroyed " + sce);
   }
}
