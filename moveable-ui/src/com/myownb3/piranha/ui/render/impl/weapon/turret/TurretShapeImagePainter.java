package com.myownb3.piranha.ui.render.impl.weapon.turret;

import java.awt.image.BufferedImage;

import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.image.ImagePainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePaintUtil;

public class TurretShapeImagePainter extends Drawable<TurretShape> {

   private ImagePainter gunCarriageImagePainter;
   private ImagePainter gunPainter;

   public TurretShapeImagePainter(TurretShape turretShape, String gunCarriageImageLocation, String gunImageLocation) {
      super(turretShape);
      gunCarriageImagePainter = buildGunCarriageImagePainter(turretShape, gunCarriageImageLocation);
      gunPainter = buildGunPainter(turretShape, gunImageLocation);
   }

   private ImagePainter buildGunCarriageImagePainter(TurretShape turretShape, String gunCarriageImageLocation) {
      Rectangle gunCarriageShape = requireAs(turretShape.getGunCarriageShape(), Rectangle.class);
      BufferedImage gunCarriageImage = readAndScaleImage4RectangleShape(gunCarriageShape, gunCarriageImageLocation);
      return new ImagePainter(gunCarriageImage, () -> gunCarriageShape.getCenter(), () -> getAngleSupplier(gunCarriageShape));
   }

   private ImagePainter buildGunPainter(TurretShape turretShape, String gunImageLocation) {
      GunShape gunShape = turretShape.getGunShape();
      Rectangle barrel = gunShape.getBarrel();
      BufferedImage gunImage = readAndScaleImage4RectangleShape(barrel, gunImageLocation);
      return new ImagePainter(gunImage, () -> barrel.getCenter(), () -> getAngleSupplier(barrel));
   }

   private static double getAngleSupplier(Rectangle shape) {
      return RectanglePaintUtil.getRectangleRotationAngle(shape);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      gunCarriageImagePainter.render(graphicsCtx);
      gunPainter.render(graphicsCtx);
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
