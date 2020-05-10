/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import java.util.List;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link AbstractGridElement} is the base class of all {@link GridElement}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractGridElement implements GridElement {

   protected Position position;
   protected Grid grid;
   protected Shape shape;
   private String name;

   /**
    * Creates a new {@link AbstractGridElement} with the given {@link Grid} and start
    * {@link Position}
    * 
    * @param grid
    *        the Grid on which this {@link AbstractGridElement} is placed
    * @param position
    *        the start {@link Position}
    */
   public AbstractGridElement(Grid grid, Position position) {
      this(grid, position, PositionShapeBuilder.builder()
            .withPosition(position)
            .build());
   }

   /**
    * Creates a new {@link AbstractGridElement} with the given {@link Grid}, start
    * {@link Position} and {@link Shape}
    * 
    * @param grid
    *        the Grid on which this {@link AbstractGridElement} is placed
    * @param position
    *        the start {@link Position}
    * @param shape
    *        the {@link Shape}
    */
   public AbstractGridElement(Grid grid, Position position, Shape shape) {
      super();
      this.position = position;
      this.grid = grid;
      this.shape = shape;
      ((AbstractShape) shape).setGridElement(this);
      shape.transform(position);
      grid.addElement(this);
   }

   @Override
   public void hasGridElementDetected(GridElement gridElement, Detector detector) {
      gridElement.isDetectedBy(getFurthermostFrontPosition(), detector);
   }

   @Override
   public void isDetectedBy(Position detectorPosition, Detector detector) {
      shape.detectObject(detectorPosition, detector);
   }

   @Override
   public void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      shape.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public Position getPosition() {
      return position;
   }

   @Override
   public Position getFurthermostFrontPosition() {
      return shape.getFurthermostFrontPosition();
   }

   @Override
   public Position getFurthermostBackPosition() {
      return shape.getFurthermostBackPosition();
   }

   @Override
   public Grid getGrid() {
      return grid;
   }

   @Override
   public Shape getShape() {
      return shape;
   }

   @Override
   public double getDimensionRadius() {
      return shape.getDimensionRadius();
   }

   @Override
   public String toString() {
      return "Position: " + position + "\n" + grid;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }
}
