package com.technoetic.xplanner.tags.db;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import net.sf.hibernate.Session;

import javax.servlet.jsp.tagext.TagSupport;

public class DatabaseTagSupport extends TagSupport {
    protected Session getSession() throws Exception {
        return HibernateHelper.getSession(pageContext.getRequest());
    }
}
