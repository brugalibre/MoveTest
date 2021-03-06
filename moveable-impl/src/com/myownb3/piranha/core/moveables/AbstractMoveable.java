/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static java.lang.Math.abs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

   protected Grid grid;
   protected MoveablePostActionHandler handler;
   protected Position posBefore;
   protected int velocity;
   private List<Position> positionHistory;

   protected AbstractMoveable(Grid grid, MoveablePostActionHandler handler, Shape shape, DimensionInfo dimensionInfo,
         int velocity) {
      super(shape, dimensionInfo);
      init(handler, velocity, grid);
   }

   private void init(MoveablePostActionHandler handler, int velocity, Grid grid) {
      posBefore = Positions.of(position);
      positionHistory = new LinkedList<>();
      this.handler = handler;
      this.grid = grid;
      this.velocity = verifyAmount(velocity);
   }

   protected AbstractMoveable(Grid grid, Shape shape, DimensionInfo dimensionInfo, int velocity) {
      this(grid, m -> true, shape, dimensionInfo, velocity);
   }

   @Override
   public void moveForward() {
      moveForward(velocity);
   }

   private void moveForward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> {
         moveForwardInternal();
         return handler.handlePostConditions(this);
      });
   }

   private void moveForwardInternal() {
      posBefore = position;
      position = grid.moveForward(this);
      shape.transform(position);
      trackPosition(position);
   }

   @Override
   public void moveBackward() {
      moveBackward(velocity);
   }

   private void moveBackward(int amount) {
      moveForwardOrBackwardInternal(amount, () -> {
         moveBackwardInternal();
         return handler.handlePostConditions(this);
      });
   }

   private void moveBackwardInternal() {
      posBefore = position;
      position = grid.moveBackward(this);
      trackPosition(position);
      shape.transform(position);
   }

   private void moveForwardOrBackwardInternal(int amount, BooleanSupplier moveForwardAndGetResult) {
      for (int i = 0; i < amount; i++) {
         if (has2Abort(!moveForwardAndGetResult.getAsBoolean())) {
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
         position = grid.rotate(this, degree);
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

   private void trackPosition(Position position) {
      synchronized (positionHistory) {
         positionHistory.add(position);
      }
   }

   private int verifyAmount(int amount) {
      if (amount <= 0) {
         throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
      }
      return amount;
   }

}
