package com.technoetic.xplanner.tags.displaytag;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Mar 1, 2005
 * Time: 9:45:55 PM
 */

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.IterationStatus;
import junit.framework.TestCase;

public class TestIterationRowDecorator extends TestCase
{
    IterationRowDecorator iterationRowDecorator;

    public void testGetCssClasses() throws Exception
    {
        iterationRowDecorator = new IterationRowDecorator();
        Iteration iteration = new Iteration();
        iteration.setStatus(IterationStatus.INACTIVE);
        Row row = new Row(iteration, 1);
        assertEquals("", iterationRowDecorator.getCssClasses(row));
        iteration.setStatus(IterationStatus.ACTIVE);
        assertEquals("iteration_current", iterationRowDecorator.getCssClasses(row));
    }
}