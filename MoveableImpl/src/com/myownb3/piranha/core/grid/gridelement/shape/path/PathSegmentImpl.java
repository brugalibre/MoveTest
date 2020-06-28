package com.myownb3.piranha.core.grid.gridelement.shape.path;

import static com.myownb3.piranha.util.MathUtil.calcAngleBetweenVectors;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.attribute.LazyAttribute;
import com.myownb3.piranha.util.vector.VectorUtil;

public class PathSegmentImpl implements PathSegment {

   private Position beginPos;
   private Position endPos;
   private LazyAttribute<Float64Vector> vector;
   private LazyAttribute<Float64Vector> normalBeginPosVector;
   private LazyAttribute<Float64Vector> normalEndPosVector;
   private LazyAttribute<Double> lenght;

   public PathSegmentImpl(Position beginPos, Position endPos) {
      this.beginPos = beginPos;
      this.endPos = endPos;
      this.vector = new LazyAttribute<>(() -> endPos.getVector().minus(beginPos.getVector()));
      this.lenght = new LazyAttribute<>(() -> beginPos.calcDistanceTo(endPos));
      this.normalBeginPosVector = new LazyAttribute<>(() -> getBeginOrEndPosVector(this, beginPos));
      this.normalEndPosVector = new LazyAttribute<>(() -> getBeginOrEndPosVector(this, endPos));
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
   public double getLenght() {
      return lenght.get();
   }

   @Override
   public Float64Vector getNormalVectorAtBeginPos() {
      return normalBeginPosVector.get();
   }

   @Override
   public Float64Vector getNormalVectorAtEndPos() {
      return normalEndPosVector.get();
   }

   private static Float64Vector getBeginOrEndPosVector(PathSegment pathSegment, Position pathSegBeginOrEndPos) {
      Direction direction = pathSegBeginOrEndPos.getDirection();
      double angleBetweenVectors = calcAngleBetweenVectors(pathSegment.getVector(), direction.getVector());
      return VectorUtil.rotateVector(direction.getVector(), angleBetweenVectors - 90);
   }

   @Override
   public Float64Vector getVector() {
      return vector.get();
   }

   @Override
   public String toString() {
      return "Begin-Position: '" + beginPos + "'; End-Position: '" + endPos + "'; Vector '" + vector;
   }
}
