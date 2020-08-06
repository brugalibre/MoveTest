package com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl;

import static com.myownb3.piranha.util.vector.VectorUtil.rotateVector;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.util.LinkedList;
import java.util.List;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.RectanglePathBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

public class DefaultRectanglePathBuilderImpl implements RectanglePathBuilder {

   private static final long serialVersionUID = 3770567256984142021L;

   @Override
   public List<PathSegment> buildRectanglePath(Position center, Orientation orientation, double width, double height) {
      return buildRectangleShape(center, width, height, orientation);
   }

   private List<PathSegment> buildRectangleShape(Position center, double width, double height, Orientation orientation) {
      double angle;
      switch (orientation) {
         case HORIZONTAL:
            angle = toDegrees(Math.atan((width / 2) / (height / 2)));
            return buildPath(center, width, height, angle);
         case VERTICAL:
            angle = toDegrees(Math.atan((height / 2) / (width / 2)));
            return buildPath(center, width, height, angle);
         default:
            throw new IllegalStateException("Unknown Orientation '" + orientation + "'!");
      }
   }

   protected List<PathSegment> buildPath(Position center, double width, double height, double angle) {
      List<PathSegment> path = new LinkedList<>();

      Position nextRectanglePos1 = getNextRectanglePos(center, width, height, -angle);
      Position nextRectanglePos2 = getNextRectanglePos(center, width, height, angle);

      Position rotatedCenter = center.rotate(180);
      Position nextRectanglePos3 = getNextRectanglePos(rotatedCenter, width, height, -angle);
      Position nextRectanglePos4 = getNextRectanglePos(rotatedCenter, width, height, angle);
      path.add(new PathSegmentImpl(nextRectanglePos1, nextRectanglePos2));
      path.add(new PathSegmentImpl(nextRectanglePos2, nextRectanglePos3));
      path.add(new PathSegmentImpl(nextRectanglePos3, nextRectanglePos4));
      path.add(new PathSegmentImpl(nextRectanglePos4, nextRectanglePos1));
      return path;
   }

   protected Position getNextRectanglePos(Position center, double width, double height, double angle) {
      Float64Vector centerVector = center.getVector();
      Float64Vector rectanglePosVector = center.getDirection().getVector();
      rectanglePosVector = rotateVector(rectanglePosVector, angle);
      rectanglePosVector = rectanglePosVector.times(getHypotenuse(width, height) * 10);
      return Positions.of(centerVector.plus(rectanglePosVector), 7);
   }

   protected double getHypotenuse(double width, double height) {
      double halfWidth = width / 2;
      double halfHeight = height / 2;
      return sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight));
   }
}
