package com.technoetic.xplanner.domain.repository;

import com.technoetic.xplanner.domain.Role;

public interface RoleRepository extends ObjectRepository {
    Role findRoleByName(String name) throws RepositoryException;
}
