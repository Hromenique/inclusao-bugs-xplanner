package com.technoetic.mocks.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.exception.SQLExceptionConverter;

import java.io.Serializable;

public class MockSessionFactory implements SessionFactory {

    public boolean openSessionCalled;
    public net.sf.hibernate.Session openSessionReturn;

    public net.sf.hibernate.Session openSession() {
        openSessionCalled = true;
        return openSessionReturn;
    }

    public boolean openSession2Called;
    public java.sql.Connection openSession2Connection;
    public net.sf.hibernate.Session openSession2Return;

    public net.sf.hibernate.Session openSession(java.sql.Connection connection) {
        openSession2Called = true;
        openSession2Connection = connection;
        return openSession2Return;
    }

    public boolean openSession3Called;
    public net.sf.hibernate.Session openSession3Return;
    public net.sf.hibernate.Interceptor openSession3Interceptor;

    public net.sf.hibernate.Session openSession(net.sf.hibernate.Interceptor interceptor) {
        openSession3Called = true;
        openSession3Interceptor = interceptor;
        return openSession3Return;
    }

    public boolean openSession4Called;
    public net.sf.hibernate.Session openSession4Return;
    public net.sf.hibernate.Interceptor openSession4Interceptor;
    public java.sql.Connection openSession4Connection;

    public net.sf.hibernate.Session openSession(java.sql.Connection connection, net.sf.hibernate.Interceptor interceptor) {
        openSession4Called = true;
        openSession4Connection = connection;
        openSession4Interceptor = interceptor;
        return openSession4Return;
    }


    public boolean openDatabinderCalled;
    public net.sf.hibernate.Databinder openDatabinderReturn;
    public net.sf.hibernate.HibernateException openDatabinderHibernateException;

    public net.sf.hibernate.Databinder openDatabinder() throws net.sf.hibernate.HibernateException {
        openDatabinderCalled = true;
        if (openDatabinderHibernateException != null) {
            throw openDatabinderHibernateException;
        }
        return openDatabinderReturn;
    }

    public boolean getClassMetadataCalled;
    public net.sf.hibernate.metadata.ClassMetadata getClassMetadataReturn;
    public net.sf.hibernate.HibernateException getClassMetadataHibernateException;
    public java.lang.Class getClassMetadataPersistentClass;

    public net.sf.hibernate.metadata.ClassMetadata getClassMetadata(java.lang.Class persistentClass) throws net.sf.hibernate.HibernateException {
        getClassMetadataCalled = true;
        getClassMetadataPersistentClass = persistentClass;
        if (getClassMetadataHibernateException != null) {
            throw getClassMetadataHibernateException;
        }
        return getClassMetadataReturn;
    }

    public boolean getCollectionMetadataCalled;
    public net.sf.hibernate.metadata.CollectionMetadata getCollectionMetadataReturn;
    public net.sf.hibernate.HibernateException getCollectionMetadataHibernateException;
    public java.lang.String getCollectionMetadataRoleName;

    public net.sf.hibernate.metadata.CollectionMetadata getCollectionMetadata(java.lang.String roleName) throws net.sf.hibernate.HibernateException {
        getCollectionMetadataCalled = true;
        getCollectionMetadataRoleName = roleName;
        if (getCollectionMetadataHibernateException != null) {
            throw getCollectionMetadataHibernateException;
        }
        return getCollectionMetadataReturn;
    }

    public boolean getAllClassMetadataCalled;
    public java.util.Map getAllClassMetadataReturn;
    public net.sf.hibernate.HibernateException getAllClassMetadataHibernateException;

    public java.util.Map getAllClassMetadata() throws net.sf.hibernate.HibernateException {
        getAllClassMetadataCalled = true;
        if (getAllClassMetadataHibernateException != null) {
            throw getAllClassMetadataHibernateException;
        }
        return getAllClassMetadataReturn;
    }

    public boolean getAllCollectionMetadataCalled;
    public java.util.Map getAllCollectionMetadataReturn;
    public net.sf.hibernate.HibernateException getAllCollectionMetadataHibernateException;

    public java.util.Map getAllCollectionMetadata() throws net.sf.hibernate.HibernateException {
        getAllCollectionMetadataCalled = true;
        if (getAllCollectionMetadataHibernateException != null) {
            throw getAllCollectionMetadataHibernateException;
        }
        return getAllCollectionMetadataReturn;
    }

    public boolean getReferenceCalled;
    public javax.naming.Reference getReferenceReturn;
    public javax.naming.NamingException getReferenceNamingException;

    public javax.naming.Reference getReference() throws javax.naming.NamingException {
        getReferenceCalled = true;
        if (getReferenceNamingException != null) {
            throw getReferenceNamingException;
        }
        return getReferenceReturn;
    }

    public void close() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evict(Class persistentClass) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictCollection(String roleName) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictQueries() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictQueries(String cacheRegion) throws HibernateException {
        throw new UnsupportedOperationException();
    }

  public SQLExceptionConverter getSQLExceptionConverter() {
    return null;
  }
}
