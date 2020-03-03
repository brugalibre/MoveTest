/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 *
 */
public class GridPainter extends Drawable<Grid> implements Renderer {

    private int padding;
    private int axisIncrement;
    private int pointWidth;
    private int gridHeight;
    private int gridWidth;

    public GridPainter(Grid grid, int padding, int pointWidth, int gridHeight, int gridWidth) {
	super(grid);
	Dimension gridDimension = grid.getDimension();
	setBounds(new Rectangle(gridDimension.getX(), gridDimension.getY(), gridDimension.getWidth(),
		gridDimension.getHeight()));

	this.padding = padding;
	this.pointWidth = pointWidth;
	this.gridWidth = gridWidth;
	this.gridHeight = gridHeight;
	axisIncrement = 50;
    }

    @Override
    public void render(RenderContext graphicsCtx) {
	GraphicsContext context = (GraphicsContext) graphicsCtx;
	Graphics2D g2 = (Graphics2D) context.getGraphics();

	// create hatch marks and grid lines for x & y axis.
	drawXAxis(g2);
	drawYAxis(g2);
	// create x and y axes
	drawMainXAndYAxixLines(g2);
    }

    private void drawMainXAndYAxixLines(Graphics2D g2) {
	g2.setColor(Color.BLACK);
	g2.drawLine(padding, gridHeight - padding, padding, padding);
	g2.drawLine(padding, gridHeight - padding, gridWidth - padding, gridHeight - padding);
    }

    private void drawYAxis(Graphics2D g2) {
	int y0 = gridHeight - padding;
	int y1 = y0 - pointWidth;
	int x0 = -axisIncrement + padding;
	while (x0 + axisIncrement < gridWidth - padding) {
	    x0 += axisIncrement;
	    int x1 = x0;
	    g2.setColor(Color.BLACK);
	    g2.drawLine(x0, y0, x1, y1);
	    g2.drawString(String.valueOf(x1 - padding), x1, padding / 2);

	    g2.setColor(Color.LIGHT_GRAY);
	    g2.drawLine(x0, gridHeight - padding - 1 - pointWidth, x1, padding);
	}
    }

    private void drawXAxis(Graphics2D g2) {
	int x0 = padding;
	int x1 = pointWidth + padding;
	int y0 = -axisIncrement + padding;
	int greyMarkerLength = x1 + gridWidth - (2 * padding + pointWidth);
	while (y0 + axisIncrement < gridHeight - padding) {
	    y0 += axisIncrement;
	    int y1 = y0;

	    g2.setColor(Color.BLACK);
	    g2.drawLine(x0, y0, x1, y1);
	    g2.drawString(String.valueOf(y1 - padding), x0 - (padding - 5), y1);

	    g2.setColor(Color.LIGHT_GRAY);
	    g2.drawLine(x1, y0, greyMarkerLength, y1);
	}
    }
}
