package com.myownb3.piranha.core.weapon.turret.turretscanner;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link TargetGridElement} describes a {@link GridElement} which was acquired by a {@link TurretScanner}
 * 
 * @author Dominic
 *
 */
public class TargetGridElement {

   private GridElement gridElement;
   private Position prevAcquiredPos;
   private Position currentAcquiredPos;
   private Position targetPosWithLead2Acquire;

   private TargetGridElement(GridElement gridElement) {
      this.gridElement = gridElement;
      this.currentAcquiredPos = gridElement.getPosition();
   }

   public boolean isMoving() {
      requireNonNull(prevAcquiredPos, "We need a previous acquired Position! This should actually never be null..");
      return currentAcquiredPos.getX() != prevAcquiredPos.getX()
            || currentAcquiredPos.getY() != prevAcquiredPos.getY();
   }

   public static TargetGridElement of(GridElement gridElement) {
      return new TargetGridElement(gridElement);
   }

   public GridElement getGridElement() {
      return gridElement;
   }

   public Position getTargetPosWithLead2Acquire() {
      return targetPosWithLead2Acquire;
   }

   public Position getCurrentGridElementPosition() {
      return currentAcquiredPos;
   }

   public void setTargetPosWithLead2Acquire(Position targetPosWithLead2Acquire) {
      this.targetPosWithLead2Acquire = targetPosWithLead2Acquire;
   }

   public void setPrevAcquiredPos(Position prevAcquiredPos) {
      this.prevAcquiredPos = prevAcquiredPos;
   }
}
