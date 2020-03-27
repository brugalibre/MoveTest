/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.shape.circle.CirclePainter;

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
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }
}
