package com.technoetic.xplanner.domain.repository;

import com.technoetic.xplanner.domain.Attribute;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class AttributeRepositoryImpl implements AttributeRepository {
    public void setAttribute(int targetId, String name, String value) throws RepositoryException {
        try {
            final Session session = ThreadSession.get();
            final Attribute attribute = new Attribute(targetId, name, value);
            List existingAttributes = session.find("from a in "+Attribute.class+
                    " where targetId = ? and name= ?",
                    new Object[]{ new Integer(targetId), name },
                    new Type[]{ Hibernate.INTEGER, Hibernate.STRING });
            if (existingAttributes.size() == 0) {
                session.save(attribute);
            } else {
                session.update(attribute);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (HibernateException e) {
            throw new RepositoryException(e);
        }
    }

    public String getAttribute(int targetId, String name) throws RepositoryException {
        try {
            final Session session = ThreadSession.get();
            final Attribute id = new Attribute(targetId, name, null);
            Attribute attribute = (Attribute)session.load(Attribute.class, id);
            return attribute.getValue();
        } catch (net.sf.hibernate.ObjectNotFoundException e) {
            return null;
        } catch (RuntimeException e) {
            throw e;
        } catch (HibernateException e) {
            throw new RepositoryException(e);
        }
    }

    public Map getAttributes(int targetId, String prefix) throws RepositoryException {
        HashMap attributes = new HashMap();
        try {
            final Session session = ThreadSession.get();
            String pattern = (prefix != null ? prefix : "")+"%";
            List attributeObjects = session.find("from a in "+Attribute.class+
                    " where targetId = ? and name like ?",
                    new Object[]{ new Integer(targetId), pattern },
                    new Type[]{ Hibernate.INTEGER, Hibernate.STRING });
            for (int i = 0; i < attributeObjects.size(); i++) {
                Attribute attribute = (Attribute)attributeObjects.get(i);
                String name = attribute.getName();
                if (StringUtils.isNotEmpty(prefix)) {
                    name = name.replaceAll("^"+prefix, "");
                }
                attributes.put(name, attribute.getValue());
            }
            return attributes;
        } catch (RuntimeException e) {
            throw e;
        } catch (HibernateException e) {
            throw new RepositoryException(e);
        }
    }
    public void delete(int targetId, String name) throws RepositoryException {
        try {
            ThreadSession.get().delete("from a in "+Attribute.class+
                    " where a.targetId = ? and a.name = ?",
                    new Object[]{ new Integer(targetId), name },
                    new Type[]{ Hibernate.INTEGER, Hibernate.STRING });
        } catch (RuntimeException e) {
            throw e;
        } catch (HibernateException e) {
            throw new RepositoryException(e);
        }
    }
}
