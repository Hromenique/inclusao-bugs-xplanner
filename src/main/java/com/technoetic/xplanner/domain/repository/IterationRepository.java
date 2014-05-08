package com.technoetic.xplanner.domain.repository;

import java.util.*;
import java.util.Date;

import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import net.sf.hibernate.*;
import org.apache.log4j.Logger;

//TODO Should be turn into a Query object
//TODO Create a spring context including IterationRepository and others for every web session (logged in user)
public class IterationRepository {
   private static Logger log = Logger.getLogger(IterationRepository.class);
    public static final String EDITABLE_ITERATIONS_QUERY_STRING =
            "select i from " + Iteration.class.getName() + " i, " + Project.class.getName() + " p " +
            "where p.hidden = false and i.projectId = p.id order by p.name, i.startDate";
    private Session session;
    private int loggedInUser;
    private Authorizer authorizer;

    public IterationRepository(Session session, Authorizer authorizer, int loggedInUser) {
        this.session = session;
        this.loggedInUser = loggedInUser;
        this.authorizer = authorizer;
    }

    private boolean canUserEditIteration(Iteration iteration) throws AuthenticationException {
        return authorizer.hasPermission(iteration.getProjectId(), loggedInUser, iteration, "edit");
    }

    public List fetchEditableIterations() throws HibernateException, AuthenticationException {
        List allIterations = getSession().getNamedQuery("com.technoetic.xplanner.domain.GetEditableIterationQuery").list();
        List acceptedIterations = new ArrayList(allIterations.size());
        for (int i = 0; i < allIterations.size(); i++) {
            Iteration it = (Iteration)allIterations.get(i);
            if (canUserEditIteration(it)) {
                acceptedIterations.add(it);
            }
        }
        return acceptedIterations;
    }

    public List fetchEditableIterations(int projectId, Date startingAfter) throws HibernateException, AuthenticationException {
        List allEditableIterations = fetchEditableIterations();
        List acceptedIterations = new ArrayList(allEditableIterations.size());
        for (int i = 0; i < allEditableIterations.size(); i++) {
            Iteration it = (Iteration)allEditableIterations.get(i);
            if (it.getProjectId() == projectId && it.getStartDate().after(startingAfter))
                acceptedIterations.add(it);
        }
        return acceptedIterations;
    }

    protected Session getSession() {
        return session;
    }

    public Iteration getIterationForStory(UserStory story) throws HibernateException {
        return getIteration(story.getIterationId());
    }

    public Iteration getIteration(int iterationId) throws HibernateException {
        return (Iteration) session.load(Iteration.class,new Integer(iterationId));
    }

   public static Iteration getCurrentIteration(int projectId) {
       java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
       try {
          Query getCurrentIterationQuery = ThreadSession.get().getNamedQuery("com.technoetic.xplanner.domain.GetCurrentIterationQuery");
          getCurrentIterationQuery.setParameter(0, new Integer(projectId), Hibernate.INTEGER);
          getCurrentIterationQuery.setParameter(1, now, Hibernate.DATE);
          List results = getCurrentIterationQuery.list();
          return (results.size() > 0) ? (Iteration)results.get(0) : null;
       } catch (Exception e) {
           log.error("error", e);
       }
       return null;
   }
}