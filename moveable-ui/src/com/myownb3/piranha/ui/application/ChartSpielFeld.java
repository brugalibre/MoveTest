/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Color;
import java.util.List;

import javax.swing.JComponent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
public class ChartSpielFeld {

   private ChartPanel chartPanel;
   private XYSeries xySeriesMoveableHistory;
   private XYSeries xySeriesObstacles;

   public ChartSpielFeld(List<GridElement> gridElements, List<Obstacle> obstacles) {

      JFreeChart chart = buildJFreeChart();
      paintValues(chart, gridElements, obstacles);

      chartPanel = new ChartPanel(chart);
      chartPanel.setVisible(true);
   }

   private void paintValues(JFreeChart chart, List<GridElement> gridElements, List<Obstacle> obstacles) {

      for (int i = 0; i < gridElements.size(); i = i + 5) {
         GridElement gridElement = gridElements.get(i);
         Position position = gridElement.getPosition();
         xySeriesMoveableHistory.add(position.getX(), position.getY());
      }
      for (Obstacle obstacle : obstacles) {
         Position position = obstacle.getPosition();
         xySeriesObstacles.add(position.getX(), position.getY());
      }
   }

   private JFreeChart buildJFreeChart() {
      XYSeriesCollection xyDataset = new XYSeriesCollection();
      getXYSeries(xyDataset);

      JFreeChart chart = ChartFactory.createXYLineChart("Übersicht", "X-Achse", "Y-Achse", xyDataset,
            PlotOrientation.VERTICAL, true, true, false);

      XYPlot plot = chart.getXYPlot();
      XYItemRenderer xyItemRenderer = new XYDotRenderer();
      ((XYDotRenderer) xyItemRenderer).setDotHeight(5);
      ((XYDotRenderer) xyItemRenderer).setDotWidth(5);
      plot.setRenderer(xyItemRenderer);
      ValueAxis rangeAxis = plot.getRangeAxis();
      rangeAxis.setAutoRange(true);

      XYItemRenderer renderer = chart.getXYPlot().getRenderer();
      renderer.setSeriesPaint(0, Color.BLACK);
      renderer.setSeriesPaint(2, Color.ORANGE);

      return chart;
   }

   private void getXYSeries(XYSeriesCollection xyDataset) {
      xySeriesMoveableHistory = new XYSeries("Moveable-History");
      xySeriesObstacles = new XYSeries("Obstacles");
      xyDataset.addSeries(xySeriesMoveableHistory);
      xyDataset.addSeries(xySeriesObstacles);
   }

   public JComponent getContent() {
      return chartPanel;
   }
}
