package com.myownb3.piranha.core.grid.gridelement.shape.rectangle;

import static com.myownb3.piranha.core.grid.gridelement.position.Positions.buildPositionsBetweenTwoPositions;
import static com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleUtil.getNextPosition;
import static com.myownb3.piranha.util.MathUtil.rotateVector;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.collision.CollisionDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.detection.RectangleCollisionDetectorImpl.RectangleCollisionDetectorBuilder;
import com.myownb3.piranha.core.grid.position.Position;

public class RectangleImpl extends AbstractShape implements Rectangle {

   private Position center;
   private double height;
   private double width;
   private Orientation orientation;
   private double distanceBetweenPosOnColDetectionPath;
   private List<Position> path4Detection;

   private RectangleImpl(Position center, double width, double height, double distanceBetweenPosOnColDetectionPath) {
      this(center, width, height, distanceBetweenPosOnColDetectionPath, Orientation.HORIZONTAL);
   }

   private RectangleImpl(Position center, double width, double height, double distanceBetweenPosOnColDetectionPath, Orientation orientation) {
      super(buildRectangleWithCenter(center, width, height, orientation));
      this.height = height;
      this.width = width;
      this.center = center;
      this.orientation = orientation;
      this.distanceBetweenPosOnColDetectionPath = distanceBetweenPosOnColDetectionPath;
      path4Detection = buildPath4DetectionPrivate();
   }

   @Override
   protected CollisionDetector buildCollisionDetector() {
      return RectangleCollisionDetectorBuilder.builder()
            .withRectangle(this)
            .build();
   }

   @Override
   public void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<GridElement> gridElements2Check) {
      collisionDetector.checkCollision(collisionDetectionHandler, gridElement, center, newPosition, gridElements2Check);
   }

   @Override
   public Position getFurthermostFrontPosition() {
      return shiftPositionForInternal(center);
   }

   private Position shiftPositionForInternal(Position furthermostFrontPos) {
      switch (orientation) {
         case HORIZONTAL:
            return Positions.movePositionForward4Distance(furthermostFrontPos, height / 2);
         case VERTICAL:
            return Positions.movePositionForward4Distance(furthermostFrontPos, width / 2);
         default:
            throw new IllegalStateException("Unknown Orientation '" + orientation + "'!");
      }
   }

   @Override
   public Position getFurthermostBackPosition() {
      Position furthermostFrontPos = center.rotate(180);
      return shiftPositionForInternal(furthermostFrontPos);
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(width, height);
   }

   @Override
   public void transform(Position position) {
      this.path = buildRectangleWithCenter(position, width, height, orientation);
      this.path4Detection = buildPath4DetectionPrivate();
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return path4Detection;
   }

   private List<Position> buildPath4DetectionPrivate() {
      Set<Position> path4Detection = new LinkedHashSet<>();
      for (int i = 0; i < path.size(); i++) {
         Position pathPos1 = path.get(i).getBegin();
         Position pathPos2 = getNextPosition(path, i);

         path4Detection.addAll(buildPositionsBetweenTwoPositions(pathPos1, pathPos2, distanceBetweenPosOnColDetectionPath));
      }
      return new ArrayList<Position>(path4Detection);
   }

   @Override
   public double getHeight() {
      return height;
   }

   @Override
   public double getWidth() {
      return width;
   }

   @Override
   public Position getUpperLeftPosition() {
      return path.get(1).getBegin();
   }

   @Override
   public Rectangle clone() {
      try {
         return cloneRectangle();
      } catch (CloneNotSupportedException e) {
         throw new IllegalStateException(e);
      }
   }

   protected Rectangle cloneRectangle() throws CloneNotSupportedException {
      return (Rectangle) super.clone();
   }

   private static List<PathSegment> buildRectangleWithCenter(Position center, double width, double height, Orientation orientation) {
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

   private static List<PathSegment> buildPath(Position center, double width, double height, double angle) {
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

   private static Position getNextRectanglePos(Position center, double width, double height, double angle) {
      Float64Vector centerVector = getVector(center);
      Float64Vector rectanglePosVector = getVector(center.getDirection());
      rectanglePosVector = rotateVector(rectanglePosVector, angle);
      rectanglePosVector = rectanglePosVector.times(getHypotenuse(width, height) * 10);
      return Positions.of(centerVector.plus(rectanglePosVector), 7);
   }

   private static double getHypotenuse(double width, double height) {
      double halfWidth = width / 2;
      double halfHeight = height / 2;
      return sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight));
   }

   public static class RectangleBuilder {
      private Position center;
      private double height;
      private double width;
      private Orientation orientation;
      private double distanceBetweenPosOnColDetectionPath;

      private RectangleBuilder() {
         distanceBetweenPosOnColDetectionPath = 1;
      }

      public RectangleBuilder withCenter(Position center) {
         this.center = center;
         return this;
      }

      public RectangleBuilder withOrientation(Orientation orientation) {
         this.orientation = orientation;
         return this;
      }

      public RectangleBuilder withWidth(double width) {
         this.width = width;
         return this;
      }

      public RectangleBuilder withHeight(double height) {
         this.height = height;
         return this;
      }

      public RectangleBuilder withDistanceBetweenPosOnColDetectionPath(double distanceBetweenPosOnColDetectionPath) {
         this.distanceBetweenPosOnColDetectionPath = distanceBetweenPosOnColDetectionPath;
         return this;
      }

      public Rectangle build() {
         if (nonNull(orientation)) {
            return new RectangleImpl(center, width, height, distanceBetweenPosOnColDetectionPath, orientation);
         }
         return new RectangleImpl(center, width, height, distanceBetweenPosOnColDetectionPath);
      }

      public static RectangleBuilder builder() {
         return new RectangleBuilder();
      }
   }
}