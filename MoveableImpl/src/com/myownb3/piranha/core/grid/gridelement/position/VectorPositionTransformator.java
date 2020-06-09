package com.myownb3.piranha.core.grid.gridelement.position;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link VectorPositionTransformator} is used to make simple lateral transformations
 * 
 * @author Dominic
 *
 */
public class VectorPositionTransformator implements PositionTransformator {

   private Position sourcePos;
   private Position dependingPos;

   private VectorPositionTransformator(Position sourcePos, Position dependingPos) {
      this.sourcePos = sourcePos;
      this.dependingPos = dependingPos;
   }

   @Override
   public Position transform(Position movedSourcePosition) {
      Float64Vector vectorFromOldSource2NewSource = movedSourcePosition.getVector().minus(sourcePos.getVector());
      return Positions.of(dependingPos.getX() + vectorFromOldSource2NewSource.get(0).doubleValue(),
            dependingPos.getY() + vectorFromOldSource2NewSource.get(1).doubleValue());
   }

   public static VectorPositionTransformator of(Position sourcePos, Position dependingPos) {
      return new VectorPositionTransformator(sourcePos, dependingPos);
   }
}
