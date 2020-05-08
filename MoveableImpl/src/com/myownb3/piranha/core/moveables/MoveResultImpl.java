package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.grid.gridelement.position.Position;

public class MoveResultImpl implements MoveResult {

   double distance2EndPos;
   double prevDistance2EndPos;
   boolean isDone;
   private Position moveablePos;

   public MoveResultImpl(Position moveablePos) {
      this(-1, -1, false);
      this.moveablePos = moveablePos;
   }

   public MoveResultImpl(double distance2EndPos, double prevDistance2EndPos) {
      this(distance2EndPos, prevDistance2EndPos, false);
   }

   public MoveResultImpl(double distance2EndPos, double prevDistance2EndPos, boolean isDone) {
      this.distance2EndPos = distance2EndPos;
      this.prevDistance2EndPos = prevDistance2EndPos;
      this.isDone = isDone;
   }

   @Override
   public double getEndPosDistance() {
      return distance2EndPos;
   }

   @Override
   public Position getMoveablePosition() {
      return moveablePos;
   }

   @Override
   public boolean isDone() {
      return isDone;
   }
}
