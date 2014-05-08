/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 8, 2006
 * Time: 1:27:04 AM
 */
package com.technoetic.xplanner.upgrade.schema;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.util.LogUtil;

public class DBDialect {

  protected static final Logger LOG = LogUtil.getLogger();

  private static final Map DIALECT_BY_DATABASE = new HashMap(10);

  static { initDialectByDatabase(); }

  private static void initDialectByDatabase() {
    addDialect(new MySqlTo4_0DBDialect());
    addDialect(new MySqlFrom4_1DBDialect());
    addDialect(new HsqldbDBDialect());
  }

  private static void addDialect(DBDialect dialect) {
    String vendor = dialect.getVendor();
    List versions = (List)DIALECT_BY_DATABASE.get(vendor);
    if (versions == null) {
      versions = new ArrayList(1);
      DIALECT_BY_DATABASE.put(vendor, versions);
    }
    versions.add(dialect);
  }


  public static DBDialect getDialect(JdbcTemplate template)
  {
    return (DBDialect)template.execute(new ConnectionCallback() {
      public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
        DBDialect dialect = (DBDialect) getDialect(connection);
        LOG.debug("Using " + dialect + " to handle schema migration of " + getDatabaseVendor(connection));
        return dialect;
      }
    });
  }

  private static Object getDialect(Connection connection) throws SQLException {
    List versions = (List) DIALECT_BY_DATABASE.get(getDatabaseVendor(connection));
    if (versions != null) {
      double version = getDatabaseVersion(connection);
      for (int i = 0; i < versions.size(); i++) {
        DBDialect dialect = (DBDialect) versions.get(i);
        if (dialect.getFromVersion() <= version && version <= dialect.getToVersion()) {
          return dialect;
        }
      }
    }
    return new DBDialect();
  }

  private static String getDatabaseVendor(Connection connection) throws SQLException {
    return connection.getMetaData().getDatabaseProductName();
  }

  private static double getDatabaseVersion(Connection connection) throws SQLException {
    int major = connection.getMetaData().getDatabaseMajorVersion();
    int minor = connection.getMetaData().getDatabaseMinorVersion();
    double version = Double.parseDouble("" + major + "." + minor);
    return version;
  }

  public String getVendor() {return "default";}
  public double getFromVersion() {return Double.MIN_VALUE;}
  public double getToVersion() {return Double.MAX_VALUE;}


  public String toString() {
    String str = getVendor();
    String min = getFromVersion()==Double.MIN_VALUE?"first":""+getFromVersion();
    String max = getToVersion()==Double.MAX_VALUE?"latest":""+getToVersion();
    return "{" + str + " [" + min + "-" + max + "]}";
  }

  public String getAddForeignKeyConstraintQuery(String table,
                                                String constraintName,
                                                String foreignKey,
                                                String referencedTable,
                                                String referencedKey) {
    return "alter table " +
           table +
           " add constraint " +
           constraintName +
           " foreign key (" + foreignKey + ") references " +
           referencedTable +
           " (" +
           referencedKey +
           ")";
  }

  public String getDropForeignKeyConstraintQuery(String table, String constraintName) {
    return getDropConstraintQuery(table, constraintName);
  }

  public String getAddUniqueConstraintQuery(String table, String constraintName, String column) {
    return "alter table " + table + " add constraint " + constraintName + " unique " + constraintName + " ( " + column + " )";
  }

  public String getDropUniqueConstraintQuery(String table, String constraintName) {
    return getDropConstraintQuery(table, constraintName);
  }

  private String getDropConstraintQuery(String table, String constraintName) {
    return "alter table " + table + " drop constraint " + constraintName;
  }

  public String getCreateTableQuery(String table, String fieldSQL) {
    return "create table " + table + " ( "+ fieldSQL + " )";
  }

  public String getDropTableQuery(String table) {
    return "drop table " + table;
  }

  public String getCreateIndexQuery(String table, String name, String field, boolean unique) {
    return "create " + (unique?"unique":"") + " index " + name + " " + " on " + table + " ( " + field + " ) ";
  }

  public String getDropIndexQuery(String table, String name) {
    return "drop index " + name + " on " + table ;
  }

  public String getAddPrimaryKeyQuery(String table, String[] primaryColumns) {
    return "alter table " + table + " add primary key ( " + StringUtils.join(primaryColumns, ',') + " )";
  }

  public String getChangePrimaryKeyQuery(String table, String[] primaryColumns) {
    return "alter table " + table + " alter primary key ( " + StringUtils.join(primaryColumns, ',') + " )";
  }

  public String getDropPrimaryKeyQuery(String table) {
    return "alter table " + table + " drop primary key";
  }

  public String getRenameColumnQuery(String table, String oldName, String newName, String type) {
    return "alter table " + table + " change " + oldName + " " + newName + " " + type;
  }

}