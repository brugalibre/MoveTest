/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.shape.circle.CirclePainter;
import com.myownb3.piranha.ui.render.impl.shape.position.PositionPainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePainter;

/**
 * @author Dominic
 *
 */
public class ShapePainterFactory {

   public static Drawable<? extends Shape> getShapePainter(Shape shape, Color color, PaintMode paintMode, int height, int width) {
      if (shape instanceof Circle) {
         return new CirclePainter((Circle) shape, paintMode, color, height, width);
      } else if (shape instanceof PositionShape) {
         return new PositionPainter((PositionShape) shape, color, height, width);
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }

   public static Drawable<? extends Shape> getShapePainter(Shape shape, Color color, int height, int width) {
      if (shape instanceof Circle) {
         return new CirclePainter((Circle) shape, PaintMode.SHAPE, color, height, width);
      } else if (shape instanceof PositionShape) {
         return new PositionPainter((PositionShape) shape, color, height, width);
      } else if (shape instanceof Rectangle) {
         return new RectanglePainter((Rectangle) shape, color);
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }
}
