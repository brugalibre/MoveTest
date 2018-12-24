/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.awt.Graphics;
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

	int factor = 15;
	Graphicsontext context = (Graphicsontext) graphicsCtx;

	Graphics graphics = context.getGraphics();

	graphics.setColor(Color.BLACK);
	graphics.drawRect(x * factor, y * factor, width * factor, height * factor);
    }
}
