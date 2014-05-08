package com.technoetic.xplanner.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;

import com.kizna.servletunit.HttpServletRequestSimulator;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Person;

public class SecurityTestHelper
{

   public static void setRemoteUserId(String userId,
                                      HttpServletRequestSimulator request,
                                      Session session) throws HibernateException {
      XPlannerProperties properties = new XPlannerProperties();
      if (userId == null) userId = properties.getProperty("xplanner.test.user");
      Person person = getPerson(session, userId);
      setSubject(request, person);
   }

   private static void setSubject(HttpServletRequestSimulator request, Person person) {
      HashSet principals = new HashSet();
      principals.add(new PersonPrincipal(person));
      SecurityHelper.setSubject(request, new Subject(true, principals, new HashSet(), new HashSet()));
   }

   private static Person getPerson(Session session, String name) throws HibernateException {
        List people = session.find("from person in class " +
                Person.class.getName() + " where userid = ?",
                name, Hibernate.STRING);
        Person person = null;
        Iterator peopleIterator = people.iterator();
        if (peopleIterator.hasNext()) {
            person = (Person)peopleIterator.next();
        }
        return person;
    }
}