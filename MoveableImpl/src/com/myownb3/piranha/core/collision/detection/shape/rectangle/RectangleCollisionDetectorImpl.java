/**
 * 
 */
package com.myownb3.piranha.core.collision.detection.shape.rectangle;

import static com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance.fillupPathSegment2DistanceMap;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.IntersectionImpl;
import com.myownb3.piranha.core.collision.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSeg2Distance;
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
   public CollisionDetectionResult checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement,
         Position oldPosition, Position newPosition, List<GridElement> gridElements2Check) {
      Rectangle transformedRectangle = getTransformedRectangle(newPosition);
      return gridElements2Check.stream()
            .map(getNearestIntersection(movedGridElement, transformedRectangle))
            .filter(Objects::nonNull)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                  returnCollisionDetectionResult(collisionDetectionHandler, movedGridElement, newPosition)));
   }

   private Function<GridElement, CollisionGridElement> getNearestIntersection(GridElement movedGridElement, Rectangle transformedRectangle) {
      return gridElement -> {
         return gridElement.getPath(movedGridElement)
               .stream()
               .map(PathSegment::getBegin)
               .filter(posOnShapePath -> isPositionInsideRectangle(posOnShapePath, transformedRectangle))
               .findAny()
               .map(createCollisionGridElement(transformedRectangle, gridElement))
               .orElse(null);
      };
   }

   private Function<? super Position, ? extends CollisionGridElement> createCollisionGridElement(Rectangle transformedRectangle,
         GridElement gridElement) {
      return pathSegmentPos -> {
         List<PathSeg2Distance> pathSegments2Distances = fillupPathSegment2DistanceMap(pathSegmentPos, transformedRectangle.getPath());
         return CollisionGridElementImpl.of(pathSegments2Distances.stream()
               .sorted(Comparator.comparing(PathSeg2Distance::getDistance))
               .findFirst()
               .map(PathSeg2Distance::getPathSegment)
               .map(pathSeg -> IntersectionImpl.of(pathSeg, pathSegmentPos))
               .orElse(null), gridElement);
      };
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
      return (Rectangle) getOurShapeAtNewPos(newPosition, rectangle);
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
