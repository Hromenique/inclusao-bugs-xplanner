package com.technoetic.xplanner.charts.timesheet;

import org.jfree.data.DefaultPieDataset;
import com.technoetic.xplanner.domain.virtual.Timesheet;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class PersonTimesheetProjectData implements DatasetProducer {
    private DefaultPieDataset dataSet = new DefaultPieDataset();

    public void setTimesheet(Timesheet timesheet) {
        Hashtable projectData = timesheet.getProjectData();
        for (Enumeration keys = projectData.keys(); keys.hasMoreElements();) {
            String project = (String)keys.nextElement();
            BigDecimal value = ((BigDecimal)projectData.get(project))
                    .setScale(0, BigDecimal.ROUND_HALF_EVEN);
            dataSet.setValue(project + " (" + value + ")", value);
        }
    }

    public Object produceDataset(Map params) throws DatasetProduceException {
        return dataSet;
    }

    public boolean hasExpired(Map params, Date since) {
        return true;
    }

    public String getProducerId() {
        return this.getClass().getName();
    }
}
