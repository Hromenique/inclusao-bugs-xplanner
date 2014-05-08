package com.sabre.security.jndi;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Oct 14, 2005
 * Time: 3:14:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends Exception {
   private Map errorByModule = new HashMap();

   public AuthenticationException() {
   }

    public AuthenticationException(String message) {
        super(message);
        errorByModule.put("Default", message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        errorByModule.put("", message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

   public AuthenticationException(Map errorByModule) {
      this.errorByModule = errorByModule;
   }

   public Map getErrorsByModule()
   {
       return this.errorByModule;
   }
   
}
