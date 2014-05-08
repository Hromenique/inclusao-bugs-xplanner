/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.domain;

import java.sql.SQLException;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.acceptance.DatabaseSupport;

/**
 * User: mprokopowicz
 * Date: Mar 30, 2006
 * Time: 1:17:40 PM
 */
public class HibernateTemplateSimulation extends HibernateTemplate {
   private DatabaseSupport databaseSupport;
   private Session session;

   public HibernateTemplateSimulation(DatabaseSupport databaseSupport) {this.databaseSupport = databaseSupport;}
   public HibernateTemplateSimulation(Session session) {this.session = session;}

   public Object execute(HibernateCallback action, boolean exposeNativeSession) throws DataAccessException {
      try {
         Session session = databaseSupport != null ? databaseSupport.getSession() : this.session;
         return action.doInHibernate(session);
      }
      catch (HibernateException ex) {
         throw convertHibernateAccessException(ex);
      }
      catch (SQLException ex) {
         throw convertJdbcAccessException(ex);
      }
      catch (RuntimeException ex) {
         throw ex;
      }
   }
}
