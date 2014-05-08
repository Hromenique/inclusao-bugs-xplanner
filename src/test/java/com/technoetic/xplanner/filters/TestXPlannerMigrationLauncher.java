package com.technoetic.xplanner.filters;
import static org.easymock.EasyMock.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.easymock.MockControl;

import com.technoetic.xplanner.filters.XPlannerMigrationLauncher;
import com.technoetic.xplanner.AbstractUnitTestCase;

import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;

public class TestXPlannerMigrationLauncher extends AbstractUnitTestCase {
   public void testContextInitialized() throws Exception {
      XPlannerMigrationLauncher xplannerLauncher = new XPlannerMigrationLauncher();

      JdbcMigrationLauncher jdbcLauncher = createLocalMock(JdbcMigrationLauncher.class);
      xplannerLauncher.setLauncher(jdbcLauncher);
      
      ServletContext servletContext = createLocalMock(ServletContext.class);

      expect(jdbcLauncher.doMigrations()).andReturn(0);
      expect(servletContext.getRealPath("/WEB-INF")).andReturn("C:/WEB-INF");

      replay();

      xplannerLauncher.contextInitialized(new ServletContextEvent(servletContext));

      verify();
   }
}