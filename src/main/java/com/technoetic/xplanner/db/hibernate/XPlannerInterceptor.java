package com.technoetic.xplanner.db.hibernate;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

public class XPlannerInterceptor implements Interceptor {
    private static String LAST_UPDATE_TIME = "lastUpdateTime";

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return false;
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return setLastUpdateTime(propertyNames, currentState);
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return setLastUpdateTime(propertyNames, state);
    }

    private boolean setLastUpdateTime(String[] propertyNames, Object[] state) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (LAST_UPDATE_TIME.equals(propertyNames[i])) {
                state[i] = new Date();
                return true;
            }
        }
        return false;
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        // empty
    }

    public void preFlush(Iterator entities) {
        // empty
    }

    public void postFlush(Iterator entities) {
        // empty
    }

    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        return null;
    }

    public Object instantiate(Class clazz, Serializable id) throws CallbackException {
        return null;
    }

    public Boolean isUnsaved(Object entity) {
        return null;
    }

}
