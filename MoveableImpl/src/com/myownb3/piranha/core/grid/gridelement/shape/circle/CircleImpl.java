/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.circle;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.detection.shape.circle.CircleCollisionDetectorImpl.CircleCollisionDetectorBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
public class CircleImpl extends AbstractShape implements Circle {

   private static final int AMOUNT_OF_PATH_POINTS_4_DETECTION = 150;
   private int amountOfPoints;
   private int radius;
   private Position center;

   private CircleImpl(List<PathSegment> path, Position center, int amountOfPoints, int radius) {
      super(path);
      this.radius = Math.abs(radius);
      this.center = center;
      this.amountOfPoints = verifyAmountOfPoints(amountOfPoints);
      this.collisionDetector = buildCollisionDetector();
   }

   @Override
   protected CollisionDetector buildCollisionDetector() {
      return CircleCollisionDetectorBuilder.builder()
            .withCircle(this)
            .build();
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return collisionDetector.checkCollision(collisionDetectionHandler, gridElement, null/*old value not necessary*/, newPosition,
            gridElements2Check);
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return buildCircleWithCenter(center, AMOUNT_OF_PATH_POINTS_4_DETECTION, radius)
            .stream()
            .map(PathSegment::getBegin)
            .collect(Collectors.toList());
   }

   private int verifyAmountOfPoints(int amountOfPoints) {
      if (amountOfPoints < 4) {
         throw new IllegalArgumentException("We need at least 4 points for a circle!");
      }
      return amountOfPoints;
   }

   @Override
   public void transform(Position position) {
      this.center = position;
      this.path = buildCircleWithCenter(center, amountOfPoints, radius);
   }

   @Override
   public Position getForemostPosition() {
      return getNextCirclePos(center, radius, 0);
   }

   @Override
   public Position getRearmostPosition() {
      Position posInverted = center.rotate(180);
      return getNextCirclePos(posInverted, radius, 0);
   }

   @Override
   public int getRadius() {
      return radius;
   }

   @Override
   public double getDimensionRadius() {
      return radius;
   }

   @Override
   public Position getCenter() {
      return center;
   }

   private static List<PathSegment> buildCircleWithCenter(Position center, int amountOfPoints, int radius) {
      List<PathSegment> path = new LinkedList<>();
      double degInc = 360 / amountOfPoints;
      double deg = 0;
      Position pos = getNextCirclePos(center, radius, deg);
      for (int i = 0; i < amountOfPoints; i++) {
         deg = deg + degInc;
         Position nextPos = getNextCirclePos(center, radius, deg);
         path.add(new PathSegmentImpl(pos, nextPos));
         pos = nextPos;
      }

      return path;
   }

   private static Position getNextCirclePos(Position center, int radius, double deg) {
      Position pos = center.rotate(deg);
      return Positions.movePositionForward4Distance(pos, radius);
   }

   public static class CircleBuilder {

      private int radius;
      private int amountOfPoints;
      private Position center;
      private GridElement gridElement;

      private CircleBuilder() {
         // private
      }

      public CircleBuilder withRadius(int radius) {
         this.radius = radius;
         return this;
      }

      public CircleBuilder withCenter(Position center) {
         this.center = center;
         return this;
      }

      public CircleBuilder withAmountOfPoints(int amountOfPoints) {
         this.amountOfPoints = amountOfPoints;
         return this;
      }

      public CircleBuilder withGridElement(GridElement gridElement) {
         this.gridElement = gridElement;
         return this;
      }

      public CircleImpl build() {
         requireNonNull(center);
         List<PathSegment> path = buildCircleWithCenter(center, amountOfPoints, radius);
         CircleImpl circle = new CircleImpl(path, center, amountOfPoints, radius);
         if (nonNull(gridElement)) {
            ((AbstractShape) circle).setGridElement(gridElement);
         }
         return circle;
      }

      public static CircleBuilder builder() {
         return new CircleBuilder();
      }
   }

}
