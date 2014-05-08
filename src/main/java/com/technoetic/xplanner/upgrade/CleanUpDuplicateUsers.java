package com.technoetic.xplanner.upgrade;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.RoleAssociation;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Permission;
import com.technoetic.xplanner.util.LogUtil;

public class  CleanUpDuplicateUsers extends HibernateMigrationTaskSupport {
   private static final Logger LOG = LogUtil.getLogger();
   public static boolean isCaseSensitive = false;
   public static boolean readCaseSensitiveValueFromProperties = true;
   List userList = null;

   public CleanUpDuplicateUsers() {
     super("cleanup_duplicate_users",5);
   }

   protected void setUp() throws Exception {
      super.setUp();
      userList = session.find("select person from Person person order by id asc");
   }

   protected void migrate() {
      initCaseSensitive();
      LOG.debug("==============================================================");
      LOG.debug("The userId casesensitive property is set to: " + isCaseSensitive);

      Map usersToBeCleand = getUserIdsToBeDeletedMap();
      LOG.debug("==============================================================");
      LOG.debug("There are " + usersToBeCleand.size() + " users to be cleaned up.");
      //cleanUp.printUsersToBeCleaned(usersToBeCleand);
      Iterator iterator = usersToBeCleand.keySet().iterator();
      while (iterator.hasNext()) {
         Person userToBeCleaned = (Person) iterator.next();
         Vector usersToBeDeleted = (Vector) usersToBeCleand.get(userToBeCleaned);
         LOG.debug("==============================================================");
         LOG.debug("Valid user:      " + userToBeCleaned);
         for (int a = 0; a < usersToBeDeleted.size(); a++) {
            LOG.debug("  Invalid user:  " + (Person) usersToBeDeleted.get(a));
            cleanUser(userToBeCleaned, (Person) usersToBeDeleted.get(a));
         }
      }
   }

   private void printUsersToBeCleaned(Map usersToBeCleand) {
      Iterator it = usersToBeCleand.keySet().iterator();
      while (it.hasNext()) {
         Person personToBeCleaned = (Person) it.next();
         //Should us ArrayList instead
         Vector v = (Vector) usersToBeCleand.get(personToBeCleaned);
         LOG.info("id=" + personToBeCleaned.getId() + ", " + personToBeCleaned.getUserId());
         for (int i = 0; i < v.size(); i++) {
            Person personToBeDeleted = (Person) v.get(i);
            LOG.info("      id=" + personToBeDeleted.getId() + ", userId=" + personToBeDeleted.getUserId());
         }
      }

      List list = null;
      try {
         list = session.find("select person from Person person where userId like 'defun%'");
      }
      catch (HibernateException e) {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }

      Iterator iter = list.iterator();
      LOG.info("Number of defunc users: " + list.size());
      while (iter.hasNext()) {
         Person p = (Person) iter.next();
         LOG.info("  id=" + p.getId() + ", userId=" + p.getUserId());
      }

   }

   private void cleanUser(Person userToBeCleaned, Person userToBeDeleted) {
      try {
         changeObject(Permission.class,
                      "select permission from Permission permission where permission.principalId=" +
                      userToBeDeleted.getId(),
                      "setPrincipalId",
                      userToBeCleaned.getId());
         changeObject(Permission.class,
                      "select permission from Permission permission where permission.resourceId=" +
                      userToBeDeleted.getId(),
                      "setResourceId",
                      userToBeCleaned.getId());
         changeObject(HistoricalEvent.class,
                      "select history from HistoricalEvent history where history.personId=" + userToBeDeleted.getId(),
                      "setPersonId",
                      userToBeCleaned.getId());
         changeObject(HistoricalEvent.class,
                      "select history from HistoricalEvent history where history.targetObjectId=" +
                      userToBeDeleted.getId(),
                      "setTargetObjectId",
                      userToBeCleaned.getId());
         changeObject(Integration.class,
                      "select integration from Integration integration where integration.personId=" +
                      userToBeDeleted.getId(),
                      "setPersonId",
                      userToBeCleaned.getId());
         changeObject(TimeEntry.class,
                      "select time_entry from TimeEntry time_entry where time_entry.person1Id=" +
                      userToBeDeleted.getId(),
                      "setPerson1Id",
                      userToBeCleaned.getId());
         changeObject(TimeEntry.class,
                      "select time_entry from TimeEntry time_entry where time_entry.person2Id=" +
                      userToBeDeleted.getId(),
                      "setPerson2Id",
                      userToBeCleaned.getId());
         changeObject(Task.class,
                      "select task from Task task where task.acceptorId=" + userToBeDeleted.getId(),
                      "setAcceptorId",
                      userToBeCleaned.getId());
         changeObject(UserStory.class,
                      "select userStory from UserStory userStory where userStory.trackerId=" + userToBeDeleted.getId(),
                      "setTrackerId",
                      userToBeCleaned.getId());

         changeCustomerInUserStory(userToBeCleaned, userToBeDeleted);
         changeRoleAssociation(userToBeCleaned, userToBeDeleted);
         session.delete(userToBeDeleted);
         session.flush();
         session.connection().commit();
         LOG.debug("  Invalid user deleted:  " + userToBeDeleted);
      } catch (HibernateException e) {
         e.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private void changeCustomerInUserStory(Person userToBeCleaned, Person userToBeDeleted)
         throws HibernateException {
      List list = session.find("select userStory from UserStory userStory where userStory.customer=" +
                               userToBeDeleted.getId());
      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
         UserStory object = (UserStory) iterator.next();
         LOG.debug("    Object to be changed:  " + object);
         object.setCustomer(userToBeCleaned);
         session.update(object);
         LOG.debug("       Object changed to:  " + object);
      }
   }

   private void changeRoleAssociation(Person userToBeCleaned, Person userToBeDeleted)
         throws HibernateException {

      List listAssociation = session.find("select roleAssociation from RoleAssociation roleAssociation where" +
                                          " roleAssociation.personId=" + userToBeCleaned.getId() +
                                          " or roleAssociation.personId=" + userToBeDeleted.getId());
      Iterator listAssociationItarator = listAssociation.iterator();
      while (listAssociationItarator.hasNext()) {
         RoleAssociation roleAssociationToBeDeleted = (RoleAssociation) listAssociationItarator.next();
         RoleAssociation roleAssociationToBeSaved = new RoleAssociation(roleAssociationToBeDeleted.getProjectId(),
                                                                        userToBeCleaned.getId(),
                                                                        roleAssociationToBeDeleted.getRoleId());
         List list = session.find(
               "select roleAssociation " +
               " from RoleAssociation roleAssociation " +
               " where role_id=" + roleAssociationToBeSaved.getRoleId() +
               " and person_id=" + roleAssociationToBeSaved.getPersonId() +
               " and project_id=" + roleAssociationToBeSaved.getProjectId());
         if (list.isEmpty()) {
            session.delete(roleAssociationToBeDeleted);
            session.saveOrUpdate(roleAssociationToBeSaved);
         }
      }
   }

   private void changeObject(Class clazz, String query, String method, int userId)
         throws HibernateException {
      try {
         Method m = clazz.getDeclaredMethod(method, new Class[]{int.class});
         List list = session.find(query);
         Iterator it = list.iterator();
         while (it.hasNext()) {
            Object object = (Object) it.next();
            LOG.debug("    Object to be changed:  " + object);
            m.invoke(object, new Object[]{new Integer(userId)});
            session.update(object);
            LOG.debug("       Object changed to:  " + object);
         }
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }

   }

   private void initCaseSensitive() {
      if (readCaseSensitiveValueFromProperties) {
         isCaseSensitive = SecurityHelper.isAuthenticationCaseSensitive();
      }
   }

   private Map getUserIdsToBeDeletedMap() {
      Map usersToBeCleand = new HashMap();
      ArrayList proceedIds = new ArrayList();
      for (int i = 0; i < userList.size(); i++) {
         Person personToBeCleaned = (Person) userList.get(i);
         Vector usersToBeDeleted = new Vector();
         if (!personToBeCleaned.isHidden()) {
            for (int ii = 0; ii < userList.size(); ii++) {
               Person personToCompare = (Person) userList.get(ii);
               if (!proceedIds.contains(new Integer(personToBeCleaned.getId())) &&
                   !proceedIds.contains(new Integer(personToCompare.getId())) &&
                   personToBeCleaned.getId() != personToCompare.getId()) {
                  if (isCaseSensitive) {
                     if (personToBeCleaned.getUserId().equals(personToCompare.getUserId())) {
                        usersToBeDeleted.add(personToCompare);
                        proceedIds.add(new Integer(personToCompare.getId()));
                     }
                  } else {
                     if (personToBeCleaned.getUserId().equalsIgnoreCase(personToCompare.getUserId())) {
                        usersToBeDeleted.add(personToCompare);
                        proceedIds.add(new Integer(personToCompare.getId()));
                     }
                  }
               }
            }
            proceedIds.add(new Integer(personToBeCleaned.getId()));
         }
         if (usersToBeDeleted.size() > 0) {
            usersToBeCleand.put(personToBeCleaned, usersToBeDeleted);
         }
      }
      return usersToBeCleand;
   }


   public static void main(String[] arg) throws Exception {
      new CleanUpDuplicateUsers().run();

   }
}
