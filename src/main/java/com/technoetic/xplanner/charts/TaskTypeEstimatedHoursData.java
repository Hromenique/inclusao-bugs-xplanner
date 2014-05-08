package com.technoetic.xplanner.charts;

import java.util.*;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

public class TaskTypeEstimatedHoursData extends XplannerPieChartData
{
   protected Hashtable getData(IterationStatisticsQuery statistics)
   {
      return statistics.getTaskEstimatedHoursByType();
   }
}

