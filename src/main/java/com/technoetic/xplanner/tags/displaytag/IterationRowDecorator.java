package com.technoetic.xplanner.tags.displaytag;

import com.technoetic.xplanner.domain.Iteration;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 1, 2005
 * Time: 9:45:32 PM
 */
public class IterationRowDecorator implements RowDecorator
{
    public String getCssClasses(Row row)
    {
        Iteration iteration = (Iteration) row.getObject();
        if (iteration.isActive())
        {
            return "iteration_current";
        }
        return "";
    }
}
