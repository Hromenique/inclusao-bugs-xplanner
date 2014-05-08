package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import org.apache.commons.lang.StringUtils;

import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.security.AuthenticationException;

public class PersonOptionsTag extends OptionsTag {
    private String filtered;
    private int projectId;
   public static final String ALL_ACTIVE_PEOPLE_QUERY = "from p in "+ Person.class+" order by p.name where p.hidden = false";

   public void release() {
       super.release();
       filtered = null;
       projectId = 0;
   }

    protected List getOptions() throws HibernateException, AuthenticationException {
        List allPeople = fetchAllPersons();
        List selectedPeople;
        int projectId = findProjectId();
        if (projectId != 0 && isFilteringRequested()) {
            selectedPeople = new ArrayList();
            selectedPeople.addAll(getAuthorizer().getPeopleWithPermissionOnProject(allPeople, projectId));
        } else {
            selectedPeople = allPeople;
        }
        return selectedPeople;
    }

   private List fetchAllPersons() throws HibernateException {
      Query query = getSession().createQuery(ALL_ACTIVE_PEOPLE_QUERY);
      //query.setCacheable(true);
      return query.list();
   }

    private boolean isFilteringRequested() {
        return filtered == null || Boolean.valueOf(filtered).booleanValue();
    }

    public String getFiltered() {
        return filtered;
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int findProjectId() {
        int projectId = this.projectId;
        if (projectId == 0) {
            DomainContext context = DomainContext.get(pageContext.getRequest());
            if (context != null) {
                projectId = context.getProjectId();
            }
        }
        String id = pageContext.getRequest().getParameter("projectId");
        if (projectId == 0 && !StringUtils.isEmpty(id)) {
            projectId = Integer.parseInt(id);
        }
        return projectId;
    }

}
