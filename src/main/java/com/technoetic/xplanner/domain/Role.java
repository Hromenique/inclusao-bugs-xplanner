package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.security.Principal;

public class Role extends DomainObject implements Principal, Serializable {
   public static final String SYSADMIN = "sysadmin";
   public static final String ADMIN = "admin";
   public static final String EDITOR = "editor";
   public static final String VIEWER = "viewer";
    private String name;
    private int left;
    private int right;

    Role() {
        // for Hibernate
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

   public boolean isSysadmin() {
      return name.equals(SYSADMIN);
   }

   public String getDescription() {return "";}
}
