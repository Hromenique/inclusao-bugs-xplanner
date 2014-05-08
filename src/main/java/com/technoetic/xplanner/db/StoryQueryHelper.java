package com.technoetic.xplanner.db;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

import java.util.Collection;
import java.util.Date;

import net.sf.hibernate.HibernateException;

/**
 * TODO move to story repository.
 * 
 * @see com.technoetic.xplanner.domain.repository.StoryRepositoryHibernate
 */
public class StoryQueryHelper {
    private int personId;

    public StoryQueryHelper() {
    }

    public StoryQueryHelper(int personId) {
        this.personId = personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Collection getStoriesForCustomer() throws QueryException {
        return bindAndExecuteQuery("stories.customer");
    }

    public Collection getStoriesForTracker() throws QueryException {
        return bindAndExecuteQuery("stories.tracker");
    }

    private Collection bindAndExecuteQuery(String queryName) throws QueryException {
        try {
            return ThreadSession.get().getNamedQuery(queryName).
                    setInteger("personId", personId).setDate("date", new Date()).list();
        } catch (HibernateException e) {
            throw new QueryException(e);
        }
    }
}
