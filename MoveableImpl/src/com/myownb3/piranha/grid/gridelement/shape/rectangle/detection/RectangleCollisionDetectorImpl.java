/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape.rectangle.detection;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.util.List;
import java.util.function.Predicate;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Rectangle;

/**
 * The {@link RectangleCollisionDetectorImpl} implements the {@link CollisionDetector}. It is able to check if a {@link Position} has
 * collided with another {@link Position} on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public class RectangleCollisionDetectorImpl extends AbstractCollisionDetector {

   private static final int ACCURACY = 10;// The amount of decimal places we care about
   private Rectangle rectangle;

   private RectangleCollisionDetectorImpl(Rectangle rectangle) {
      super();
      this.rectangle = rectangle;
   }

   @Override
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement gridElement, Position oldPosition,
         Position newPosition, List<Avoidable> allAvoidables) {
      Rectangle transformedRectangle = getTransformedRectangle(newPosition);
      allAvoidables.stream()
            .filter(isCollision(transformedRectangle))
            .forEach(handleCollisionWithAvoidable(collisionDetectionHandler, newPosition, gridElement));
   }

   private Predicate<? super Avoidable> isCollision(Rectangle transformedRectangle) {
      return avoidable -> avoidable.getShape()
            .getPath()
            .stream()
            .anyMatch(posOnShapePath -> isPositionInsideRectangle(posOnShapePath, transformedRectangle));
   }

   private boolean isPositionInsideRectangle(Position posOnShapePath, Rectangle transformedRectangle) {
      List<Position> path = transformedRectangle.getPath();
      Float64Vector rectangleEdge1Vector = getVector(path.get(1)).minus(getVector(path.get(0)));
      Float64Vector rectangleEdge2Vector = getVector(path.get(3)).minus(getVector(path.get(2)));

      double distanceFromShapePos2Edge1 = calcDistanceFromPositionToLine(posOnShapePath, path.get(0), rectangleEdge1Vector);
      double distanceFromShapePos2Edge2 = calcDistanceFromPositionToLine(posOnShapePath, path.get(2), rectangleEdge2Vector);
      double distanceRectanglePos1ToPos2 = calcDistanceFromPositionToLine(path.get(2), path.get(0), rectangleEdge1Vector);

      return distanceRectanglePos1ToPos2 == round(distanceFromShapePos2Edge1 + distanceFromShapePos2Edge2, ACCURACY);
   }

   private Rectangle getTransformedRectangle(Position newPosition) {
      Rectangle transformedRectangle = rectangle.clone();
      transformedRectangle.transform(newPosition);
      return transformedRectangle;
   }

   public static class RectangleCollisionDetectorBuilder {

      private Rectangle rectangle;

      private RectangleCollisionDetectorBuilder() {
         super();
      }

      public static RectangleCollisionDetectorBuilder builder() {
         return new RectangleCollisionDetectorBuilder();
      }

      public RectangleCollisionDetectorBuilder withRectangle(Rectangle rectangle) {
         this.rectangle = rectangle;
         return this;
      }

      public CollisionDetector build() {
         return new RectangleCollisionDetectorImpl(rectangle);
      }
   }
}
