package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;

public class AuthConstraint {
    private ArrayList roleNames = new ArrayList();

    public void addRoleName(String roleName) {
        roleNames.add(roleName);
    }

    public Collection getRoleNames() {
        return roleNames;
    }
}
