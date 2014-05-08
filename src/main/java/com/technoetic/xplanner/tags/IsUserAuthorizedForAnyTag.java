package com.technoetic.xplanner.tags;

import java.util.Collection;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.hibernate.HibernateException;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

/**
 * @jira XPR-15 Cannot deploy xplanner.war to Weblogic 8.1
 */
public class IsUserAuthorizedForAnyTag extends DatabaseTagSupport {
    private Collection collection;
    private String name;
    private String property;
    private String permissions;
    private int projectId;
    private boolean negate;

    public int doStartTag() throws JspException {
        String[] permissionArray = permissions.split(",");
        Collection objects = getCollection();
        boolean isAuthorized = false;
        try {
            isAuthorized =
            AuthorizationHelper.hasPermissionToAny(permissionArray,
                                                   objects,
                                                   pageContext.getRequest(), projectId);
        } catch (Exception e) {
            throw new JspException(e);
        }
        return (negate ? !isAuthorized : isAuthorized) ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
    }

    public void release() {
        collection = null;
        name = null;
        property = null;
        permissions = null;
        projectId = 0;
        negate = false;
        super.release();
    }

    private Collection getCollection() throws JspException {
        if (collection != null) {
            return collection;
        }
        if (name != null) {
            return (Collection) RequestUtils.lookup(pageContext, name, property, null);
        }
        try {
            return ThreadSession.get().find("from project in " + Project.class);
        } catch (HibernateException e) {
            throw new JspException(e);
        }
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

   public int getProjectId() {
      return projectId;
   }

   public void setProjectId(int projectId) {
      this.projectId = projectId;
   }
}
