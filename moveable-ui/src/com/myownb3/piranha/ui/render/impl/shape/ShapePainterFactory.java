/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.TIEFighterShape;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement;
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

   private ShapePainterFactory() {
      // private
   }

   public static Drawable<? extends Shape> getShapePainter(GridElement gridElement, Color color) {
      Shape shape = gridElement.getShape();
      if (gridElement instanceof WallGridElement) {
         return new PolygonPainter(shape, color, true);
      } else if (gridElement instanceof Turret) {
         return new TurretPainter(gridElement.getShape(), color);
      } else if (gridElement instanceof Tank) {
         Color tankTurretColor = GridElementColorUtil.getTurretColor(((Tank) gridElement).getTurret().getBelligerentParty());
         return new TankPainter((Tank) gridElement, color, tankTurretColor);
      } else if (gridElement instanceof ProjectileGridElement) {
         if (((ProjectileGridElement) gridElement).getProjectileType() != ProjectileTypes.BULLET) {
            return new PolygonPainter(shape, color);
         }
      }
      return getShapePainter(shape, color, false);
   }

   public static Drawable<? extends Shape> getShapePainter(Shape shape, Color color, boolean drawBorder) {
      if (shape instanceof Circle) {
         return new CirclePainter((Circle) shape, PaintMode.SHAPE, color, 1, 1);
      } else if (shape instanceof PositionShape) {
         return new PositionPainter((PositionShape) shape, color, 1, 1);
      } else if (shape instanceof Rectangle) {
         return new RectanglePainter((Rectangle) shape, color);
      } else if (shape instanceof TurretShape || shape instanceof TurretClusterShape) {
         return new TurretPainter(shape, color);
      } else if (shape instanceof GunShape) {
         return new GunPainter((GunShape) shape, color);
      } else if (shape instanceof TIEFighterShape) {
         return new TIEFighterShapePainter((TIEFighterShape) shape, color.darker());
      } else {
         throw new IllegalStateException("Unknown Shape '" + shape + "'!");
      }
   }
}
