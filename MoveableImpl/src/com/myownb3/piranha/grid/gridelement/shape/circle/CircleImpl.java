/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape.circle;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.grid.gridelement.shape.circle.detection.CircleCollisionDetectorImpl;

/**
 * @author Dominic
 *
 */
public class CircleImpl extends AbstractShape implements Circle {

   private static final int AMOUNT_OF_PATH_POINTS_4_DETECTION = 150;
   private int amountOfPoints;
   private int radius;
   private Position center;

   private CircleImpl(List<Position> path, Position center, int amountOfPoints, int radius) {
      super(path);
      this.radius = Math.abs(radius);
      this.center = center;
      this.amountOfPoints = verifyAmountOfPoints(amountOfPoints);
   }

   @Override
   protected CollisionDetector buildCollisionDetector() {
      return new CircleCollisionDetectorImpl(this);
   }

   @Override
   public void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<Avoidable> allAvoidables) {
      collisionDetector.checkCollision(collisionDetectionHandler, gridElement, null/*old value not necessary*/, newPosition,
            allAvoidables);
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return buildCircleWithCenter(center, AMOUNT_OF_PATH_POINTS_4_DETECTION, radius);
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
   public Position getPositionOnPathFor(Position position) {
      return getNextCirclePos(position, radius, 0);
   }

   @Override
   public int getRadius() {
      return radius;
   }

   @Override
   public Position getCenter() {
      return center;
   }

   private static List<Position> buildCircleWithCenter(Position center, int amountOfPoints, int radius) {
      List<Position> path = new LinkedList<>();
      double degInc = 360 / amountOfPoints;
      double deg = 0;
      for (int i = 0; i < amountOfPoints; i++) {
         Position pos = getNextCirclePos(center, radius, deg);
         deg = deg + degInc;
         path.add(pos);
      }

      return path;
   }

   private static Position getNextCirclePos(Position center, int radius, double deg) {
      Position pos = Positions.of(center);
      pos.rotate(deg);
      double effectDistance = center.calcDistanceTo(pos);
      while (effectDistance < radius) {
         pos = Positions.movePositionForward(pos);
         effectDistance = center.calcDistanceTo(pos);
      }
      return pos;
   }

   public static class CircleBuilder {

      private int radius;
      private int amountOfPoints;
      private Position center;
      private GridElement gridElement;

      public CircleBuilder(int radius) {
         this.radius = radius;
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
         List<Position> path = buildCircleWithCenter(center, amountOfPoints, radius);
         CircleImpl circle = new CircleImpl(path, center, amountOfPoints, radius);
         if (nonNull(gridElement)) {
            ((AbstractShape) circle).setGridElement(gridElement);
         }
         return circle;
      }
   }

}
