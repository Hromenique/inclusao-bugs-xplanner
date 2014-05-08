package com.technoetic.xplanner.db;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Task;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlannedTaskQuery {
    private Logger log = Logger.getLogger(getClass());
    private static String query;
    private static String futureQuery;
    private java.util.Collection tasks = new ArrayList();
    private int personId;

    public boolean hasCurrentTasks() throws QueryException {
        return getTasks().size() > 0;
    }

    public java.util.Collection getTasks() throws QueryException {
        if (personId == 0) {
            throw new QueryException("no person specified for query");
        }
        Session session = ThreadSession.get();
        try {
            if (session == null) {
                log.error("no Hibernate session provided, ignoring " + this);
                return Collections.EMPTY_LIST;
            }
            try {
                if (query == null) {
                    query =
                            "select distinct task "
                            + " from task in class com.technoetic.xplanner.domain.Task, "
                            + " iteration in class com.technoetic.xplanner.domain.Iteration, "
                            + " story in class com.technoetic.xplanner.domain.UserStory,"
                            + " person in class com.technoetic.xplanner.domain.Person "
                            + " where task.story.id = story.id and story.iterationId = iteration.id and"
                            + " (iteration.startDate <= ? and iteration.endDate >= ?) and"
                            + " task.acceptorId = person.id and person.id = ? and task.completed = 'false'";
                }
                Date now = new Date();
                List acceptedTasks =
                        session.find(
                                query,
                                new Object[]{now, now, new Integer(personId)},
                                new Type[]{Hibernate.DATE, Hibernate.DATE, Hibernate.INTEGER});
                Iterator itr = acceptedTasks.iterator();
                tasks.clear();
                while (itr.hasNext()) {
                    Task task = (Task)itr.next();
                    if (task.getTimeEntries().size() == 0) {
                        tasks.add(task);
                    }
                }
            } catch (Exception ex) {
                log.error("query error", ex);
            } finally {
                session.connection().rollback();
            }
        } catch (Exception ex) {
            log.error("error in PlannedTaskQuery", ex);
        }
        return tasks;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public boolean hasFutureTasks() throws QueryException {
        return getFutureTasks().size() > 0;
    }

    public java.util.Collection getFutureTasks() throws QueryException {
        if (personId == 0) {
            throw new QueryException("no person specified for query");
        }
        Session session = ThreadSession.get();
        try {
            if (session == null) {
                log.error("no Hibernate session provided, ignoring " + this);
                return Collections.EMPTY_LIST;
            }
            try {
                if (futureQuery == null) {
                    futureQuery =
                            "select distinct task "
                            + " from task in class com.technoetic.xplanner.domain.Task, "
                            + " iteration in class com.technoetic.xplanner.domain.Iteration, "
                            + " story in class com.technoetic.xplanner.domain.UserStory,"
                            + " person in class com.technoetic.xplanner.domain.Person "
                            + " where task.story.id = story.id and story.iterationId = iteration.id and"
                            + " iteration.startDate > ? and"
                            + " task.acceptorId = person.id and"
                            + " person.id = ? and task.completed = 'false'";
                }
                Date now = new Date();
                return session.find(
                        futureQuery,
                        new Object[]{now, new Integer(personId)},
                        new Type[]{Hibernate.DATE, Hibernate.INTEGER});
            } catch (Exception ex) {
                log.error("query error", ex);
            } finally {
                session.connection().rollback();
            }
        } catch (Exception ex) {
            log.error("error in PlannedTaskQuery", ex);
        }
        return Collections.EMPTY_LIST;
    }
}
