package com.myownb3.piranha.ui.render.impl.weapon.turret;

import java.awt.Color;

import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.PolygonPainter;

public class TurretGridElementImagePainter extends GridElementPainter {

   private TurretShapeImagePainter turretShapeImagePainter;
   private PolygonPainter gunCarriagePainter;
   private PolygonPainter gunPainter;

   public TurretGridElementImagePainter(TurretGridElement turretGridElement, String gunCarriageImageLocation, String gunImageLocation) {
      super(turretGridElement, Color.BLACK);
      TurretShape turretShape = (TurretShape) turretGridElement.getShape();
      turretShapeImagePainter = new TurretShapeImagePainter(turretShape, gunCarriageImageLocation, gunImageLocation);
      this.gunCarriagePainter = new PolygonPainter(turretShape.getGunCarriageShape(), java.awt.Color.RED);
      this.gunPainter = new PolygonPainter(turretShape.getGunShape().getBarrel(), java.awt.Color.BLUE);
   }

   @Override
   protected void renderGridElement(RenderContext graphicsCtx) {
      //      gunPainter.render(graphicsCtx);
      //      gunCarriagePainter.render(graphicsCtx);
      turretShapeImagePainter.render(graphicsCtx);
   }

   @Override
   public void setBounds(java.awt.Rectangle bounds) {
      throw new IllegalStateException("Not allowed here!");
   }

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      throw new IllegalStateException("Not allowed here!");
   }
}
