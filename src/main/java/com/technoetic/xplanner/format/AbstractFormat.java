package com.technoetic.xplanner.format;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import com.technoetic.xplanner.util.LocaleUtil;

public class AbstractFormat {

    public static String getFormat(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();
       Locale locale = LocaleUtil.getLocale(session);
       MessageResources resources = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
        return resources.getMessage(locale, key);
    }

}