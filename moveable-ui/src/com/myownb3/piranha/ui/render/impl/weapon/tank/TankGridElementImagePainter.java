package com.myownb3.piranha.ui.render.impl.weapon.tank;

import static com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementPaintUtil.getGunCarriageImageRes;
import static com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementPaintUtil.getGunImageLocation;
import static java.awt.Color.BLACK;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.image.constants.ImageConsts;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.image.ImagePainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePaintUtil;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretShapeImagePainter;

public class TankGridElementImagePainter extends GridElementPainter {

   private ImagePainter tankHullImagePainter;
   private List<TurretShapeImagePainter> turretImagePainters;

   public TankGridElementImagePainter(TankGridElement tank) {
      super(tank, BLACK);
      Shape turretShape = tank.getTurret().getShape();
      this.tankHullImagePainter = buildTankHullImagePainter(tank.getShape(), getTankHullImageLocation(tank.getBelligerentParty(), turretShape));
      this.turretImagePainters = new ArrayList<>();
      ImageResource gunCarriageImageRes = getGunCarriageImageRes(tank.getBelligerentParty());
      String gunImageLocation = getGunImageLocation(tank.getBelligerentParty());
      buildTankGridElementInternal(turretShape, gunCarriageImageRes, gunImageLocation);
   }

   private void buildTankGridElementInternal(Shape turretShape, ImageResource gunCarriageImageRes, String gunImageLocation) {
      if (turretShape instanceof TurretShape) {
         addTurretShape(gunCarriageImageRes, gunImageLocation, (TurretShape) turretShape);
      } else if (hasTurretCluster(turretShape)) {
         List<Shape> turretShapes = ((TurretClusterShape) turretShape).getTurretShapes();
         addTurretShapes(gunCarriageImageRes, gunImageLocation, turretShapes);
      } else {
         throw new IllegalStateException("Unsupported TurretShape '" + turretShape + "'");
      }
   }

   private String getTankHullImageLocation(BelligerentParty belligerentParty, Shape turretShape) {
      if (belligerentParty == BelligerentPartyConst.GALACTIC_EMPIRE) {
         return ImageConsts.TANK_HULL_IMAGE_V2;
      }
      return hasTurretCluster(turretShape) ? ImageConsts.TANK_HULL_SYMECTRIC_IMAGE : ImageConsts.TANK_HULL_IMAGE;
   }

   private void addTurretShape(ImageResource gunCarriageImageRes, String gunImageLocation, TurretShape turretShape) {
      TurretShapeImagePainter turretImagePainter = new TurretShapeImagePainter(turretShape, gunCarriageImageRes, gunImageLocation);
      turretImagePainters.add(turretImagePainter);
   }

   private void addTurretShapes(ImageResource gunCarriageImageRes, String gunImageLocation, List<Shape> turretShapes) {
      for (Shape shape : turretShapes) {
         TurretShape turretShape = requireAs(shape, TurretShape.class);
         addTurretShape(gunCarriageImageRes, gunImageLocation, turretShape);
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

   private boolean hasTurretCluster(Shape turretShape) {
      return turretShape instanceof TurretClusterShape;
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
