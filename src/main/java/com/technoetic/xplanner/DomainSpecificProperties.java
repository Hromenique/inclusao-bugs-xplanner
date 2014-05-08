/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner;

import java.util.List;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.type.Type;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;

import com.technoetic.xplanner.domain.Attribute;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.UserStory;

/**
 * User: mprokopowicz
 * Date: Feb 6, 2006
 * Time: 12:59:52 PM
 */
public class DomainSpecificProperties extends Properties {
   private SessionFactory sessionFactory;
   private Object object;

   protected DomainSpecificProperties(Properties defaultProperties,
                                      SessionFactory sessionFactory,
                                      Object domainObject) {
      super(defaultProperties);
      this.sessionFactory = sessionFactory;
      this.object = domainObject;
   }

   public String getProperty(String key) {
      HibernateTemplate hibernateTemplate = new HibernateTemplate(this.sessionFactory);
      final Integer attributeTargetId = getAttributeTargetId(object, hibernateTemplate);
      if (attributeTargetId != null) {
         HibernateCallback action = new GetAttributeHibernateCallback(attributeTargetId, key);
         Attribute attribute = (Attribute) hibernateTemplate.execute(action);
         if (attribute != null) {
            return attribute.getValue();
         }
      }
      return super.getProperty(key);
   }


   private Integer getAttributeTargetId(Object object, HibernateTemplate hibernateTemplate) {
      if (object instanceof Project) {
         Project project = (Project) object;
         return new Integer(project.getId());
      }
      if (object instanceof Iteration) {
         Iteration iteration = (Iteration) object;
         return new Integer(iteration.getProjectId());
      }
      Integer iterationId = null;
      if (object instanceof UserStory) {
         UserStory story = (UserStory) object;
         iterationId = new Integer(story.getIterationId());
      }
      if (object instanceof Task) {
         Task task = (Task) object;
         iterationId = new Integer(task.getStory().getIterationId());
      }
      if (iterationId != null) {
         final Integer id = iterationId;
         Iteration iteration = (Iteration) hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
               return session.load(Iteration.class, id);
            }
         });
         return new Integer(iteration.getProjectId());
      }
      return null;
   }

   public synchronized Enumeration keys() {
      Set keys = keySet();
      return Collections.enumeration(keys);
   }

   public Set keySet() {
      Set keys = new HashSet(defaults.keySet());
      keys.addAll(super.keySet());
      return keys;
   }

   public Collection values() {
      List values = new ArrayList(defaults.values());
      values.addAll(super.values());
      return values;
   }


   private static class GetAttributeHibernateCallback implements HibernateCallback {
      private final Integer attributeTargetId;
      private final String key;

      public GetAttributeHibernateCallback(Integer attributeTargetId, String key) {
         this.attributeTargetId = attributeTargetId;
         this.key = key;
      }

      public Object doInHibernate(Session session) throws HibernateException {
         Attribute attribute = null;

         List attributeList =
               session.find(
                     "select attribute from Attribute attribute where attribute.targetId = ? and attribute.name= ? ",
                     new Object[]{attributeTargetId, key},
                     new Type[]{Hibernate.INTEGER, Hibernate.STRING});
         if (attributeList.size() > 0) {
            attribute = (Attribute) attributeList.get(0);
         }
         return attribute;
      }
   }
}
