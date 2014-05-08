package com.technoetic.xplanner.tags;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.taglib.tiles.ComponentConstants;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

public class TilesHelper {
    public static Object getAttribute(String name, PageContext pageContext) throws JspException {
        ComponentContext compContext = (ComponentContext) pageContext.getAttribute(
                ComponentConstants.COMPONENT_CONTEXT, PageContext.REQUEST_SCOPE);

        if (compContext == null) {
            throw new JspException("Error - tag.getAttribute : component context is not defined. Check tag syntax");
        }

        return compContext.getAttribute(name);
    }
}
