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
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * @author Dominic
 *
 */
public class CircleImpl extends AbstractShape implements Circle {

   private static final long serialVersionUID = -2559315841397177005L;
   private static final int AMOUNT_OF_PATH_POINTS_4_DETECTION = 150;
   private int amountOfPoints;
   private double radius;

   private CircleImpl(List<PathSegment> path, Position center, int amountOfPoints, double radius) {
      super(path, center);
      this.radius = Math.abs(radius);
      this.amountOfPoints = verifyAmountOfPoints(amountOfPoints);
      this.collisionDetector = buildCollisionDetector();
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
      super.transform(position);
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
   public double getDimensionRadius() {
      return radius;
   }

   @Override
   public Position getCenter() {
      return center;
   }

   private static List<PathSegment> buildCircleWithCenter(Position center, int amountOfPoints, double radius) {
      List<PathSegment> path = new LinkedList<>();
      double degInc = 360 / (double) amountOfPoints;
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

   private static Position getNextCirclePos(Position center, double radius, double deg) {
      return center.rotate(deg)
            .movePositionForward4Distance(radius);
   }

   public static class CircleBuilder {

      private double radius;
      private int amountOfPoints;
      private Position center;
      private GridElement gridElement;

      private CircleBuilder() {
         // private
      }

      public CircleBuilder withRadius(double radius) {
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
            circle.setGridElement(gridElement);
         }
         return circle;
      }

      public static CircleBuilder builder() {
         return new CircleBuilder();
      }
   }

}
