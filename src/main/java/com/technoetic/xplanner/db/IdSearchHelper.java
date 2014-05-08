package com.technoetic.xplanner.db;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Note;
import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;

public class IdSearchHelper {
    private Class[] searchedDomainClasses = {
        Project.class, Iteration.class, UserStory.class, Task.class, Person.class, Note.class };

    public DomainObject search(int oid) throws HibernateException {
        final Integer id = new Integer(oid);
        for (int i = 0; i < searchedDomainClasses.length; i++) {
           DomainObject o = null;
            try {
               o = (DomainObject) ThreadSession.get().load(searchedDomainClasses[i], id);
            } catch (ObjectNotFoundException e) {
                // ignored
            }
           if (o != null) {
              if (o instanceof Note) o = ((Note) o).getParent();
              return o;
           }
        }
        return null;
    }

}
