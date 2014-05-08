/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.upgrade;

import java.util.Properties;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.DatabaseType;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.util.NonPooledDataSource;

import com.technoetic.xplanner.XPlannerProperties;


public class XPlannerMigrationLauncherFactory extends JdbcMigrationLauncherFactory {
   public static final String SYSTEM_NAME = "xplanner";

   private Properties properties;

   public XPlannerMigrationLauncherFactory() {
     this(new XPlannerProperties().get());
   }

   public XPlannerMigrationLauncherFactory(Properties properties) {
      this.properties = properties;
   }

   public JdbcMigrationLauncher createMigrationLauncher() throws MigrationException {
      JdbcMigrationLauncher launcher = new JdbcMigrationLauncher();
      configureFromXPlannerProperties(launcher);
      return launcher;
   }

   public JdbcMigrationLauncher createMigrationLauncher(String systemName) throws MigrationException {
      return createMigrationLauncher();
   }

   public void configureFromXPlannerProperties(JdbcMigrationLauncher launcher) throws MigrationException {
      launcher.setJdbcMigrationContext(createMigrationContext());
      launcher.setPatchPath(getRequiredParameter(properties, XPlannerProperties.PATCH_PATH_KEY));
   }

   private DataSourceMigrationContext createMigrationContext() {
      DatabaseType databaseType = new DatabaseType(getRequiredParameter(properties, XPlannerProperties.PATCH_DATABASE_TYPE_KEY));
      DataSourceMigrationContext context = new DataSourceMigrationContext();
      context.setDatabaseType(databaseType);
      context.setDataSource(createDataSource());
      context.setSystemName(SYSTEM_NAME);
      return context;
   }

   private NonPooledDataSource createDataSource() {
      NonPooledDataSource dataSource = new NonPooledDataSource();
      dataSource.setDatabaseUrl(getRequiredParameter(properties, XPlannerProperties.CONNECTION_URL_KEY));
      dataSource.setDriverClass(getRequiredParameter(properties, XPlannerProperties.DRIVER_CLASS_KEY));
      dataSource.setUsername(getRequiredParameter(properties, XPlannerProperties.CONNECTION_USERNAME_KEY));
      dataSource.setPassword(getRequiredParameter(properties, XPlannerProperties.CONNECTION_PASSWORD_KEY));
      return dataSource;
   }

   protected String getRequiredParameter(Properties properties, String parameterKey) {

      String value = properties.getProperty(parameterKey);
      if (value == null) {
         throw new IllegalArgumentException("Autopatching aborted. Required init param '" + parameterKey + "' was not found");
      }
      return value;

   }
}


