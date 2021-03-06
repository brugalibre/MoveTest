/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link AbstractGridElement} is the base class of all {@link GridElement}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractGridElement implements GridElement {

   protected Position position;
   protected Shape shape;
   protected DimensionInfo dimensionInfo;

   /**
    * Creates a new {@link AbstractGridElement} with the given {@link Grid}, start
    * {@link Position} and {@link Shape}
    * 
    * @param shape
    *        the {@link Shape}
    * @param position
    *        the start {@link Position}
    */
   protected AbstractGridElement(Shape shape, DimensionInfo dimensionInfo) {
      super();
      this.position = shape.getCenter();
      this.shape = shape;
      this.dimensionInfo = DimensionInfoBuilder.buildFromExisting(dimensionInfo, shape.getDimensionRadius());
      ((AbstractShape) shape).setGridElement(this);
   }

   @Override
   public boolean isDetectedBy(Position detectorPosition, Detector detector) {
      return shape.detectObject(detectorPosition, detector);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return shape.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public int getVelocity() {
      return 0;
   }

   @Override
   public void onCollision(List<GridElement> destructives) {
      // we don't do that here, may be in a subclass?
   }

   @Override
   public List<PathSegment> getPath(GridElement gridElement) {
      if (this.dimensionInfo.isWithinHeight(position.getZ(), gridElement.getPosition().getZ())) {
         return shape.getPath();
      }
      return Collections.emptyList();
   }

   @Override
   public Position getPosition() {
      return position;
   }

   @Override
   public Position getForemostPosition() {
      return shape.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return shape.getRearmostPosition();
   }

   @Override
   public Shape getShape() {
      return shape;
   }

   @Override
   public DimensionInfo getDimensionInfo() {
      return dimensionInfo;
   }

   @Override
   public String toString() {
      return "Position: " + position;
   }

   public abstract static class AbstractGridElementBuilder<V extends AbstractGridElement, T extends AbstractGridElementBuilder<V, T>> {

      protected Grid grid;
      protected Shape shape;
      protected DimensionInfo dimensionInfo;

      protected AbstractGridElementBuilder() {
         // private
      }

      public T withShape(Shape shape) {
         this.shape = shape;
         return getThis();
      }

      public T withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return getThis();
      }

      public T withGrid(Grid grid) {
         this.grid = grid;
         return getThis();
      }

      protected abstract T getThis();

      public abstract V build();
   }
}
