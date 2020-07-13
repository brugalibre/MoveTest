/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.lineshape;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link ImmutableLineShape} is a {@link Shape} defined by a begin and end {@link Position}.
 * As the name says it cannot be mutated after it's creation
 * 
 * @author Dominic
 *
 */
public class ImmutableLineShape extends AbstractShape {

   private PathSegment pathSegment;

   /**
    * Creates a new {@link ImmutableLineShape}
    * 
    * @param gridElemPos
    */
   private ImmutableLineShape(Position beginPos, Position endPos) {
      super(Collections.singletonList(new PathSegmentImpl(beginPos, endPos)), endPos);
      this.collisionDetector = buildCollisionDetector();
      this.pathSegment = new PathSegmentImpl(beginPos, endPos);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return collisionDetector.checkCollision(collisionDetectionHandler, gridElement, center, newPosition, gridElements2Check);
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return Arrays.asList(pathSegment.getBegin(), pathSegment.getEnd());
   }

   @Override
   public List<PathSegment> getPath() {
      return Collections.singletonList(pathSegment);
   }

   @Override
   public void transform(Position position) {
      // We don't do that here
   }

   @Override
   public Position getRearmostPosition() {
      return pathSegment.getBegin();
   }

   @Override
   public double getDimensionRadius() {
      return pathSegment.getLenght();
   }

   @Override
   public Position getForemostPosition() {
      return pathSegment.getEnd();
   }

   public static class ImmutableLineShapeBuilder {
      private Position beginPos;
      private Position endPos;

      private ImmutableLineShapeBuilder() {
         // private
      }

      public ImmutableLineShapeBuilder withBeginPosition(Position beginPos) {
         this.beginPos = beginPos;
         return this;
      }

      public ImmutableLineShapeBuilder withEndPosition(Position endPos) {
         this.endPos = endPos;
         return this;
      }

      public ImmutableLineShape build() {
         return new ImmutableLineShape(beginPos, endPos);
      }

      public static ImmutableLineShapeBuilder builder() {
         return new ImmutableLineShapeBuilder();
      }
   }
}
