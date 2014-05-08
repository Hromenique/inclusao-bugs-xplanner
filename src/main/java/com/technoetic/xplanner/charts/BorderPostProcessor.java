package com.technoetic.xplanner.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import de.laures.cewolf.ChartPostProcessor;

import java.awt.*;
import java.util.Map;

/**
 * Post processor which is used change the border colour of a pie chart.
 */
public class BorderPostProcessor implements ChartPostProcessor {
    public void processChart(Object chart, Map params) {
        PiePlot plot = (PiePlot)((JFreeChart)chart).getPlot();
        plot.setOutlinePaint(Color.WHITE);
    }
}
