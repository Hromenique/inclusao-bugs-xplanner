package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletException;

public class XPlannerInitializationPlugin implements PlugIn {
    private static Logger log = Logger.getLogger(XPlannerInitializationPlugin.class);
    private static boolean isInitialized = false;

    public void init(ActionServlet actionservlet, ModuleConfig moduleConfig)
        throws ServletException {
        if (!isInitialized) {
            log.info("initialization started");
            try {
                HibernateHelper.initializeHibernate();
            } catch (Throwable e) {
                throw new ServletException(e);
            }
            log.info("initialization completed");
            isInitialized = true;
        }
    }

    public void destroy() {
    }
}
