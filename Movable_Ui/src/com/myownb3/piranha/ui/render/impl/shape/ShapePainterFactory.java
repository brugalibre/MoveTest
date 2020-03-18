/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.shape.Circle;
import com.myownb3.piranha.grid.gridelement.shape.PointShape;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.impl.Drawable;

/**
 * @author Dominic
 *
 */
public class ShapePainterFactory {

    public static Drawable<? extends Shape> getShapePainter(Shape shape, Color color, int height, int width) {
	if (shape instanceof Circle) {
	    return new CirclePainter((Circle) shape, color, height, width);
	} else if (shape instanceof PointShape) {
	    return new PositionPainter((PointShape) shape, color, height, width);
	} else {
	    throw new RuntimeException("Unknown Shape '" + shape + "'!");
	}
    }
}
