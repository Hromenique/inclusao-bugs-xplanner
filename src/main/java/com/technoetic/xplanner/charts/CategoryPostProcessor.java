package com.technoetic.xplanner.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.LineAndShapeRenderer;
import de.laures.cewolf.ChartPostProcessor;

import java.util.Map;


/**
 * Post processor which is used to add shapes to the datapoints of a line graph.
 */
public class CategoryPostProcessor implements ChartPostProcessor {
    public void processChart(Object chart, Map params) {
        CategoryPlot plot = (CategoryPlot)((JFreeChart)chart).getPlot();
        plot.setRenderer(new LineAndShapeRenderer());
    }
}
