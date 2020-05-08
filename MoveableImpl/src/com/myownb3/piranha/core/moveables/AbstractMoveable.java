/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

   protected MoveablePostActionHandler handler;
   protected Position posBefore;
   private List<Position> positionHistory;

   public AbstractMoveable(Grid grid, Position position, MoveablePostActionHandler handler, Shape shape) {
      super(grid, position, shape);
      init(grid, handler);
   }

   public AbstractMoveable(Grid grid, Position position, MoveablePostActionHandler handler) {
      super(grid, position);
      init(grid, handler);
   }

   private void init(Grid grid, MoveablePostActionHandler handler) {
      posBefore = Positions.of(position);
      positionHistory = new LinkedList<>();
      this.handler = handler;
      this.handler.handlePostConditions(grid, this);
   }

   public AbstractMoveable(Grid grid, Position position, Shape shape) {
      this(grid, position, (g, m) -> {/* This empty handler does nothing */
      }, shape);
   }

   public AbstractMoveable(Grid grid, Position position) {
      this(grid, position, (g, m) -> {/* This empty handler does nothing */
      });
   }

   @Override
   public void moveForward() {
      moveForwardInternal();
      handler.handlePostConditions(grid, this);
   }

   protected void moveForwardInternal() {
      posBefore = Positions.of(position);
      position = grid.moveForward(this);
      shape.transform(position);
      trackPosition(position);
   }

   @Override
   public void moveForward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> moveForwardInternal());
      handler.handlePostConditions(grid, this);
   }

   @Override
   public void moveBackward() {
      moveBackwardInternal();
      handler.handlePostConditions(grid, this);
   }

   protected void moveBackwardInternal() {
      posBefore = Positions.of(position);
      position = grid.moveBackward(this);
      trackPosition(position);
      shape.transform(position);
   }

   @Override
   public void moveBackward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> moveBackwardInternal());
   }

   private void moveForwardOrBackwardInternal(int amount, Runnable runnable) {
      verifyAmount(amount);
      for (int i = 0; i < amount; i++) {
         runnable.run();
      }
   }

   @Override
   public void turnLeft() {
      makeTurn(90);
   }

   @Override
   public void makeTurnWithoutPostConditions(double degree) {
      makeTurnInternal(degree);
   }

   @Override
   public void turnRight() {
      makeTurn(-90);
   }

   @Override
   public void makeTurn(double degree) {
      makeTurnInternal(degree);
      if (degree != 0) {
         handler.handlePostConditions(grid, this);
      }
   }

   private void makeTurnInternal(double degree) {
      if (degree != 0) {
         position = position.rotate(degree);
         trackPosition(position);
         shape.transform(position);
      }
   }

   @Override
   public Position getPositionBefore() {
      return posBefore;
   }

   @Override
   public List<Position> getPositionHistory() {
      return Collections.unmodifiableList(positionHistory);
   }

   private void verifyAmount(int amount) {
      if (amount <= 0) {
         throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
      }
   }

   private void trackPosition(Position position) {
      synchronized (positionHistory) {
         positionHistory.add(position);
      }
   }
}
