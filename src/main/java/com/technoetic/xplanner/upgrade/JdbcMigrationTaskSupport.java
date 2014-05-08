/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 13, 2006
 * Time: 1:51:27 PM
 */
package com.technoetic.xplanner.upgrade;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationContext;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.upgrade.schema.DBSchemaMigrater;

public abstract class JdbcMigrationTaskSupport extends MigrationTaskSupport {
  protected DBSchemaMigrater migrater;
  protected JdbcTemplate template;

  protected JdbcMigrationTaskSupport(String name, int level) {
    setName(name);
    setLevel(new Integer(level));
  }

  private Connection getConnection(MigrationContext context) throws MigrationException {
    try {
      return ((JdbcMigrationContext) context).getConnection();
    } catch (SQLException e) {
      throw new MigrationException("Could not open connection", e);
    }
  }

  final public void migrate(MigrationContext context) throws MigrationException {
    init(context);

    boolean inException = false;
    try {
      setUp();
      migrate();
    } catch (MigrationException e) {
      throw e;
    } catch (Exception e) {
      inException = true;
      throw new MigrationException("exception during migration", e);
    } finally {
      try {
        tearDown();
      } catch (Exception e) {
        if (!inException) {
          throw new MigrationException("exception in tearDown", e);
        }
      }
    }
  }

  protected void init(MigrationContext context) throws MigrationException {
    Connection connection = getConnection(context);
    migrater = new DBSchemaMigrater(connection);
    template = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
  }

  protected void setUp() throws Exception { }

  protected void tearDown() throws Exception {}

  abstract protected void migrate() throws Exception;

  /**
   * Support for running a migration task in isolation (for manual testing for example)
   * Warning: This should be used with caution since it does not update the patch table and therefore will not take care
   * of preventing this task to run the next time
   */
  public void run() throws Exception {
    Session session = newSession();
    final Connection conn = session.connection();
    try {
      migrate(new DataSourceMigrationContext() {
        public Connection getConnection() throws SQLException {
          return conn;
        }
      });
    } finally {
      conn.commit();
      session.close();
    }
  }

  private Session newSession() throws HibernateException {
    HibernateHelper.initializeHibernate();
    Session session = GlobalSessionFactory.get().openSession();
    return session;
  }
}