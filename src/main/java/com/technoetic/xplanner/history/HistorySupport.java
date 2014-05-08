package com.technoetic.xplanner.history;

import java.util.Date;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.DomainObject;

public class HistorySupport {
   private static Logger log = Logger.getLogger(HistorySupport.class);
   private static long FIFTEEN_MINUTES = 3600000 * 15;

   //DEBT should be spring loaded (metadatarepository should be passed in and we should not have private methods
   private static void saveEvent(Session session, Class objectClass, int containerOid, int oid, String action,
                                 String description, int personId, Date when) {
      try {
         HistoricalEvent historicalEvent = new HistoricalEvent(containerOid,
                                                               oid,
                                                               DomainMetaDataRepository.getInstance().classToTypeName(objectClass),
                                                               action,
                                                               description,
                                                               personId,
                                                               when);
         if (!isEventThrottled(session, historicalEvent)) {
            session.save(historicalEvent);
         }
         if (action.equals(HistoricalEvent.DELETED)) {
            // Set name in event descriptions for deleted objects
            List events = getEvents(oid);
            for (int i = 0; i < events.size(); i++) {
               HistoricalEvent event = (HistoricalEvent) events.get(i);
               if (StringUtils.isEmpty(event.getDescription())) {
                  event.setDescription(description);
               }
            }
         }
      } catch (HibernateException e) {
         log.error("history error", e);
      }
   }

   private static boolean isEventThrottled(Session session, HistoricalEvent event) throws HibernateException {
      if (event.getAction().equals(HistoricalEvent.UPDATED)) {
         HistoricalEvent previousEvent = (HistoricalEvent) session.createQuery(
               "from event in " +
               HistoricalEvent.class +
               " where event.targetObjectId = :oid and event.action = :action order by event.when desc").
               setInteger("oid", event.getTargetObjectId()).setString("action", HistoricalEvent.UPDATED).
               setMaxResults(1).uniqueResult();
         return previousEvent != null &&
                (event.getWhen().getTime() - previousEvent.getWhen().getTime()) < FIFTEEN_MINUTES;
      } else {
         return false;
      }
   }

   public static void saveEvent(Session session, DomainObject object, String action,
                                String description, int personId, Date when) {
      try {
         Integer id = (Integer) PropertyUtils.getProperty(object, "id");
         saveEvent(session, object.getClass(), DomainMetaDataRepository.getInstance().getParentId(object), id.intValue(),
                   action, description, personId, when);
      } catch (Exception e) {
         log.error("history error", e);
      }
   }

   public static List getEvents(int oid) throws HibernateException {
      //TODO externalize these queries into mapping file
      return ThreadSession.get().find("from event in " +
                                      HistoricalEvent.class +
                                      " where event.targetObjectId = ? order by event.when desc",
                                      new Integer(oid), Hibernate.INTEGER);
   }

   public static List getContainerEvents(int oid) throws Exception {
      return ThreadSession.get().find("from event in " +
                                      HistoricalEvent.class +
                                      " where event.containerId = ? " +
                                      " and event.action in ('" +
                                      HistoricalEvent.CREATED +
                                      "','" +
                                      HistoricalEvent.DELETED + "') order by event.when desc",
                                      new Integer(oid), Hibernate.INTEGER);
   }

   public static Object getHistoricalObject(HistoricalEvent event) throws HibernateException {
      if (event.getAction().equals(HistoricalEvent.DELETED)) {
         return null;
      }
      return DomainMetaDataRepository.getInstance().getObject(event.getObjectType(), event.getTargetObjectId());
   }

}
