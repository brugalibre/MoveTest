package com.myownb3.piranha.ui.render.impl.missile;

import java.awt.Color;
import java.util.function.Supplier;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePaintUtil;

public class MissileGridElementPainter extends ProjectileGridElementPainter {

   private static final int MISSILE_TAIL_LENGTH = 15;
   private MissileTailPainter missileTailPainter;

   public MissileGridElementPainter(ProjectileGridElement projectileGridElement, Color color) {
      super(projectileGridElement, color);
      this.missileTailPainter = new MissileTailPainter(new MissileTail(MISSILE_TAIL_LENGTH, true), getMissileTailPosSupplier(projectileGridElement),
            () -> getRotationAngleSupplier(value.getShape()));
   }

   private Supplier<Position> getMissileTailPosSupplier(ProjectileGridElement projectileGridElement) {
      return () -> projectileGridElement.getRearmostPosition().movePositionForward4Distance(MISSILE_TAIL_LENGTH);
   }

   private double getRotationAngleSupplier(Shape missileShape) {
      return RectanglePaintUtil.getRectangleRotationAngle(requireAs(missileShape, Rectangle.class));
   }

   @Override
   protected void renderGridElement(RenderContext graphicsCtx) {
      super.renderGridElement(graphicsCtx);
      missileTailPainter.render(graphicsCtx);
   }

   @Override
   protected boolean canShowExplosion(GridElement gridElement) {
      return true;
   }
}
