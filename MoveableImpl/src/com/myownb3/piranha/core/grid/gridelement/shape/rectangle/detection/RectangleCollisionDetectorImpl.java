/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.rectangle.detection;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;
import java.util.function.Predicate;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;

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
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition,
         Position newPosition, List<GridElement> gridElements2Check) {
      Rectangle transformedRectangle = getTransformedRectangle(newPosition);
      gridElements2Check.stream()
            .filter(isCollision(transformedRectangle))
            .forEach(handleCollision(collisionDetectionHandler, newPosition, movedGridElement));
   }

   private Predicate<? super GridElement> isCollision(Rectangle transformedRectangle) {
      return gridElement -> gridElement.getShape()
            .getPath()
            .stream()
            .map(PathSegment::getBegin)
            .anyMatch(posOnShapePath -> isPositionInsideRectangle(posOnShapePath, transformedRectangle));
   }

   private boolean isPositionInsideRectangle(Position posOnShapePath, Rectangle transformedRectangle) {
      List<PathSegment> trasformedPath = transformedRectangle.getPath();
      PathSegment firstPathSegment = trasformedPath.get(0);
      PathSegment thirdPathSegment = trasformedPath.get(2);
      Float64Vector rectangleEdge1Vector = firstPathSegment.getVector();
      Float64Vector rectangleEdge2Vector = thirdPathSegment.getVector();

      double distanceFromShapePos2Edge1 = calcDistanceFromPositionToLine(posOnShapePath, firstPathSegment.getBegin(), rectangleEdge1Vector);
      double distanceFromShapePos2Edge2 = calcDistanceFromPositionToLine(posOnShapePath, thirdPathSegment.getBegin(), rectangleEdge2Vector);
      double distanceBetweenFirstAndThirdPathSegment =
            calcDistanceFromPositionToLine(thirdPathSegment.getBegin(), firstPathSegment.getBegin(), rectangleEdge1Vector);
      return distanceBetweenFirstAndThirdPathSegment == round(distanceFromShapePos2Edge1 + distanceFromShapePos2Edge2, ACCURACY);
   }

   private Rectangle getTransformedRectangle(Position newPosition) {
      Rectangle transformedRectangle = (Rectangle) rectangle.clone();
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
