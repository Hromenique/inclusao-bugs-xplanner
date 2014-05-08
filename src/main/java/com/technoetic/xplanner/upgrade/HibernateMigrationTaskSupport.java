package com.technoetic.xplanner.upgrade;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

//DEBT(AUTOPATCH): Should inherit from spring HibernateDAOSupport.
//DEBT(AUTOPATCH): Need a MigrationContext that holds a HibernateSessionFactory instead of a connection
//DEBT(AUTOPATCH): Derived class SHOULD NOT Commit themselves. This is because the patch table should be updated inside the same transaction but is not if migration task is doing its own tx management

/** @noinspection OverlyBroadCatchBlock*/
public abstract class HibernateMigrationTaskSupport extends JdbcMigrationTaskSupport {
   protected XPlannerProperties properties = new XPlannerProperties();

   private static final Logger LOG = Logger.getLogger(HibernateMigrationTaskSupport.class);
   protected Session session;

  protected HibernateMigrationTaskSupport(String name, int level) {
    super(name, level);
  }

   protected void setUp() throws Exception {
     template.execute(new ConnectionCallback() {
       public Object doInConnection(Connection con) throws SQLException, DataAccessException {
         try {
           HibernateHelper.initializeHibernate();
         } catch (HibernateException e) {
           throw new RuntimeException(e);
         }
         session = GlobalSessionFactory.get().openSession(con);
         ThreadSession.set(session);
         return null;
       }
     });
   }

   protected void tearDown() throws Exception {
     if (session != null && session.isOpen()) {
       session.flush();
       session.close();
     }
   }

   protected abstract void migrate() throws Exception;

}
