/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.shape.circle.CirclePainter;
import com.myownb3.piranha.ui.render.impl.shape.position.PositionPainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePainter;
import com.myownb3.piranha.ui.render.impl.weapon.tank.TankPainter;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretPainter;

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
      } else if (shape instanceof TurretShape) {
         return new TurretPainter((TurretShape) shape, color);
      } else if (shape instanceof TankShape) {
         return new TankPainter((TankShape) shape, color, color.darker());
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }

   public static Drawable<? extends Shape> getShapePainter(Shape shape, Color color, boolean drawBorder) {
      if (shape instanceof Circle) {
         return new CirclePainter((Circle) shape, PaintMode.SHAPE, color, 1, 1);
      } else if (shape instanceof PositionShape) {
         return new PositionPainter((PositionShape) shape, color, 1, 1);
      } else if (shape instanceof Rectangle) {
         return new RectanglePainter((Rectangle) shape, color, drawBorder);
      } else if (shape instanceof TurretShape) {
         return new TurretPainter((TurretShape) shape, color);
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }
}
