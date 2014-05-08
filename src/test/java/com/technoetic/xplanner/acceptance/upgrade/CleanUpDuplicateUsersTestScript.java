package com.technoetic.xplanner.acceptance.upgrade;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.exception.SQLExceptionConverter;
import net.sf.hibernate.exception.SQLExceptionConverterFactory;

import com.technoetic.xplanner.acceptance.web.AbstractPageTestScript;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Permission;
import com.technoetic.xplanner.upgrade.CleanUpDuplicateUsers;
import com.technoetic.xplanner.upgrade.schema.DBSchemaMigrater;

/**
 * 1. Fix your class template. Until I come up with one, just have an empty one (use the Copyright plugin to manage a project specific copyright header
 * 2. ALWAYS call super.setUp and super.tearDown in setUp and tearDown even if you directly inherit from TestCase
 * 3. Don't get an Person to then update it through a direct SQL update statement. Use hb, set the field and call save()
 * 4. The current test does not test case insensitive and case sensitive userid.
 * 5. Make sure you understand what a call does: HBHelper.initializeHibernate() calls directly initializeConfiguration()! No need to call initializeConfiguration() right after initializeHibernate()...
 * 6. No need to call the session "testSession". There is only one and it is already inside a test so session IMHO would be fine.
 * 7. Remove all duplication. YOU ARE NOT FINISHED until you do!!!
 *         1. Run all the tests
 *         2. Contain no duplicate code
 *         3. Express all the ideas the author wants to express
 *         4. Minimize classes and methods
 *    Please read http://www.xprogramming.com/xpmag/expEmergentDesign.htm
 *    This class is riddle of duplication:
 *       id, name, initials can be stored into a Person object => call to same method to populate that person.
 *       query string are the same.
 *       Do not repeat the same lines of code if they are in both branch of an if. Extract them before or after.
 * 8 No need f
 * @noinspection StringConcatenation
 */
public class CleanUpDuplicateUsersTestScript extends AbstractPageTestScript
{
    private boolean wasPersonTableAltered = false;
    private Map currentObjectMap = new HashMap();
    private Map upperCaseObjectMap = new HashMap();
    private Map lowerCaseObjectMap = new HashMap();
    private boolean isCaseSensitive = false;

    private String developerIdUpperCase;
    private String developerNameUpperCase;
    private String developerNameInitialsUpperCase = "UC";

    private String developerIdLowerCase;
    private String developerIdLowerCaseToBeChangedOne;
    private String developerIdLowerCaseToBeChangedTwo;
    private String developerNameLowerCase;
    private String developerNameLowerCaseOne;
    private String developerNameLowerCaseTwo;
    private String developerNameInitialsLowerCase = "LC";
    private String developerNameInitialsLowerCaseOne = "LC1";
    private String developerNameInitialsLowerCaseTwo = "LC2";
    private String taskId_UC = null;
    private String taskId_LC = null;
    private final String CONSTRAINT_NAME = "userId";
  private DBSchemaMigrater migrater;

  protected void setUp() throws Exception {
     try {
        super.setUp();
        migrater = createDBSchemaMigrater();
        migrater.withNoException().dropUniqueConstraint("person", CONSTRAINT_NAME);
        migrater.withNoException().dropUniqueConstraint("person", "SYS_CT_51");
        setCaseSensitive();

        developerIdUpperCase = IdGenerator.getUniqueId("SG");
        developerNameUpperCase = developerIdUpperCase + " - " + developerNameInitialsUpperCase;

        developerIdLowerCase = developerIdUpperCase.replace('S','s').replace('G','g');
        developerIdLowerCaseToBeChangedOne = developerIdLowerCase + "-1";
        developerIdLowerCaseToBeChangedTwo = developerIdLowerCase + "-2";

        developerNameLowerCase = developerIdLowerCase + " - " + developerNameInitialsLowerCase;
        developerNameLowerCaseOne = developerIdLowerCase + " - " + developerNameInitialsLowerCaseOne;
        developerNameLowerCaseTwo = developerIdLowerCase + " - " + developerNameInitialsLowerCaseTwo;

        tester.login();
        personTester.addPerson(developerNameLowerCaseOne, developerIdLowerCaseToBeChangedOne, developerNameInitialsLowerCaseOne, userEmail, userPhone);
        taskId_UC = addMigrationData("UC", developerNameUpperCase, developerIdUpperCase, developerNameInitialsUpperCase);
        this.upperCaseObjectMap = getObjects(developerIdUpperCase);
        taskId_LC = addMigrationData("LC", developerNameLowerCaseTwo, developerIdLowerCaseToBeChangedTwo, developerNameInitialsLowerCaseTwo);
        this.lowerCaseObjectMap = getObjects(developerIdLowerCaseToBeChangedTwo);

        setDuplicatePersonId(developerIdLowerCaseToBeChangedOne, developerIdLowerCase);
        setDuplicatePersonId(developerIdLowerCaseToBeChangedTwo, developerIdLowerCase);

        commitSession();
     } catch (Throwable e) {
        tearDown();
        throw getException(e);
     }
  }

   private Exception getException(Throwable e) {
      if (e instanceof Error) throw (Error)e;
      return (Exception) e;
   }

  protected void tearDown() throws Exception {
     try {
        deleteObjects(currentObjectMap, Permission.class);
        deleteObjects(currentObjectMap, HistoricalEvent.class);
        deleteObjects(currentObjectMap, Integration.class);

        //deleteObjects(upperCaseObjectMap, Permission.class);
        deleteObjects(upperCaseObjectMap, HistoricalEvent.class);
        deleteObjects(upperCaseObjectMap, Integration.class);

        if (taskId_UC != null)
            deleteLocalTimeEntry(taskId_UC);
        if (taskId_LC != null)
            deleteLocalTimeEntry(taskId_LC);
        tester.deleteObjects(Task.class, "name", testTaskName+"LC");
        tester.deleteObjects(Task.class, "name", testTaskName+"UC");
        tester.deleteObjects(UserStory.class, "name", storyName+"LC");
        tester.deleteObjects(UserStory.class, "name", storyName+"UC");
        tester.deleteObjects(Iteration.class, "name", testIterationName+"LC");
        tester.deleteObjects(Iteration.class, "name", testIterationName+"UC");
        tester.deleteObjects(Project.class, "name", testProjectName+"LC");
        tester.deleteObjects(Project.class, "name", testProjectName+"UC");

        tester.deleteObjects(Person.class, "userId", developerIdUpperCase);
        tester.deleteObjects(Person.class, "userId", developerIdLowerCase);

        commitSession();
     } finally {
        migrater.addUniqueConstraint("person", CONSTRAINT_NAME, "userId");
        super.tearDown();
     }
  }

    private void deleteObjects(Map objectMap, Class aClass) throws HibernateException, SQLException {
       List objectList = (List)objectMap.get(aClass);
       if (objectList == null) return;
       Iterator objectIterator = objectList.iterator();
       while(objectIterator.hasNext()){
          getSession().delete(objectIterator.next());
       }
       commitSession();
    }

    public void testCleanDuplicateUsersUtility() throws Exception {

        CleanUpDuplicateUsers.main(new String[0]);

        personTester.gotoPeoplePage();
        tester.assertLinkPresentWithText(developerNameLowerCaseOne);
        tester.assertLinkNotPresentWithText(developerNameLowerCaseTwo);
        if(isCaseSensitive){
            tester.assertLinkPresentWithText(developerNameUpperCase);
        } else {
            tester.assertLinkNotPresentWithText(developerNameUpperCase);
        }
       assertObjectsReconnection();
    }

    private void assignPersonToStory(String developerName, String suffix)
        throws Exception
    {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName+suffix);
        tester.clickLinkWithText(testIterationName+suffix);
        tester.clickLinkWithText(storyName+suffix);
        tester.clickLinkWithKey("action.edit.story");
        tester.selectOption("customerId", developerName);
        tester.submit();
    }

    private void setDuplicatePersonId(String currentDeveloperId, String newDeveloperId)
        throws HibernateException
    {
        List persons = getSession().find("from person in class " + Person.class + " where person.userId = ?",
                                         currentDeveloperId, Hibernate.STRING);
        Person person = (Person) persons.iterator().next();
        executeSQL("UPDATE person SET userId='"+newDeveloperId+"' WHERE id="+person.getId());
    }

    private void executeSQL(String sql) throws HibernateException {
      System.out.println("Executing " + sql);
      try {
        getSession().connection().createStatement().execute(sql);
      } catch (SQLException e) {
        SQLExceptionConverter sqlExceptionConverter =
          SQLExceptionConverterFactory.buildSQLExceptionConverter(HibernateHelper.getDialect(),
                                                                  HibernateHelper.getProperties());
        throw sqlExceptionConverter.convert(e, "");
      }
    }
    private void setCaseSensitive()
    {
       isCaseSensitive = SecurityHelper.isAuthenticationCaseSensitive();
    }

    private Map getObjects(String personId) throws Exception
   {
       commitSession();
       Map objectMap = new HashMap();
       List persons = getSession().find("from person in class " + Person.class +
                                        " where person.userId = ?",
                                        personId, Hibernate.STRING);
       Person person = (Person) persons.iterator().next();
       List list = getSession().find("select permission from Permission permission where permission.principalId="+person.getId());
       objectMap.put(Permission.class, list);
       list = getSession().find("select history from HistoricalEvent history where history.personId="+person.getId());
       objectMap.put(HistoricalEvent.class, list);
       list = getSession().find("select integration from Integration integration where integration.personId="+person.getId());
       objectMap.put(Integration.class, list);
       list = getSession().find("select time_entry from TimeEntry time_entry where time_entry.person1Id="+person.getId());
       objectMap.put(TimeEntry.class, list);
       list = getSession().find("select task from Task task where task.acceptorId="+person.getId());
       objectMap.put(Task.class, list);
       list = getSession().find("select userStory from UserStory userStory where userStory.trackerId="+person.getId());
       objectMap.put(UserStory.class, list);
       return objectMap;
   }

   private void assertObjectsReconnection() throws Exception
   {
       currentObjectMap = getObjects(developerIdLowerCase);
       Iterator iterator = currentObjectMap.keySet().iterator();
       while(iterator.hasNext()){
           Object o = iterator.next();
           List currentList = (List)currentObjectMap.get(o);
           List lowerCaseList = (List)lowerCaseObjectMap.get(o);
           if(isCaseSensitive){
               assertFalse(currentList.size() < lowerCaseList.size());
           } else {
               List upperCaseList = (List)upperCaseObjectMap.get(o);
               assertFalse(currentList.size() < (lowerCaseList.size() + upperCaseList.size()));
           }
       }
   }

   private String addMigrationData(String suffix,String developerName, String developerId, String developerInitials)
        throws Exception
    {
        tester.gotoProjectsPage();
        tester.addProject(testProjectName+suffix,testProjectDescription+suffix);
        personTester.addPersonWithRole(developerName, developerId, developerInitials, userEmail, userPhone, testProjectName+suffix, tester.getMessage("person.editor.role.editor"));
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName+suffix);
        iterationTester.addIteration(testIterationName+suffix,tester.dateStringForNDaysAway(0), tester.dateStringForNDaysAway(7), testIterationDescription+suffix);
        tester.clickLinkWithText(testIterationName+suffix);
        iterationTester.startCurrentIteration();

        tester.addUserStory(storyName+suffix, testStoryDescription+suffix, estimatedHoursString, "1");
        tester.clickLinkWithText(storyName+suffix);
        String taskId = tester.addTask(testTaskName+suffix, developerName, testTaskDescription+suffix, testTaskEstimatedHours);
        tester.clickLinkWithText(testTaskName+suffix);
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 0, 20, developerName);
        assignPersonToStory(developerName, suffix);
        return taskId;
    }
}
