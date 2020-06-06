package com.myownb3.piranha.core.weapon.turret.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;

public class TurretShapeImpl extends AbstractShape implements TurretShape {

   private GunCarriage gunCarriage;

   private TurretShapeImpl(List<PathSegment> path, GunCarriage gunCarriage) {
      super(path, gunCarriage.getShape().getCenter());
      this.gunCarriage = gunCarriage;
      this.collisionDetector = buildCollisionDetector();
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      Shape gunCarriageShape = gunCarriage.getShape();
      CollisionDetectionResult collisionWithGunCarriageDetectionResult =
            gunCarriageShape.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
      if (collisionWithGunCarriageDetectionResult.isCollision()) {
         return collisionWithGunCarriageDetectionResult;
      }
      return getGunShape().check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public Position getForemostPosition() {
      Rectangle gunShape = getGunShape();
      return gunShape.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return gunCarriage.getShape().getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(gunCarriage.getShape().getDimensionRadius(), getGunShape().getDimensionRadius());
   }

   public GunCarriage getGunCarriage() {
      return gunCarriage;
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      Rectangle gunShape = getGunShape();
      gunCarriage.evalAndSetPosition(position);
      this.path = combinePath(gunShape, gunCarriage.getShape());
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return Collections.emptyList();// not needed since we are not moving yet!
   }

   @Override
   public List<Position> getPath4CollisionDetection() {
      return buildPath4Detection();
   }

   private static List<PathSegment> combinePath(Shape gunShape, Shape gunCarriageShape) {
      List<PathSegment> combinedPath = new ArrayList<PathSegment>(gunShape.getPath());
      combinedPath.addAll(gunCarriageShape.getPath());
      return combinedPath;
   }

   @Override
   public Rectangle getGunShape() {
      return gunCarriage.getGun().getShape();
   }

   @Override
   public Shape getGunCarriageShape() {
      return gunCarriage.getShape();
   }

   public static final class TurretShapeBuilder {
      private GunCarriage gunCarriage;

      private TurretShapeBuilder() {
         // private
      }

      public TurretShapeBuilder wighGunCarriage(GunCarriage gunCarriage) {
         this.gunCarriage = gunCarriage;
         return this;
      }

      public TurretShapeImpl build() {
         Rectangle gunShape = gunCarriage.getGun().getShape();
         List<PathSegment> combinedPath = combinePath(gunShape, gunCarriage.getShape());
         return new TurretShapeImpl(combinedPath, gunCarriage);
      }

      public static TurretShapeBuilder builder() {
         return new TurretShapeBuilder();
      }
   }
}
