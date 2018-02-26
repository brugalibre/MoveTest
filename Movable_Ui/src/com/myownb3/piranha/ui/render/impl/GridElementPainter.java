/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 *
 */
public class GridElementPainter extends Drawable<GridElement> implements Renderer {

    /**
     * @param value
     */
    public GridElementPainter(GridElement value, Rectangle bounds) {
	super(value);
	setBounds(bounds);
    }

    @Override
    public void render(RenderContext graphicsCtx) {

	Graphics2DContext context = (Graphics2DContext) graphicsCtx;

	Graphics2D graphics2d = context.getGraphics2d();
	graphics2d.drawOval(x, y, width, height);
    }
}
