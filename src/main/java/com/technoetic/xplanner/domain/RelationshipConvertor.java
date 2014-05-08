package com.technoetic.xplanner.domain;

import java.lang.reflect.InvocationTargetException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.beanutils.PropertyUtils;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

//DEBT(METADATA) Rename this class to illustrate its responsability

/**
 * @resp Convert a DTO (Form, SOAP data object) attribute to a Domain object relationship and vise-a-versa
 */
public class RelationshipConvertor {
    private String adapterProperty;
    private String domainProperty;

    public RelationshipConvertor(String adapterProperty, String domainObjectProperty) {
        this.adapterProperty = adapterProperty;
        this.domainProperty = domainObjectProperty;
    }

    public String getAdapterProperty() {
        return adapterProperty;
    }

    public String getDomainProperty() {
        return domainProperty;
    }

    public void populateDomainObject(DomainObject target, Object adapter) throws HibernateException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        populateDomainObject(target, adapter, ThreadSession.get());
    }

    public void populateDomainObject(DomainObject destination, Object source, Session session) throws HibernateException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        if (PropertyUtils.isReadable(source, adapterProperty) &&
                PropertyUtils.isWriteable(destination, domainProperty)) {
            Integer referredId = (Integer)PropertyUtils.getProperty(source, adapterProperty);
            Class destinationType = PropertyUtils.getPropertyType(destination, domainProperty);
            Object referred = findObjectById(session, destinationType, referredId);
            PropertyUtils.setProperty(destination, domainProperty, referred);
        }
    }

    private Object findObjectById(Session session, Class aClass, Integer id) throws HibernateException {
        if (id.intValue() == 0) return null;
        return session.load(aClass, id);
    }

    public void populateAdapter(Object adapter, DomainObject domainObject) throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {
        Object referred = PropertyUtils.getProperty(domainObject, domainProperty);
        Integer id = referred == null ?
                new Integer(0) : (Integer)PropertyUtils.getProperty(referred, "id");
        PropertyUtils.setProperty(adapter, adapterProperty, id);
    }

}