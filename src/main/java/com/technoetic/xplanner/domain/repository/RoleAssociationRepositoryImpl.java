package com.technoetic.xplanner.domain.repository;

import com.technoetic.xplanner.domain.Role;
import com.technoetic.xplanner.domain.RoleAssociation;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;

public class RoleAssociationRepositoryImpl extends HibernateObjectRepository
        implements RoleAssociationRepository {
    private RoleRepository roleRepository;

    public RoleAssociationRepositoryImpl() throws HibernateException {
        super(RoleAssociation.class);
    }

    public void deleteAllForPersonOnProject(int personId, int projectId) throws RepositoryException {
       getHibernateTemplate().delete("from assoc in " + RoleAssociation.class +
                                     " where assoc.personId = ? and assoc.projectId = ?",
                                     new Object[]{new Integer(personId), new Integer(projectId)},
                                     new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    }

    public void deleteForPersonOnProject(String roleName, int personId, int projectId) throws RepositoryException {
        Role role = roleRepository.findRoleByName(roleName);
       getHibernateTemplate().delete("from assoc in " + RoleAssociation.class +
                                     " where assoc.roleId = ? and assoc.projectId = ? and assoc.personId = ?",
                                     new Object[]{new Integer(role.getId()), new Integer(projectId),
                                                  new Integer(personId)},
                                     new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});
    }

    public void insertForPersonOnProject(String roleName, int personId, int projectId)
            throws RepositoryException {
        Role role = roleRepository.findRoleByName(roleName);
        if (role != null) {
           getHibernateTemplate().save(new RoleAssociation(projectId, personId, role.getId()));
        }
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
