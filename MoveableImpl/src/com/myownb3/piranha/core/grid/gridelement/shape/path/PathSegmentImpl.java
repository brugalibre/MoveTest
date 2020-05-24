package com.myownb3.piranha.core.grid.gridelement.shape.path;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.position.Position;

public class PathSegmentImpl implements PathSegment {

   private Position beginPos;
   private Position endPos;
   private Float64Vector vector;

   public PathSegmentImpl(Position beginPos, Position endPos) {
      this.beginPos = beginPos;
      this.endPos = endPos;
      this.vector = endPos.getVector().minus(beginPos.getVector());
   }

   @Override
   public Position getBegin() {
      return beginPos;
   }

   @Override
   public Position getEnd() {
      return endPos;
   }

   @Override
   public Float64Vector getVector() {
      return vector;
   }
}
