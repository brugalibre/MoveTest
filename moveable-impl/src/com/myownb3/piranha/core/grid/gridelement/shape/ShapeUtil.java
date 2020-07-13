package com.myownb3.piranha.core.grid.gridelement.shape;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class ShapeUtil {

   private ShapeUtil() {
      // private
   }

   /**
    * Combines the Path of all given shapes
    * 
    * @param shapes
    *        all given {@link Shape}s
    * @return the combined path
    */
   public static List<PathSegment> combinePath(Shape... shapes) {
      return combinePath(Arrays.asList(shapes));
   }

   /**
    * Combines the Path of all given shapes
    * 
    * @param gunShapes
    *        all given {@link Shape}s
    * @return the combined path
    */
   public static List<PathSegment> combinePath(List<? extends Shape> gunShapes) {
      return gunShapes.stream()
            .map(Shape::getPath)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
   }

   /**
    * Sets the given {@link GridElement} on all given {@link Shape}
    * 
    * @param gridElement
    *        the {@link GridElement}
    * @param shapes
    *        the {@link Shape}s
    */
   public static void setGridElement(GridElement gridElement, Shape... shapes) {
      for (Shape shape : shapes) {
         ((AbstractShape) shape).setGridElement(gridElement);
      }
   }

   /**
    * Checks the given {@link Shape}s for collision, starting with the first in the list. As soon as there is a collision, the result is
    * returned immediately
    * 
    * @param shapes
    *        the {@link Shape}s to check
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler}
    * @param newPosition
    *        the new {@link Position}
    * @param gridElements2Check
    *        the {@link GridElement}s to check
    * @return a {@link CollisionDetectionResult}
    */
   public static CollisionDetectionResult check4Collision(List<Shape> shapes, CollisionDetectionHandler collisionDetectionHandler,
         Position newPosition, List<GridElement> gridElements2Check) {
      for (Shape shape : shapes) {
         CollisionDetectionResult collisionDetectionResult = shape.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
         if (collisionDetectionResult.isCollision()) {
            return collisionDetectionResult;
         }
      }
      return new CollisionDetectionResultImpl(newPosition);
   }
}
