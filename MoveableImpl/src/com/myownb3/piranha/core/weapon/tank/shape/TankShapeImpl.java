package com.myownb3.piranha.core.weapon.tank.shape;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;

public class TankShapeImpl extends AbstractShape implements TankShape {

   private TurretShape turretShape;
   private Shape hull;

   private TankShapeImpl(Shape tankShape, TurretShape turretShape) {
      super(combinePath(tankShape, turretShape), tankShape.getCenter());
      this.hull = tankShape;
      this.turretShape = turretShape;
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
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ((AbstractShape) hull).setGridElement(gridElement);
      ((AbstractShape) turretShape).setGridElement(gridElement);
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
      // TODO this depends on the current position of the turret of this tank
      return hull.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {

      // TODO this depends on the current position of the turret of this tank
      return hull.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(turretShape.getDimensionRadius(), hull.getDimensionRadius());
   }

   @Override
   public void transform(Position newPposition) {
      super.transform(newPposition);
      Position newTurretPosition = getNewTurretPosition();
      turretShape.transform(newTurretPosition);
      hull.transform(center);
      path = combinePath(hull, turretShape);
   }

   private Position getNewTurretPosition() {
      Position currentTurretPosition = turretShape.getCenter();
      return Positions.of(currentTurretPosition.getDirection(), center.getX(), center.getY());
   }

   @Override
   public Shape getHull() {
      return hull;
   }

   @Override
   public TurretShape getTurretShape() {
      return turretShape;
   }

   private static List<PathSegment> combinePath(Shape tankShape, Shape turretShape) {
      List<PathSegment> combinedPath = new ArrayList<PathSegment>(tankShape.getPath());
      combinedPath.addAll(turretShape.getPath());
      return combinedPath;
   }

   public static final class TankShapeBuilder {

      private TurretShape turretShape;
      private Shape tankShape;

      private TankShapeBuilder() {
         // private
      }

      public TankShapeBuilder withTurretShape(TurretShape turretShape) {
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
