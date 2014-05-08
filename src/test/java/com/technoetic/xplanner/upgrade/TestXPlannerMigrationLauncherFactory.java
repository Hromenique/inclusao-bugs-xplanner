/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.upgrade;
/**
 * Created by IntelliJ IDEA.
 * User: sg0620641
 * Date: Jan 20, 2006
 * Time: 11:54:22 AM
 * To change this template use File | Settings | File Templates.
 */

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.DatabaseType;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.util.NonPooledDataSource;
import junit.framework.TestCase;

import com.technoetic.xplanner.XPlannerProperties;

public class TestXPlannerMigrationLauncherFactory extends TestCase {
   private XPlannerMigrationLauncherFactory factory;
   private JdbcMigrationLauncher mockLauncher;
   private MockControl mockLauncherControl;
   private XPlannerProperties properties;

   protected void setUp() throws Exception {
      super.setUp();
      properties = new XPlannerProperties();
      XPlannerProperties.getProperties();
      factory = new XPlannerMigrationLauncherFactory();
      mockLauncherControl = MockClassControl.createControl(JdbcMigrationLauncher.class);
      mockLauncher = (JdbcMigrationLauncher) mockLauncherControl.getMock();
   }

   public void testConfigureFromXPlannerProperties() throws Exception {
      DataSourceMigrationContext context = new DataSourceMigrationContext();
      NonPooledDataSource dataSource = new NonPooledDataSource();
      dataSource.setDatabaseUrl(properties.getProperty(XPlannerProperties.CONNECTION_URL_KEY));
      dataSource.setDriverClass(properties.getProperty(XPlannerProperties.DRIVER_CLASS_KEY));
      dataSource.setUsername(properties.getProperty(XPlannerProperties.CONNECTION_USERNAME_KEY));
      dataSource.setPassword(properties.getProperty(XPlannerProperties.CONNECTION_PASSWORD_KEY));
      context.setDatabaseType(
            new DatabaseType(properties.getProperty(XPlannerProperties.PATCH_DATABASE_TYPE_KEY)));
      context.setSystemName(XPlannerMigrationLauncherFactory.SYSTEM_NAME);
      context.setDataSource(dataSource);
      mockLauncher.setPatchPath(properties.getProperty(XPlannerProperties.PATCH_PATH_KEY));
      mockLauncher.setJdbcMigrationContext(context);
      mockLauncherControl.setMatcher(new JdbcMigrationContextMatcher());
      mockLauncherControl.replay();
      factory.configureFromXPlannerProperties(mockLauncher);
      mockLauncherControl.verify();
   }

   private class JdbcMigrationContextMatcher implements ArgumentsMatcher {
      public boolean matches(Object[] expected, Object[] actual) {
         DataSourceMigrationContext expectedContext = (DataSourceMigrationContext) expected[0];
         DataSourceMigrationContext actualContext = (DataSourceMigrationContext) actual[0];
         NonPooledDataSource expectedDataSource = (NonPooledDataSource) expectedContext.getDataSource();
         NonPooledDataSource actualDataSource = (NonPooledDataSource) actualContext.getDataSource();
         boolean dataSourcesMatch =
               StringUtils.equals(expectedDataSource.getDatabaseUrl(), actualDataSource.getDatabaseUrl()) &&
               StringUtils.equals(expectedDataSource.getDriverClass(), actualDataSource.getDriverClass()) &&
               StringUtils.equals(expectedDataSource.getUsername(), actualDataSource.getUsername()) &&
               StringUtils.equals(expectedDataSource.getPassword(), actualDataSource.getPassword());
         return dataSourcesMatch &&
                StringUtils.equals(expectedContext.getSystemName(), actualContext.getSystemName());
      }

      public String toString(Object[] arguments) {
         DataSourceMigrationContext context = (DataSourceMigrationContext) arguments[0];
         NonPooledDataSource dataSource = (NonPooledDataSource) context.getDataSource();
         StringBuffer buf =
               new StringBuffer("DataSourceMigrationContext for \"").append(context.getSystemName()).append("\"\n");
         buf.append("Data source: \n");
         buf.append("\tURL=").append(dataSource.getDatabaseUrl()).append("\n");
         buf.append("\tDriver=").append(dataSource.getDriverClass()).append("\n");
         buf.append("\tUsername=").append(dataSource.getUsername()).append("\n");
         buf.append("\tPassword=").append(dataSource.getPassword()).append("\n");
         return buf.toString();
      }
   }
}