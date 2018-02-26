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

    public GridPainter(Grid value) {
	super(value);
	Dimension gridDimension = value.getDimension();
	setBounds(new Rectangle(gridDimension.getX(), gridDimension.getY(), gridDimension.getWidth(),
		gridDimension.getHeight()));
    }

    @Override
    public void render(RenderContext graphicsCtx) {

	Graphics2DContext context = (Graphics2DContext) graphicsCtx;

	Graphics2D graphics2d = context.getGraphics2d();

	graphics2d.setColor(Color.BLACK);
	graphics2d.drawOval(x, y, width, height);
    }
}
