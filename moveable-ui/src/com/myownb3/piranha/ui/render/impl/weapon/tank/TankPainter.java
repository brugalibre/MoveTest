package com.myownb3.piranha.ui.render.impl.weapon.tank;

import java.awt.Color;
import java.awt.Rectangle;

import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

public class TankPainter extends Drawable<TankShape> {

   private Drawable<? extends Shape> turretPainter;
   private Drawable<? extends Shape> tankPainter;

   public TankPainter(Tank tank, Color tankColor, Color turretColor) {
      super(tank.getShape());
      TankShape tankShape = tank.getShape();
      this.turretPainter = ShapePainterFactory.getShapePainter(tankShape.getTurretShape(), turretColor, false);
      this.tankPainter = ShapePainterFactory.getShapePainter(tankShape.getHull(), tankColor, false);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      tankPainter.render(graphicsCtx);
      turretPainter.render(graphicsCtx);
   }

   @Override
   public void setBounds(Rectangle bounds) {
      throw new IllegalStateException("Not allowed here!");
   }

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      throw new IllegalStateException("Not allowed here!");
   }
}
