package com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl;


import static com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl.RectangleSides.FRONT_AND_BACK;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

/**
 * The {@link RectanglePathBuilderImpl} adds more {@link PathSegment}s. The amount of {@link PathSegment} as well as the location of this
 * additionally {@link PathSegment} can be defined by using the {@link RectangleSides}
 * 
 * @author DStalder
 *
 */
public class RectanglePathBuilderImpl extends DefaultRectanglePathBuilderImpl {

   private static final long serialVersionUID = 1L;
   private int amountOfSegments;
   private RectangleSides rectangleSide;

   public RectanglePathBuilderImpl(int amountOfSegments, RectangleSides rectangleSides) {
      this.amountOfSegments = amountOfSegments;
      this.rectangleSide = rectangleSides;
   }

   @Override
   protected List<PathSegment> buildPath(Position center, double width, double height, double angle) {
      if (rectangleSide == FRONT_AND_BACK) {
         return buildPathFroFrontAndBackSide(center, width, height, angle);
      } else {
         throw new IllegalStateException("Unsupported RectangleSide '" + rectangleSide + "'");
      }
   }

   private List<PathSegment> buildPathFroFrontAndBackSide(Position center, double width, double height, double angle) {
      List<PathSegment> path = new LinkedList<>();

      Position upperRightRectanglePos = getNextRectanglePos(center, width, height, -angle);
      Position upperLeftRectanglePos = getNextRectanglePos(center, width, height, angle);
      Position rotatedCenter = center.rotate(180);
      Position lowerLeftRectanglePos = getNextRectanglePos(rotatedCenter, width, height, -angle);
      Position lowerRightRectanglePos = getNextRectanglePos(rotatedCenter, width, height, angle);

      double pathSegLenght = calcPathSegLenght(upperRightRectanglePos, upperLeftRectanglePos);

      List<Position> positionsBetweenUpperRightAndUpperLeft =
            calcPositionsBetweenPositions(upperRightRectanglePos, upperLeftRectanglePos, pathSegLenght);
      List<Position> positionsBetweenLowerLeftAndLowerRight =
            calcPositionsBetweenPositions(lowerLeftRectanglePos, lowerRightRectanglePos, pathSegLenght);

      path.addAll(buildPathSegmentsFromPositions(positionsBetweenUpperRightAndUpperLeft));
      path.add(new PathSegmentImpl(upperLeftRectanglePos, lowerLeftRectanglePos));
      path.addAll(buildPathSegmentsFromPositions(positionsBetweenLowerLeftAndLowerRight));

      path.add(new PathSegmentImpl(lowerRightRectanglePos, upperRightRectanglePos));
      return path;
   }

   private double calcPathSegLenght(Position upperRightRectanglePos, Position upperLeftRectanglePos) {
      double distanceFromUpperRight2UpperLeft = upperRightRectanglePos.calcDistanceTo(upperLeftRectanglePos);
      return distanceFromUpperRight2UpperLeft / amountOfSegments;
   }

   private List<PathSegment> buildPathSegmentsFromPositions(List<Position> positionsBetweenUpperRightAndUpperLeft) {
      List<PathSegment> path = new LinkedList<>();
      Iterator<Position> iterator = positionsBetweenUpperRightAndUpperLeft.iterator();
      Position pathSegBegin = iterator.next();
      while (iterator.hasNext()) {
         Position pathSegEnd = pathSegBegin;
         if (iterator.hasNext()) {
            pathSegEnd = iterator.next();
         }
         path.add(new PathSegmentImpl(pathSegBegin, pathSegEnd));
         pathSegBegin = pathSegEnd;
      }
      return path;
   }

   private static List<Position> calcPositionsBetweenPositions(Position beginPos, Position endPos, double pathSegLenght) {
      return Positions.buildPositionsBetweenTwoPositions(beginPos, endPos, pathSegLenght);
   }

   public enum RectangleSides {

      /** Defines the front and the back side of the rectangle depending on it's {@link Orientation} */
      FRONT_AND_BACK,

      /** Only for testing purpose */
      NONE
   }
}
