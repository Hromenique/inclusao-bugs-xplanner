package com.technoetic.xplanner.domain.repository;

import static org.easymock.EasyMock.*;
import net.sf.hibernate.Hibernate;

import org.springframework.orm.hibernate.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.DummyDomainObject;

public class TestHibernateObjectRepository extends AbstractUnitTestCase {
   private HibernateObjectRepository repository;
   private HibernateTemplate mockHibernateTemplate;
   private DomainObject domainObject;

   protected void setUp() throws Exception {
      super.setUp();
      mockHibernateTemplate = createLocalMock(HibernateTemplate.class);
      repository = new HibernateObjectRepository(Object.class);
      repository.setHibernateTemplate(mockHibernateTemplate);
      domainObject = new DummyDomainObject() ;

   }

   protected void tearDown() throws Exception {
      ThreadSession.set(null);
      super.tearDown();
   }

   public void testDelete() throws Exception {
      expect(mockHibernateTemplate.delete("from object in class java.lang.Object where id = ?",
                                                            new Integer(10), Hibernate.INTEGER)).andReturn(1);
      replay();

      repository.delete(10);

      verify();
   }

   public void testLoad() throws Exception {
      Object object = new Object();
      expect(mockHibernateTemplate.load(Object.class, new Integer(1))).andReturn(object);
      replay();

      Object loadedObject = repository.load(1);

      verify();
      assertSame(object, loadedObject);
   }

   public void testInsert() throws Exception {
      expect(mockHibernateTemplate.save(domainObject)).andReturn(new Integer(44));
      replay();

      int id = repository.insert(domainObject);

      verify();
      assertEquals("wrong id", 44, id);
   }

   public void testUpdate() throws Exception {
      replay();

      repository.update(domainObject);

      verify();
   }

}
