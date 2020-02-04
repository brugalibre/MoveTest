/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 *
 */
public class AbstractGridElementPainter<T extends GridElement> extends Drawable<T> implements Renderer {

    private Color color;

    public AbstractGridElementPainter(T gridElement, Color color, int height, int width) {
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

	Position position = getPosition2Paint();
	graphics.drawRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
	graphics.fillRoundRect((int) position.getX(), (int) position.getY(), width, height, 2, 2);
    }

    protected Position getPosition2Paint() {
	return value.getPosition();
    }
}
