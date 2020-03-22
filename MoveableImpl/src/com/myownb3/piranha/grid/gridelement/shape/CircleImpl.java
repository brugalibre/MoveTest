/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;

/**
 * @author Dominic
 *
 */
public class CircleImpl extends AbstractShape implements Circle {

   private int amountOfPoints;
   private int radius;
   private Position center;

   private CircleImpl(List<Position> path, Position center, int amountOfPoints, int radius) {
      super(path);
      this.radius = Math.abs(radius);
      this.center = center;
      this.amountOfPoints = verifyAmountOfPoints(amountOfPoints);
   }

   private int verifyAmountOfPoints(int amountOfPoints) {
      if (amountOfPoints < 4) {
         throw new IllegalArgumentException("We need at least 4 points for a circle!");
      }
      return amountOfPoints;
   }


   @Override
   public void check4Collision(CollisionDetector collisionDetector, Position newCenterPos, List<Avoidable> allAvoidables) {
      List<Position> newPath = buildCircleWithCenter(newCenterPos, amountOfPoints, radius);
      Collections.sort(newPath, new CircePathPositionComparator());
      Collections.sort(path, new CircePathPositionComparator());
      for (Position newPos : newPath) {
         Position oldPos = getOldPosForTransoformedValue(newPos);
         collisionDetector.checkCollision(gridElement, oldPos, newPos, allAvoidables);
      }
   }

   private Position getOldPosForTransoformedValue(Position newPos) {
      return path.stream()
            .filter(oldPos -> oldPos.getDirection().equals(newPos.getDirection()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No old Position found on path for transformed Position '" + newPos + "'"));
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

      public CircleImpl build() {
         requireNonNull(center);
         List<Position> path = buildCircleWithCenter(center, amountOfPoints, radius);
         return new CircleImpl(path, center, amountOfPoints, radius);
      }
   }

}
