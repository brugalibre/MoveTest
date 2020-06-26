package com.myownb3.piranha.ui.render.impl.weapon.gun;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Optional;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

public class GunPainter extends Drawable<GunShape> implements Renderer {

   private Drawable<? extends Shape> barrelPainter;
   private Optional<Drawable<? extends Shape>> muzzlePainterOpt;

   public GunPainter(GunShape gunShape, Color color) {
      super(gunShape);
      barrelPainter = ShapePainterFactory.getShapePainter(gunShape.getBarrel(), color, false);
      muzzlePainterOpt = Optional.empty();
      if (gunShape.getMuzzleBreak().isPresent()) {
         muzzlePainterOpt = Optional.of(ShapePainterFactory.getShapePainter(gunShape.getMuzzleBreak().get(), color, false));
      }
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      barrelPainter.render(graphicsCtx);
      muzzlePainterOpt.ifPresent(muzzlePainter -> muzzlePainter.render(graphicsCtx));
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
