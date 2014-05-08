package com.technoetic.xplanner.db;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.UserStory;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class CustomerStoriesQuery {
    private Logger log = Logger.getLogger(getClass());
    private int personId;
    private Collection stories;
    private Collection myStories;
//DEBT Why static? Should the query go to either Story.xml or Person.xml?
    private static String query;

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public Collection getMyStories() {
        if(myStories == null) {
            myStories = getStories();
        }
        return myStories;
    }

    private Collection getStories() {
        Session session = ThreadSession.get();
        if(stories == null) {
            try
            {
                if(session == null) {
                        log.error("no Hibernate session provided, ignoring "+this);
                        return Collections.EMPTY_LIST;
                }
                try {
                    if(query == null) {
                        query = "select distinct story from " +
                                "story in " + UserStory.class + "," +
                                "iteration in " + Iteration.class + "," +
                                "person in " + Person.class +
                                " where iteration.id = story.iterationId and iteration.endDate > ? and " +
                                "person.id = story.customer.id and person.id = ?";
                    }
                    stories = session.find(query,
                            new Object[] { new Date(), new Integer(personId) },
                            new Type[] { Hibernate.DATE, Hibernate.INTEGER});
                } catch (Exception ex) {
                        log.error("query error", ex);
                } finally {
                    session.connection().rollback();
                }
            }
            catch (Exception e) {
                log.error("error in CustomerStoriesQuery", e);
            }

        }
        return stories;
    }
}
