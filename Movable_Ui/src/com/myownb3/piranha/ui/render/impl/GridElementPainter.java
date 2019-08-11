/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 *
 */
public class GridElementPainter extends Drawable<GridElement> implements Renderer {

    private Color color;

    public GridElementPainter(GridElement gridElement, Color color, int height, int width) {
	super(gridElement);

	this.color = color;
	Position position = gridElement.getPosition();
	setBounds(new Rectangle((int) position.getX(), (int) position.getY(), height, width));
    }

    @Override
    public void render(RenderContext graphicsCtx) {

	GraphicsContext context = (GraphicsContext) graphicsCtx;
	Graphics graphics = context.getGraphics();
	graphics.setColor(color);

	Position position = value.getPosition();
	graphics.drawRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
	graphics.fillRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
    }
}
