package com.myownb3.piranha.ui.render.impl.weapon.tank;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.image.ImagePainter;
import com.myownb3.piranha.ui.render.impl.shape.PolygonPainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePaintUtil;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretShapeImagePainter;

public class TankGridElementImagePainter extends GridElementPainter {

   private ImagePainter tankHullImagePainter;
   private List<TurretShapeImagePainter> turretImagePainters;
   private Drawable<? extends Shape> tankHullPainter;

   public TankGridElementImagePainter(TankGridElement tank, String tankHullImageLocation, String gunCarriageImageLocation,
         String gunImageLocation) {
      super(tank, BLACK, 0, 0);
      this.tankHullImagePainter = buildTankHullImagePainter(tank.getShape(), tankHullImageLocation);
      this.tankHullPainter = new PolygonPainter(tank.getShape().getHull(), Color.BLACK);
      this.turretImagePainters = new ArrayList<>();
      Shape turretShape = tank.getTurret().getShape();
      if (turretShape instanceof TurretShape) {
         addTurretShape(gunCarriageImageLocation, gunImageLocation, (TurretShape) turretShape);
      } else if (turretShape instanceof TurretClusterShape) {
         List<Shape> turretShapes = ((TurretClusterShape) turretShape).getTurretShapes();
         addTurretShapes(gunCarriageImageLocation, gunImageLocation, turretShapes);
      } else {
         throw new IllegalStateException("Unsupported TurretShape '" + turretShape + "'");
      }
   }

   private void addTurretShape(String gunCarriageImageLocation, String gunImageLocation, TurretShape turretShape) {
      TurretShapeImagePainter turretImagePainter = new TurretShapeImagePainter(turretShape, gunCarriageImageLocation, gunImageLocation);
      turretImagePainters.add(turretImagePainter);
   }

   private void addTurretShapes(String gunCarriageImageLocation, String gunImageLocation, List<Shape> turretShapes) {
      for (Shape shape : turretShapes) {
         TurretShape turretShape = requireAs(shape, TurretShape.class);
         addTurretShape(gunCarriageImageLocation, gunImageLocation, turretShape);
      }
   }

   private ImagePainter buildTankHullImagePainter(TankShape tankShape, String tankHullImageLocation) {
      Rectangle hull = requireAs(tankShape.getHull(), Rectangle.class);
      BufferedImage scaledImage = readAndScaleImage4RectangleShape(hull, tankHullImageLocation);
      return new ImagePainter(scaledImage, () -> tankShape.getCenter(), () -> getAngleSupplier(hull));
   }

   private double getAngleSupplier(Rectangle tankHull) {
      return RectanglePaintUtil.getRectangleRotationAngle(tankHull);
   }

   @Override
   protected void renderGridElement(RenderContext graphicsCtx) {
      //      tankHullPainter.render(graphicsCtx);
      tankHullImagePainter.render(graphicsCtx);
      turretImagePainters.forEach(turretImagePainter -> turretImagePainter.render(graphicsCtx));
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
