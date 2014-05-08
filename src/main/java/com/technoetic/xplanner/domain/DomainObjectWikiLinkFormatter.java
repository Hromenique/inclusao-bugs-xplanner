/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: May 28, 2005
 * Time: 5:49:35 PM
 */
package com.technoetic.xplanner.domain;

import java.util.Map;
import java.util.HashMap;

public class DomainObjectWikiLinkFormatter {
   String fromText;
   String toText;

   Map schemeByClass = new HashMap();

   public DomainObjectWikiLinkFormatter() {
      initSchemeByClassMap();
   }

   private void initSchemeByClassMap() {
      schemeByClass.put(Project.class, "project");
      schemeByClass.put(Iteration.class, "iteration");
//      schemeByClass.put(Feature.class, "feature");
      schemeByClass.put(UserStory.class, "story");
      schemeByClass.put(Task.class, "task");
   }

   public String format(DomainObject object) {
      String link = "";
      if (object != null) {
         String scheme = (String) schemeByClass.get(object.getClass());
         link = scheme + ":" + object.getId();
      }
      return link;
   }
}