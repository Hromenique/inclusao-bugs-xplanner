package com.technoetic.xplanner.tags.displaytag;

import com.technoetic.xplanner.domain.Task;
import org.displaytag.decorator.TableDecorator;


public class AllTasksTableDecorator extends TableDecorator
{
    public TaskOrdering getStatusOrder()
    {
        return new TaskOrdering((Task) getCurrentRowObject());
    }
}
