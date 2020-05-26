package com.myownb3.piranha.core.grid.gridelement.shape.circle.detection;

import java.util.List;

import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.detection.AbstractCollisionDetector;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link CircleCollisionDetectorImpl} detects collision within a circle
 * 
 * @author Dominic
 *
 */
public class CircleCollisionDetectorImpl extends AbstractCollisionDetector {

   private Circle circle;

   public CircleCollisionDetectorImpl(Circle circle) {
      super();
      this.circle = circle;
   }

   @Override
   public void checkCollision(CollisionDetectionHandler collisionDetectionHandler, GridElement movedGridElement, Position oldPosition,
         Position newPosition, List<GridElement> gridElements2Check) {
      Shape ourCircleAtNewPos = getOurShapeAtNewPos(newPosition, circle);
      gridElements2Check.stream()
            .filter(isGridElementsInsideOrOnShape(newPosition, ourCircleAtNewPos))
            .forEach(handleCollision(collisionDetectionHandler, newPosition, movedGridElement));
   }

   private static Shape getOurShapeAtNewPos(Position newPosition, Shape shape) {
      Shape ourShapeAtNewPos = shape.clone();
      ourShapeAtNewPos.transform(newPosition);
      return ourShapeAtNewPos;
   }
}
