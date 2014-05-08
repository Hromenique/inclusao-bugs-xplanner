/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 23, 2006
 * Time: 4:04:26 PM
 */
package com.technoetic.xplanner.upgrade.schema;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.toys.decorate.Decorating;
import com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport;

import com.technoetic.xplanner.util.LogUtil;

/** @noinspection ClassWithTooManyMethods,RefusedBequest,LoopStatementThatDoesntLoop*/
public class DBSchemaMigrater {
  protected static final Logger LOG = LogUtil.getLogger();

  private DBDialect dialect;
  private JdbcTemplate template = new JdbcTemplate();

  public DBSchemaMigrater() { } // for the proxy

  public DBSchemaMigrater(Connection connection) {
    this.template = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
    this.dialect = DBDialect.getDialect(template);
  }

  public DBSchemaMigrater withNoException() {
    return (DBSchemaMigrater) Decorating.object(new Class[] {DBSchemaMigrater.class}, this, new InvocationDecoratorSupport() {
      public Throwable decorateTargetException(Object proxy, Method method, Object[] args, Throwable cause) {
        return null;
      }

      public Exception decorateInvocationException(Object proxy, Method method, Object[] args, Exception cause) {
        return null;
      }

      public Object decorateResult(final Object proxy, final Method method, final Object[] args, final Object result) {
        return result;
      }

    }, new CglibProxyFactory());
  }

  public void addForeignKeyConstraint(String table,
                                      String constraintName,
                                      String foreignKey,
                                      String referencedTable,
                                      String referencedKey) {
    executeQuery(dialect.getAddForeignKeyConstraintQuery(table, constraintName, foreignKey, referencedTable, referencedKey));
  }

  public void dropForeignKeyConstraint(String table, String constraintName) {
    executeQuery(dialect.getDropForeignKeyConstraintQuery(table, constraintName));
  }

  public void addUniqueConstraint(String table, String constraintName, String column) {
    executeQuery(dialect.getAddUniqueConstraintQuery(table, constraintName, column));
  }

  public void dropUniqueConstraint(String table, String constraintName) {
    executeQuery(dialect.getDropUniqueConstraintQuery(table, constraintName));
  }

  public void createTable(String table, String fieldSQL) {
    executeQuery(dialect.getCreateTableQuery(table, fieldSQL));
  }

  public void dropTable(String table) {
    executeQuery(dialect.getDropTableQuery(table));
  }

  public void addIndex(String table, String name, String field, boolean unique) {
    executeQuery(dialect.getCreateIndexQuery(table, name, field, unique));
  }

  public void addPrimaryKey(String table, String primaryColumn) {
    executeQuery(dialect.getAddPrimaryKeyQuery(table, new String[] {primaryColumn}));
  }

  public void addPrimaryKey(String table, String[] primaryColumns) {
    executeQuery(dialect.getAddPrimaryKeyQuery(table, primaryColumns));
  }

  public void changePrimaryKey(String table, String[] primaryColumns) {
    executeQuery(dialect.getChangePrimaryKeyQuery(table, primaryColumns));
  }

  public void changePrimaryKey(String table, String primaryColumn) {
    executeQuery(dialect.getChangePrimaryKeyQuery(table, new String[] {primaryColumn}));
  }

  public void dropPrimaryKey(String table) {
    executeQuery(dialect.getDropPrimaryKeyQuery(table));
  }

  public void dropIndex(String table, String name) {
    executeQuery(dialect.getDropIndexQuery(table, name));
  }

  public void renameColumn(String table, String oldName, String newName) {
    executeQuery(dialect.getRenameColumnQuery(table, oldName, newName, getColumnType(table, oldName)));
  }

  private String getColumnType(final String table, final String name) {
    return (String) template.execute(new ConnectionCallback() {
      public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
        ResultSet columns = connection.getMetaData().getColumns(null, null, table, name);
        try {
          if (columns.next()) {
            return columns.getString("TYPE_NAME");
          } else {
            return null;
          }
        } finally {
          columns.close();
        }
      }
    });
  }

  public boolean doesForeignKeyConstraintExist(final String table, final String name) {
    Boolean res = (Boolean) template.execute(new ConnectionCallback() {
      public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
        ResultSet importedKeys = connection.getMetaData().getImportedKeys(null, null, table);
        try {
          return Boolean.valueOf(hasKeyWithName(importedKeys, name));
        } finally {
          importedKeys.close();
        }
      }
    });
    return res.booleanValue();
  }

  private static boolean hasKeyWithName(ResultSet keys, String name) throws SQLException {
    while( keys.next()) {
      return keys.getString("FK_NAME").equals(name);
    }
    return false;
  }

  private void executeQuery(final String sql) {
    template.execute(new StatementCallback() {
      public Object doInStatement(Statement statement) throws SQLException, DataAccessException {
        String[] lines = StringUtils.split(sql, ';');
        for (int i = 0; i < lines.length; i++) {
          String line = lines[i];
          LOG.debug("Executing SQL " + line);
          statement.execute(line);
        }
        return null;
      }
    } );
  }

  public boolean isHsql() {
    return dialect instanceof HsqldbDBDialect;
  }
}