/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Permission;

/**
 * User: mprokopowicz
 * Date: Feb 15, 2006
 * Time: 4:09:12 PM
 */
public class PersonHibernateObjectRepository extends HibernateObjectRepository {
   static final String CHECK_PERSON_UNIQUENESS_QUERY = "com.technoetic.xplanner.domain.CheckPersonUniquenessQuery";

   public PersonHibernateObjectRepository(Class objectClass) throws HibernateException {
      super(objectClass);
   }

   public int insert(DomainObject domainObject) throws RepositoryException {
      Person person = (Person) domainObject;
      checkPersonUniquness(person);
      int id = super.insert(person);
      setUpEditPermission(person);
      return id;
   }

   public void update(DomainObject domainObject) throws DuplicateUserIdException {
      Person person = (Person) domainObject;
      checkPersonUniquness(person);
      super.update(person);
   }

   public void delete(final int objectIdentifier) throws RepositoryException {
      super.delete(objectIdentifier);
      getHibernateTemplate().delete("from " + Permission.class.getName() + " where resource_id = ?",
                                    new Integer(objectIdentifier),
                                    Hibernate.INTEGER);
   }

   void setUpEditPermission(DomainObject person) {
      getHibernateTemplate().save(new Permission("system.person", person.getId(), person.getId(), "edit%"));
//        session.save(new Permission("system.person",person.getId(),person.getId(),"read%"));
      getHibernateTemplate().save(new Permission("system.person", 0, person.getId(), "read%"));
//        roleAssociationRepository.insertForPersonOnProject("viewer",person.getId(),0);
   }

   void checkPersonUniquness(Person person) throws DuplicateUserIdException {
      List potentialDuplicatePeople =
            getHibernateTemplate().findByNamedQuery(CHECK_PERSON_UNIQUENESS_QUERY,
                                                    new Object[]{new Integer(person.getId()), person.getUserId()},
                                                    new Type[]{Hibernate.INTEGER, Hibernate.STRING});

      Iterator iterator = potentialDuplicatePeople.iterator();
      if (iterator.hasNext()) {
         while (iterator.hasNext()) {
            Person potentialDuplicatePerson = (Person) iterator.next();
            if (hasSameUserId(person, potentialDuplicatePerson)) {
               throw new DuplicateUserIdException();
            }
         }
      }
   }

   boolean hasSameUserId(Person person1, Person person2) {
//     if(SecurityHelper.isAuthenticationCaseSensitive())
//       return person1.getUserId().equals(person2.getUserId());
//     else
       return person1.getUserId().equalsIgnoreCase(person2.getUserId());
   }
}
