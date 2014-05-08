/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 8, 2006
 * Time: 4:16:57 AM
 */
package com.technoetic.xplanner.upgrade;

import java.util.Properties;
import java.sql.Statement;
import java.sql.SQLException;

import net.sf.hibernate.type.Type;
import net.sf.hibernate.type.LongType;
import org.apache.axis.types.Id;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.dao.DataAccessException;
import com.tacitknowledge.util.migration.MigrationException;

import com.technoetic.xplanner.upgrade.schema.DBSchemaMigrater;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.soap.XPlanner;

public class RemoveIterationDeletionRightsFromEditor extends JdbcMigrationTaskSupport {

  public RemoveIterationDeletionRightsFromEditor() {
     super("Remove iteration deletion rights from editor", 14);
  }

    protected void migrate() throws Exception {
      final int nextId = IdGenerator.getNextPersistentId();
      IdGenerator.setNextPersistentId(nextId+1);
      template.execute(new StatementCallback() {
        public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
          stmt.execute("INSERT INTO permission VALUES(" + nextId + ",3,'delete','system.project.iteration',0, 0)");

          return null;
        }
      });
    }

    public static void main(String[] args) throws Exception {
       new RemoveIterationDeletionRightsFromEditor().run();
    }
}