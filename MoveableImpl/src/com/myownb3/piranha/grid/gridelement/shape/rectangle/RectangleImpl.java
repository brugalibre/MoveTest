package com.myownb3.piranha.grid.gridelement.shape.rectangle;

import static com.myownb3.piranha.grid.gridelement.position.Positions.buildPositionsBetweenTwoPositions;
import static com.myownb3.piranha.grid.gridelement.shape.rectangle.RectangleUtil.getNextPosition;
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

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.detection.RectangleCollisionDetectorImpl.RectangleCollisionDetectorBuilder;

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
   public void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<Avoidable> allAvoidables) {
      collisionDetector.checkCollision(collisionDetectionHandler, gridElement, center, newPosition, allAvoidables);
   }

   @Override
   public Position getFurthermostFrontPosition() {
      Position furthermostFrontPos = Positions.of(center);
      return shiftPositionForInternal(furthermostFrontPos);
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
      Position furthermostFrontPos = Positions.of(center);
      furthermostFrontPos.rotate(180);
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
         Position pathPos1 = Positions.of(path.get(i));
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
      return path.get(1);
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

   private static List<Position> buildRectangleWithCenter(Position center, double width, double height, Orientation orientation) {
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

   private static List<Position> buildPath(Position center, double width, double height, double angle) {
      List<Position> path = new LinkedList<>();

      Position nextRectanglePos1 = getNextRectanglePos(center, width, height, -angle);
      Position nextRectanglePos2 = getNextRectanglePos(center, width, height, angle);

      Position rotatedCenter = Positions.of(center);
      rotatedCenter.rotate(180);
      Position nextRectanglePos3 = getNextRectanglePos(rotatedCenter, width, height, -angle);
      Position nextRectanglePos4 = getNextRectanglePos(rotatedCenter, width, height, angle);
      path.add(nextRectanglePos1);
      path.add(nextRectanglePos2);
      path.add(nextRectanglePos3);
      path.add(nextRectanglePos4);
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
