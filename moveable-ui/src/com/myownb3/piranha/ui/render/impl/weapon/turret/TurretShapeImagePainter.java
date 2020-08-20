package com.myownb3.piranha.ui.render.impl.weapon.turret;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.image.ImagePainter;
import com.myownb3.piranha.ui.render.impl.shape.AbstractShapePainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePaintUtil;

public class TurretShapeImagePainter extends AbstractShapePainter<TurretShape> {

   private ImagePainter gunCarriageImagePainter;
   private ImagePainter gunPainter;

   public TurretShapeImagePainter(TurretShape turretShape, ImageResource gunCarriageImageRes, String gunImageLocation) {
      super(turretShape);
      this.gunCarriageImagePainter = buildGunCarriageImagePainter(turretShape, gunCarriageImageRes);
      this.gunPainter = buildGunPainter(turretShape, gunImageLocation);
   }

   private ImagePainter buildGunCarriageImagePainter(TurretShape turretShape, ImageResource gunCarriageImageRes) {
      Shape gunCarriageShape = turretShape.getGunCarriageShape();
      BufferedImage gunCarriageImage = null;
      Supplier<Double> imageRotateDegreeSupplier = () -> 0.0;
      switch (gunCarriageImageRes.getImageShape()) {
         case CIRCLE:
            Circle gunCarriageCircle = requireAs(gunCarriageShape, Circle.class);
            imageRotateDegreeSupplier = () -> Math.toRadians(gunCarriageCircle.getCenter().getDirection().getAngle());
            gunCarriageImage = readAndScaleImage4CircleShape(gunCarriageCircle.getDimensionRadius(), gunCarriageImageRes.getResource());
            break;
         case RECTANGLE:
            Rectangle gunCarriageRectangle = requireAs(gunCarriageShape, Rectangle.class);
            imageRotateDegreeSupplier = () -> getAngleSupplier(gunCarriageRectangle);
            gunCarriageImage = readAndScaleImage4RectangleShape(gunCarriageRectangle, gunCarriageImageRes.getResource());
         default:
            break;
      }
      return new ImagePainter(gunCarriageImage, () -> gunCarriageShape.getCenter(), imageRotateDegreeSupplier);
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
