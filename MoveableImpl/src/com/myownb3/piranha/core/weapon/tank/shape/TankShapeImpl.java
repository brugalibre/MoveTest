package com.myownb3.piranha.core.weapon.tank.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
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

   public TankShapeImpl(Shape tankShape, TurretShape turretShape, Position position) {
      super(combinePath(tankShape, turretShape), position);
      this.hull = tankShape;
      this.turretShape = turretShape;
      transform(position);
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return Collections.emptyList();
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
      this.center = newPposition;
      Position newTurretPosition = getNewTurretPosition(center);
      turretShape.transform(newTurretPosition);
      hull.transform(center);
      path = combinePath(hull, turretShape);
   }

   private Position getNewTurretPosition(Position newPposition) {
      Position currentTurretPosition = turretShape.getPosition();
      return Positions.of(currentTurretPosition.getDirection(), newPposition.getX(), newPposition.getY());
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
      private Position tankPosition;

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

      public TankShapeBuilder withPosition(Position tankPosition) {
         this.tankPosition = tankPosition;
         return this;
      }

      public TankShapeImpl build() {
         return new TankShapeImpl(tankShape, turretShape, tankPosition);
      }

      public static TankShapeBuilder builder() {
         return new TankShapeBuilder();
      }
   }
}
