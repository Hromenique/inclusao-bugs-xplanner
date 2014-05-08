package com.technoetic.xplanner.tags.displaytag;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.decorator.DecoratorFactory;
import org.displaytag.exception.DecoratorInstantiationException;
import org.displaytag.exception.MissingAttributeException;
import org.displaytag.exception.ObjectLookupException;
import org.displaytag.exception.TagStructureException;
import org.displaytag.model.Cell;
import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.util.Href;
import org.displaytag.util.HtmlAttributeMap;
import org.displaytag.util.MultipleHtmlAttribute;
import org.displaytag.util.TagConstants;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.*;


/**
 * <p>
 * This tag works hand in hand with the TableTag to display a list of objects. This describes a column of data in the
 * TableTag. There can be any number of columns that make up the list.
 * </p>
 * <p>
 * This tag does no work itself, it is simply a container of information. The TableTag does all the work based on the
 * information provided in the attributes of this tag.
 * <p>
 * @author mraible
 * @version $Revision: 408 $ ($Author: sg0897500 $)
 */
public class ColumnTag extends org.displaytag.tags.ColumnTag
{

    public void setHtmlTitle(String htmlTitle)
    {
        this.getHeaderAttributeMap().put(TagConstants.ATTRIBUTE_TITLE, htmlTitle);
    }

}