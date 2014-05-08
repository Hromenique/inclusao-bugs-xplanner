package com.technoetic.xplanner.actions.admin;

import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.db.hibernate.HibernateSessionFilter;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.SessionFactory;

public class InvalidateHibernateCacheAction extends Action {
    private static final Logger log = LogUtil.getLogger();
    SessionFactory sessionFactory;

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
      sessionFactory.evictQueries();
      log.info("hibernate cache cleared");
      return null;
   }


   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }
}
