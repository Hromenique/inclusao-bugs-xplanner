package com.technoetic.xplanner.tags;

import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

public class AuthenticatedUserTag extends DatabaseTagSupport {

   public int doStartTag() throws JspException {
      try {
         if (SecurityHelper.isUserAuthenticated((HttpServletRequest) pageContext.getRequest())) {
            Principal userPrincipal = SecurityHelper.getUserPrincipal((HttpServletRequest) pageContext.getRequest());
            //DEBT(DAO) : Move this to a dao
            if (getSession() != null) {
               List users = getSession().createQuery("from p in " + Person.class + " where p.userId = :userId").
                     setString("userId", userPrincipal.getName()).
                     setCacheable(true).
                     list();
               if (users.size() > 0) {
                  pageContext.setAttribute(id, users.get(0));
               }
            }
         }
         return super.doStartTag();
      } catch (Exception ex) {
         pageContext.getServletContext().log("error getting authenticated user", ex);
         throw new JspException(ex.getMessage());
      }
   }

   public void release() {
      super.release();
   }
}