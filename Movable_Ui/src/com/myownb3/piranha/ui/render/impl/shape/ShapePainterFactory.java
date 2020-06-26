/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.shape.circle.CirclePainter;
import com.myownb3.piranha.ui.render.impl.shape.position.PositionPainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePainter;
import com.myownb3.piranha.ui.render.impl.weapon.gun.GunPainter;
import com.myownb3.piranha.ui.render.impl.weapon.tank.TankPainter;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;

/**
 * @author Dominic
 *
 */
public class ShapePainterFactory {

   public static Drawable<? extends Shape> getShapePainter(GridElement gridElement, Color color) {

      Shape shape = gridElement.getShape();
      if (shape instanceof Circle) {
         return new CirclePainter((Circle) shape, PaintMode.SHAPE, color, 0, 0);
      } else if (shape instanceof Rectangle) {
         return new RectanglePainter((Rectangle) shape, color);
      } else if (gridElement instanceof Turret) {
         return new TurretPainter(gridElement.getShape(), color);
      } else if (gridElement instanceof Tank) {
         Color tankTurretColor = GridElementColorUtil.getTurretColor(((Tank) gridElement).getTurret().getBelligerentParty());
         return new TankPainter((Tank) gridElement, color, tankTurretColor);
      } else if (shape instanceof PositionShape) {
         return new PositionPainter((PositionShape) shape, color, 5, 5);
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
      } else if (shape instanceof TurretShape || shape instanceof TurretClusterShape) {
         return new TurretPainter(shape, color);
      } else if (shape instanceof GunShape) {
         return new GunPainter((GunShape) shape, color);
      } else {
         throw new RuntimeException("Unknown Shape '" + shape + "'!");
      }
   }
}
