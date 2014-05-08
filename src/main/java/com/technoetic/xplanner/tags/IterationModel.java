package com.technoetic.xplanner.tags;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import net.sf.hibernate.HibernateException;

public class IterationModel {
    private Iteration iteration;

    public IterationModel(Iteration iteration) {
        this.iteration = iteration;
    }

    public String getName() {
        return getProject().getName() + " :: " + iteration.getName();
    }

    public int getId() {
        return iteration.getId();
    }


    protected Project getProject() {
        try {
            return (Project)ThreadSession.get().load((Project.class), new Integer(iteration.getProjectId()));
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IterationModel)) return false;

        final IterationModel option = (IterationModel)o;

        if (!iteration.equals(option.iteration)) return false;

        return true;
    }

    public int hashCode() {
        return iteration.hashCode();
    }

    public String toString() {
        return "Option{" +
                "iteration=" +
                iteration +
                "}";
    }
}
