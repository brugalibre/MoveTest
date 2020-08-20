package com.myownb3.piranha.ui.render.impl.weapon.turret;

import static com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementPaintUtil.getGunCarriageImageRes;
import static com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementPaintUtil.getGunImageLocation;

import java.awt.Color;

import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;

public class TurretGridElementImagePainter extends GridElementPainter {

   private TurretShapeImagePainter turretShapeImagePainter;

   public TurretGridElementImagePainter(TurretGridElement turretGridElement) {
      this(turretGridElement, getGunCarriageImageRes(turretGridElement.getBelligerentParty()),
            getGunImageLocation(turretGridElement.getBelligerentParty()));
   }

   private TurretGridElementImagePainter(TurretGridElement turretGridElement, ImageResource gunCarriageImage, String gunImageLocation) {
      super(turretGridElement, Color.BLACK);
      TurretShape turretShape = (TurretShape) turretGridElement.getShape();
      this.turretShapeImagePainter = new TurretShapeImagePainter(turretShape, gunCarriageImage, gunImageLocation);
   }

   @Override
   protected void renderGridElement(RenderContext graphicsCtx) {
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
