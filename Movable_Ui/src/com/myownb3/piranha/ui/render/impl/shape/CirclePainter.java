/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;
import java.awt.Graphics;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.shape.Circle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;

/**
 * @author Dominic
 *
 */
public class CirclePainter extends Drawable<Circle> implements Renderer {

    private Color color;

    public CirclePainter(Circle circle, Color color, int height, int width) {
	super(circle);
	this.color = color;
    }

    @Override
    public void render(RenderContext graphicsCtx) {

	GraphicsContext context = (GraphicsContext) graphicsCtx;
	Graphics graphics = context.getGraphics();

	renderPosition(graphics);
    }

    private void renderPosition(Graphics graphics) {
	graphics.setColor(color);
	Position position = value.getCenter();
	int yCoordinate = (int) position.getY() - (value.getRadius() / 2);
	int xCoordinate = (int) position.getX() - (value.getRadius() / 2);
	graphics.fillOval(xCoordinate, yCoordinate, value.getRadius(), value.getRadius());
    }
}
