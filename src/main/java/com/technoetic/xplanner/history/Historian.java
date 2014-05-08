/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: May 28, 2005
 * Time: 7:54:24 PM
 */
package com.technoetic.xplanner.history;

import java.util.Date;

import net.sf.hibernate.Session;

import com.technoetic.xplanner.domain.DomainObject;

public class Historian {
   private Session session;
   private int currentUserId;

   //DEBT 3LAYERCONTEXT: Introduce a 3 layer context file hierarchy: 1 request, 1 session, 1 application: The session should hold the Historian so that it can be initialized after the user has authenticated.
   public Historian(Session session, int currentUserId) {
      this.session = session;
      this.currentUserId = currentUserId;
   }

   public void saveEvent(DomainObject object, String action, String description, Date when) {
      HistorySupport.saveEvent(session, object, action, description, currentUserId, when);
   }
}