package com.myownb3.piranha.ui.render.impl.weapon.turret;

import java.awt.Color;
import java.awt.Rectangle;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

public class TurretPainter extends Drawable<TurretShape> implements Renderer {

   private Drawable<? extends Shape> shapeGunPainter;
   private Drawable<? extends Shape> shapeGunCarriagePainter;

   public TurretPainter(TurretShape turretShape, Color color) {
      super(turretShape);
      Gun gun = turretShape.getGunCarriage().getGun();
      this.shapeGunPainter = ShapePainterFactory.getShapePainter(gun.getShape(), color, false);
      this.shapeGunCarriagePainter = ShapePainterFactory.getShapePainter(turretShape.getGunCarriage().getShape(), color, false);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      shapeGunPainter.render(graphicsCtx);
      shapeGunCarriagePainter.render(graphicsCtx);
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
