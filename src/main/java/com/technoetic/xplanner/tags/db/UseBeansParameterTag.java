package com.technoetic.xplanner.tags.db;

import net.sf.hibernate.type.Type;
import net.sf.hibernate.type.TypeFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class UseBeansParameterTag extends TagSupport {
    private String name;
    private Object value;
    private Type type;

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = TypeFactory.basic(type);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int doEndTag() throws JspException {
        Tag parent = getParent();
        if (parent instanceof UseBeansTag) {
            if (name == null) {
                ((UseBeansTag)parent).addParameter(value, type);
            } else {
                ((UseBeansTag)parent).addParameter(name, value, type);
            }
        }
        return EVAL_PAGE;
    }

    public void release() {
        name = null;
        value = null;
        type = null;
        super.release();
    }
}
