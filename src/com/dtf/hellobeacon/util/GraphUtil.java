package com.dtf.hellobeacon.util;

import android.graphics.Color;
import pl.flex_it.androidplot.MultitouchPlot;

public class GraphUtil {
	
	// set border and padding
	public static void setBordersandPadding(MultitouchPlot plot){
		
		plot.setBorderStyle(MultitouchPlot.BorderStyle.NONE, null, null);
		plot.setPlotMargins(0, 20, 0, 0);
		plot.setPlotPadding(0, 20, 0, 0);
		plot.setGridPadding(0, 20, 60, 0);
	}
	
	
	// set colors
	public static void setColors(MultitouchPlot plot){
		
		plot.setBackgroundColor(Color.WHITE);
		plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
		plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
	}
	
	// Configure legend
	public static void configureLegend(MultitouchPlot plot){
		
		//remove legend
		plot.getLayoutManager().remove(plot.getLegendWidget());
		plot.getLayoutManager().remove(plot.getDomainLabelWidget());
		plot.getLayoutManager().remove(plot.getRangeLabelWidget());
		plot.getLayoutManager().remove(plot.getTitleWidget());
		
	}
	
}
