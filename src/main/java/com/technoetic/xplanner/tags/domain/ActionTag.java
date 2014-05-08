/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.tags.domain;

import java.io.StringWriter;
import javax.servlet.jsp.JspException;

import org.apache.struts.Globals;
import org.apache.struts.taglib.html.ImgTag;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.tags.LinkTag;
import com.technoetic.xplanner.views.ActionRenderer;

//DEBT(DATADRIVEN) Move the responsability of configuring the link to the actionrender (i.e. it is a strategy object)
public class ActionTag extends LinkTag {
   String action;
   ActionRenderer actionRenderer;
   DomainObject targetBean;

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public ActionRenderer getActionRenderer() {
      return actionRenderer;
   }

   public void setActionRenderer(ActionRenderer actionRenderer) {
      this.actionRenderer = actionRenderer;
   }

   public DomainObject getTargetBean() {
      return targetBean;
   }

   public void setTargetBean(DomainObject targetBean) {
      this.targetBean = targetBean;
   }

   public int doStartTag() throws JspException {
      setPage("/do/" + actionRenderer.getTargetPage());
      setUseReturnto(actionRenderer.useReturnTo());
      setOnclick(actionRenderer.getOnclick());
      if (!actionRenderer.shouldPassOidParam())
         setFkey(0);
      int result = super.doStartTag();
      if (actionRenderer.shouldPassOidParam())
         addRequestParameter("oid", String.valueOf(targetBean.getId()));
      if (actionRenderer.isDisplayedAsIcon())
         renderAsIcon();
      else
         renderAsText();
      return result;
   }

   public int doEndTag() throws JspException {
      return super.doEndTag();
   }

   private void renderAsIcon() throws JspException {
      StringWriter stringWriter = new StringWriter();
      pageContext.pushBody(stringWriter);
      ImgTag imgTag = new ImgTag();
      imgTag.setPageContext(pageContext);
      imgTag.setPage(actionRenderer.getIconPath());
      imgTag.setBorder("0");
//      imgTag.setAlt(getActionName());
      imgTag.doStartTag();
      imgTag.doEndTag();
      pageContext.popBody();
      text = stringWriter.toString();
   }

   private void renderAsText() throws JspException {text = getActionName();}

   private String getActionName() throws JspException {
      return RequestUtils.message(pageContext, null, Globals.LOCALE_KEY, actionRenderer.getTitleKey(), null);
   }

   public void release() {
      super.release();
      actionRenderer = null;
      text = null;
   }
}
