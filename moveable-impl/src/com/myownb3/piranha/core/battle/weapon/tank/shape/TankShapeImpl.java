package com.myownb3.piranha.core.battle.weapon.tank.shape;

import static com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil.combinePath;

import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class TankShapeImpl extends AbstractShape implements TankShape {

   private Shape turretShape;
   private Shape hull;

   private TankShapeImpl(Shape tankShape, Shape turretShape) {
      super(combinePath(tankShape, turretShape), tankShape.getCenter());
      this.hull = tankShape;
      this.turretShape = turretShape;
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ((AbstractShape) hull).setGridElement(gridElement);
      ((AbstractShape) turretShape).setGridElement(gridElement);
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      boolean isHullDetected = hull.detectObject(detectorPosition, detector);
      if (!isHullDetected) {
         return turretShape.detectObject(detectorPosition, detector);
      }
      return true;
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      CollisionDetectionResult collisionWithDetectionTankResult =
            hull.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
      if (collisionWithDetectionTankResult.isCollision()) {
         return collisionWithDetectionTankResult;
      }
      return turretShape.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public Position getForemostPosition() {
      return hull.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return hull.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(turretShape.getDimensionRadius(), hull.getDimensionRadius());
   }

   @Override
   public void transform(Position newPosition) {
      super.transform(newPosition);
      turretShape.transform(newPosition);
      hull.transform(newPosition);
      path = combinePath(hull, turretShape);
   }

   @Override
   public Shape getHull() {
      return hull;
   }

   @Override
   public Shape getTurretShape() {
      return turretShape;
   }

   public static final class TankShapeBuilder {

      private Shape turretShape;
      private Shape tankShape;

      private TankShapeBuilder() {
         // private
      }

      public TankShapeBuilder withTurretShape(Shape turretShape) {
         this.turretShape = turretShape;
         return this;
      }

      public TankShapeBuilder withHull(Shape tankShape) {
         this.tankShape = tankShape;
         return this;
      }

      public TankShapeImpl build() {
         return new TankShapeImpl(tankShape, turretShape);
      }

      public static TankShapeBuilder builder() {
         return new TankShapeBuilder();
      }
   }
}
