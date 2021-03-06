package com.myownb3.piranha.core.battle.weapon.target;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScanner;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link TargetGridElement} describes a {@link GridElement} which was acquired by a {@link TurretScanner}
 * 
 * @author Dominic
 *
 */
public class TargetGridElementImpl implements TargetGridElement {

   private GridElement gridElement;
   private Position prevAcquiredPos;
   private Position currentAcquiredPos;
   private Position targetPosWithLead2Acquire;

   private TargetGridElementImpl(GridElement gridElement) {
      this.gridElement = gridElement;
      this.currentAcquiredPos = gridElement.getPosition();
   }

   @Override
   public boolean isMoving() {
      requireNonNull(prevAcquiredPos, "We need a previous acquired Position! This should actually never be null..");
      return currentAcquiredPos.getX() != prevAcquiredPos.getX()
            || currentAcquiredPos.getY() != prevAcquiredPos.getY();
   }

   @Override
   public boolean isSameGridElementTarget(Optional<TargetGridElement> currentTargetGridElementAvailable) {
      return currentTargetGridElementAvailable
            .map(TargetGridElement::getGridElement)
            .map(isStillSameGridElementDetected())
            .orElse(false);
   }

   private Function<? super GridElement, Boolean> isStillSameGridElementDetected() {
      return currentEvaluatedTargetGridElement -> currentEvaluatedTargetGridElement == gridElement;
   }

   @Override
   public GridElement getGridElement() {
      return gridElement;
   }

   @Override
   public Position getTargetPosWithLead2Acquire() {
      return targetPosWithLead2Acquire;
   }

   @Override
   public Position getCurrentGridElementPosition() {
      return currentAcquiredPos;
   }

   @Override
   public void setTargetPosWithLead2Acquire(Position targetPosWithLead2Acquire) {
      this.targetPosWithLead2Acquire = targetPosWithLead2Acquire;
   }

   @Override
   public void setPrevAcquiredPos(Position prevAcquiredPos) {
      this.prevAcquiredPos = prevAcquiredPos;
   }

   @Override
   public int getTargetVelocity() {
      return gridElement.getVelocity();
   }

   public static TargetGridElement of(GridElement gridElement) {
      return new TargetGridElementImpl(gridElement);
   }
}
