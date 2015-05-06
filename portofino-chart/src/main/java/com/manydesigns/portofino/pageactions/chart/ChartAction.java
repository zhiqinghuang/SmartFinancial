package com.manydesigns.portofino.pageactions.chart;

import com.manydesigns.portofino.pageactions.annotations.ConfigurationClass;
import com.manydesigns.portofino.pageactions.chart.jfreechart.configuration.JFreeChartConfiguration;
import com.manydesigns.portofino.pageactions.chart.jfreechart.JFreeChartAction;
import com.manydesigns.portofino.security.AccessLevel;
import com.manydesigns.portofino.security.RequiresPermissions;

@RequiresPermissions(level = AccessLevel.VIEW)
@ConfigurationClass(JFreeChartConfiguration.class)
@Deprecated
public class ChartAction extends JFreeChartAction {
}