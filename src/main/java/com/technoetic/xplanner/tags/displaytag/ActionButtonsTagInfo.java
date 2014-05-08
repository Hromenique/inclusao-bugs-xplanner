package com.technoetic.xplanner.tags.displaytag;

import com.technoetic.xplanner.views.ActionRenderer;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class ActionButtonsTagInfo extends TagExtraInfo {

    public ActionButtonsTagInfo() {
    }

    public VariableInfo[] getVariableInfo(TagData data) {
        List variables = new ArrayList(4);
        Object tagId = data.getAttributeString("id");
        if (tagId != null) {
            variables.add(
                new VariableInfo(tagId.toString(), ActionRenderer.class.getName(), true, 0));
        }
        return (VariableInfo[]) variables.toArray(new VariableInfo[0]);
    }
}
