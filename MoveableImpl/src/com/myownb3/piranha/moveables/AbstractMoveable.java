/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends SimpleGridElement implements Moveable {

   protected MoveablePostActionHandler handler;
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
      this.handler = handler;
      this.handler.handlePostConditions(grid, this);
      positionHistory = new LinkedList<>();
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
   public boolean hasDetected(Avoidable avoidable) {
      if (handler instanceof DetectableMoveableHelper) {
         return ((DetectableMoveableHelper) handler).hasDetected(grid, avoidable);
      } else {
         return super.hasDetected(avoidable);
      }
   }

   @Override
   public void moveForward() {
      moveForwardInternal();
      handler.handlePostConditions(grid, this);
   }

   private void moveForwardInternal() {
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
      position = grid.moveBackward(this);
      trackPosition(position);
      shape.transform(position);
      handler.handlePostConditions(grid, this);
   }

   @Override
   public void moveBackward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> moveBackward());
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
         position.rotate(degree);
         trackPosition(position);
      }
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
