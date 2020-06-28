/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static java.lang.Math.abs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
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
   protected int velocity;
   private List<Position> positionHistory;

   protected AbstractMoveable(Grid grid, MoveablePostActionHandler handler, Shape shape, DimensionInfo dimensionInfo,
         int velocity) {
      super(grid, shape, dimensionInfo);
      init(handler, velocity);
   }

   private void init(MoveablePostActionHandler handler, int velocity) {
      posBefore = Positions.of(position);
      positionHistory = new LinkedList<>();
      this.handler = handler;
      this.velocity = velocity;
   }

   protected AbstractMoveable(Grid grid, Shape shape, DimensionInfo dimensionInfo, int velocity) {
      this(grid, m -> true, shape, dimensionInfo, velocity);
   }

   @Override
   public void moveForward() {
      moveForward(1);
   }

   protected void moveForwardInternal() {
      posBefore = position;
      position = grid.moveForward(this);
      shape.transform(position);
      trackPosition(position);
   }

   @Override
   public void moveForward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> {
         moveForwardInternal();
         return handler.handlePostConditions(this);
      });
   }

   @Override
   public void moveBackward() {
      moveBackward(1);
   }

   protected void moveBackwardInternal() {
      posBefore = position;
      position = grid.moveBackward(this);
      trackPosition(position);
      shape.transform(position);
   }

   @Override
   public void moveBackward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> {
         moveBackwardInternal();
         return handler.handlePostConditions(this);
      });
   }

   private void moveForwardOrBackwardInternal(int amount, Supplier<Boolean> moveForwardAndGetResult) {
      verifyAmount(amount);
      for (int i = 0; i < amount; i++) {
         if (has2Abort(!moveForwardAndGetResult.get())) {
            break;
         }
      }
   }

   private boolean has2Abort(boolean has2Abort) {
      return DestructionHelper.isDestroyed(this)
            || has2Abort;
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
      if (abs(degree) > 0) {
         handler.handlePostConditions(this);
      }
   }

   private void makeTurnInternal(double degree) {
      if (abs(degree) > 0) {
         position = position.rotate(degree);
         trackPosition(position);
         shape.transform(position);
      }
   }

   @Override
   public int getVelocity() {
      return velocity;
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
