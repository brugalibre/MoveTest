package com.myownb3.piranha.core.battle.weapon.turret.cluster.shape;

import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class TurretClusterShapeImpl extends AbstractShape implements TurretClusterShape {

   private static final long serialVersionUID = 6921906560487280495L;
   private List<Shape> turretShapes;

   private TurretClusterShapeImpl(List<Shape> turretShapes, List<PathSegment> path, Position tankPosition) {
      super(path, tankPosition);
      this.turretShapes = turretShapes;
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      turretShapes.stream()
            .map(AbstractShape.class::cast)
            .forEach(abstractShape -> abstractShape.setGridElement(gridElement));
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return ShapeUtil.check4Collision(turretShapes, collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      turretShapes.stream()
            .forEach(turretShape -> turretShape.transform(position));
   }

   @Override
   public Position getForemostPosition() {
      throw new IllegalStateException("Don't call this!");
   }

   @Override
   public Position getRearmostPosition() {
      throw new IllegalStateException("Don't call this!");
   }

   @Override
   public double getDimensionRadius() {
      return turretShapes.stream()
            .map(Shape::getDimensionRadius)
            .mapToDouble(dimRadius -> dimRadius)
            .sum();
   }

   @Override
   public List<Shape> getTurretShapes() {
      return turretShapes;
   }

   public static class TurretClusterShapeBuilder {

      private List<Shape> turretShapes;
      private Position position;

      private TurretClusterShapeBuilder() {
         // private
      }

      public TurretClusterShapeBuilder withTurretShapes(List<Shape> turretShapes) {
         this.turretShapes = turretShapes;
         return this;
      }

      public TurretClusterShapeBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      public TurretClusterShapeImpl build() {
         return new TurretClusterShapeImpl(turretShapes, ShapeUtil.combinePath(turretShapes), position);
      }

      public static TurretClusterShapeBuilder builder() {
         return new TurretClusterShapeBuilder();
      }
   }
}
