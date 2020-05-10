package com.myownb3.piranha.core.grid.gridelement.shape.path;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.vector.VectorUtil;

public class PathSegmentImpl implements PathSegment {

   private Position beginPos;
   private Position endPos;
   private Float64Vector vector;

   public PathSegmentImpl(Position beginPos, Position endPos) {
      this.beginPos = beginPos;
      this.endPos = endPos;
      this.vector = VectorUtil.getVector(endPos).minus(VectorUtil.getVector(beginPos));
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
