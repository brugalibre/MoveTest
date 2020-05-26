package com.myownb3.piranha.core.grid.gridelement.shape.path;

import static com.myownb3.piranha.util.MathUtil.calcAngleBetweenVectors;
import static java.util.Objects.nonNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.vector.VectorUtil;

public class PathSegmentImpl implements PathSegment {

   private Position beginPos;
   private Position endPos;
   private Float64Vector vector;
   private Float64Vector normalBeginPosVector;
   private Float64Vector normalEndPosVector;

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
   public Float64Vector getNormalVectorAtBeginPos() {
      if (nonNull(normalBeginPosVector)) {
         return normalBeginPosVector;
      }
      normalBeginPosVector = getBeginOrEndPosVector(this, beginPos);
      return normalBeginPosVector;
   }

   @Override
   public Float64Vector getNormalVectorAtEndPos() {
      if (nonNull(normalBeginPosVector)) {
         return normalBeginPosVector;
      }
      normalEndPosVector = getBeginOrEndPosVector(this, endPos);
      return normalEndPosVector;
   }

   private static Float64Vector getBeginOrEndPosVector(PathSegment pathSegment, Position pathSegBeginOrEndPos) {
      Direction direction = pathSegBeginOrEndPos.getDirection();
      double angleBetweenVectors = calcAngleBetweenVectors(pathSegment.getVector(), direction.getVector());
      return VectorUtil.rotateVector(direction.getVector(), angleBetweenVectors - 90);
   }

   @Override
   public Float64Vector getVector() {
      return vector;
   }
}
