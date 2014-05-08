package com.technoetic.xplanner.tags;

import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.tiles.DirectStringAttribute;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

public class ContentTag extends InsertTag implements BodyTag {
    private BodyContent bodyContent;

    public int doStartTag() throws JspException {
        if (PrintLinkTag.isInPrintMode(pageContext)) {
            definitionName = "tiles:print";
        }
        if (definitionName == null) {
            definitionName = "tiles:default";
        }
        super.doStartTag();
        return EVAL_BODY_BUFFERED;
    }

    public void doInitBody() throws JspException {
        // empty
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public int doEndTag() throws JspException {
        putAttribute("body", new DirectStringAttribute(bodyContent.getString()));
        return super.doEndTag();
    }

    public void release() {
        bodyContent = null;
        super.release();
    }
}
