package com.technoetic.xplanner.tags;

import com.technoetic.xplanner.XPlannerProperties;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class ProgressBarDelegatorTag implements ProgressBarTag {
    private Logger log = Logger.getLogger(getClass());
    private ProgressBarTag delegate;
    private static final String HTML_TYPE = "html";
    private static final String IMAGE_TYPE = "image";

    public ProgressBarDelegatorTag() {
       String type = new XPlannerProperties().getProperty("xplanner.progressbar.impl", HTML_TYPE);
        if (StringUtils.equalsIgnoreCase(type, HTML_TYPE)) {
            delegate = new ProgressBarHtmlTag();
        } else if (StringUtils.equalsIgnoreCase(type, IMAGE_TYPE)) {
            delegate = new ProgressBarImageTag();
        } else {
            log.error("unrecognized progress bar type, using HTML by default: type=" + type);
        }
    }

    public void setActual(double actual) {
        delegate.setActual(actual);
    }

    public void setComplete(boolean complete) {
        delegate.setComplete(complete);
    }

    public void setEstimate(double estimate) {
        delegate.setEstimate(estimate);
    }

    public void setHeight(int height) {
        delegate.setHeight(height);
    }

    public void setWidth(int width) {
        delegate.setWidth(width);
    }

    public int doEndTag() throws JspException {
        return delegate.doEndTag();
    }

    public int doStartTag() throws JspException {
        return delegate.doStartTag();
    }

    public Tag getParent() {
        return delegate.getParent();
    }

    public void release() {
        delegate.release();
    }

    public void setPageContext(PageContext pageContext) {
        delegate.setPageContext(pageContext);
    }

    public void setParent(Tag tag) {
        delegate.setParent(tag);
    }
}
