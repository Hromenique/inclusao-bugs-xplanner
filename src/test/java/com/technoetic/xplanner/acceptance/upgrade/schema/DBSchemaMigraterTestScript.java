/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 21, 2006
 * Time: 2:10:54 PM
 */
package com.technoetic.xplanner.acceptance.upgrade.schema;

import java.sql.SQLException;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import org.springframework.orm.hibernate.HibernateCallback;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.upgrade.schema.DBSchemaMigrater;

public class DBSchemaMigraterTestScript extends AbstractDatabaseTestScript
{
  public static final String HIBERNATE_CONSTRAINT_NAME = "FK33AFF2A62F61B6";
  public static final String APP_CONSTRAINT_NAME = "referer_to_referee_fk";
  public static final String REFERER = "referer";
  public static final String REFEREE = "referee";

  private DBSchemaMigrater migrater;
  private Throwable exception;

  protected void setUp() throws Exception {
    super.setUp();
    migrater = createDBSchemaMigrater();
    migrater.withNoException().dropTable(REFERER);
    migrater.withNoException().dropTable(REFEREE);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    migrater.withNoException().dropForeignKeyConstraint("referer", HIBERNATE_CONSTRAINT_NAME);
    migrater.withNoException().dropForeignKeyConstraint("referer", APP_CONSTRAINT_NAME);
    migrater.withNoException().dropTable(REFERER);
    migrater.withNoException().dropTable(REFEREE);
  }

  public void testWithNoException_TrapAllException() throws Exception {
    migrater.createTable(REFERER, "id integer, referee_id integer");
    Throwable exceptionThrown = null;
    try {
      migrater.createTable(REFERER, "id integer, referee_id integer");
    } catch (Throwable ex) {
      exceptionThrown = ex;
    }
    assertNotNull(exceptionThrown);
    migrater.withNoException().createTable(REFERER, "id integer, referee_id integer");
  }

  public void testCreateAndDropTable() throws Exception {
    migrater.createTable(REFERER, "id integer, referee_id integer");
    assertTableHasValidStructure();
    migrater.dropTable(REFERER);
    assertTableUnknown();
  }

  private void assertTableUnknown() {assertCannotExecuteSQL("select id, referee_id from referer");}
  private void assertTableHasValidStructure() { assertCanExecuteSQL("select id, referee_id from referer"); }

  public void testAddAndDropForeignConstraints() throws Exception {
    migrater.createTable(REFERER, "id integer, referee_id integer");
    migrater.createTable(REFEREE, "id integer");
    migrater.addPrimaryKey(REFEREE, "id");
    executeSQL("insert into referee values (10)");

    assertCanExecuteSQL("insert into referer values (1, 2)");

    executeSQL("delete from referer where id=1");

    migrater.addForeignKeyConstraint(REFERER, APP_CONSTRAINT_NAME, "referee_id", REFEREE, "id");

    assertCanExecuteSQL("insert into referer values (1, 10)");
    assertCannotExecuteSQL("insert into referer values (1, 3)");

    migrater.dropForeignKeyConstraint(REFERER, APP_CONSTRAINT_NAME);
    assertCanExecuteSQL("insert into referer values (1, 3)");
  }

  public void testAddAndDropUniqueConstraints() throws Exception {
    migrater.createTable(REFERER, "id integer, referee_id integer");
    migrater.addUniqueConstraint(REFERER, "referee_id", "referee_id");
    executeSQL("insert into " + REFERER + " values (1, 2)");

    assertCannotExecuteSQL("insert into " + REFERER + " values (2, 2)");
    assertCanExecuteSQL("insert into " + REFERER + " values (2, 3)");

    migrater.dropUniqueConstraint(REFERER, "referee_id");

    assertCanExecuteSQL("insert into " + REFERER + " values (2, 2)");
  }

  public void testDoesForeignConstraintsExist() throws Exception {
    if (migrater.isHsql()) return; //As of 1.8.0.4 Hsql does not provide a driver with meta data browsing capabilities
    migrater.createTable(REFERER, "id integer, referee_id integer");
    migrater.createTable(REFEREE, "id integer");
    migrater.addPrimaryKey(REFEREE, "id");

    assertFalse(migrater.doesForeignKeyConstraintExist(REFERER, APP_CONSTRAINT_NAME));

    migrater.addForeignKeyConstraint(REFERER, APP_CONSTRAINT_NAME, "referee_id", REFEREE, "id");

    assertTrue(migrater.doesForeignKeyConstraintExist(REFERER, APP_CONSTRAINT_NAME));
  }

  public void testRenameColumn() throws Exception {
    migrater.createTable(REFERER, "old integer");

    assertCanExecuteSQL("insert into referer (old) values (1)");
    assertCannotExecuteSQL("insert into referer (new) values (1)");

    migrater.renameColumn(REFERER, "old", "new");

    assertCannotExecuteSQL("insert into referer (old) values (1)");
    assertCanExecuteSQL("insert into referer (new) values (1)");
  }

  private void assertCanExecuteSQL(String sql) {
    if (canExecuteSQL(sql)) return;
    fail("Could not execute '" + sql + "': thrown exception " + exception);
  }

  private void assertCannotExecuteSQL(String sql) {
    if (!canExecuteSQL(sql)) return;
    fail("Could  execute '" + sql + "'");
  }


  private boolean canExecuteSQL(String sql) {
    try {
      executeSQL(sql);
    } catch (Throwable e) {
      exception = e;
      return false;
    }
    return true;
  }

  private void executeSQL(final String sql) {
    hibernateTemplate.execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        session.connection().createStatement().execute(sql);
        session.connection().commit();
        return null;
      }
    });
  }

}

