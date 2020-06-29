package com.myownb3.piranha.core.grid.gridelement.position;

import static com.myownb3.piranha.util.MathUtil.calcAngleBetweenVectors;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * The {@link PositionHelper} contains methods in order to shift or create new {@link Position}s
 * 
 * @author Dominic
 *
 */
public class PositionHelper {

   /**
    * Creates a new {@link Position} with the new x-axis value =
    * {@link Direction#getForwardX()} + {@link Position#getX()} and the new y-axis
    * value = {@link Direction#getForwardY()} + {@link Position#getY()}
    * 
    * @param position
    *        the position
    * @return a new {@link Position}
    */
   public Position movePositionForward(Position position) {
      Direction direction = position.getDirection();
      double newX = position.getX() + direction.getForwardX();
      double newY = position.getY() + direction.getForwardY();
      return Positions.of(direction, newX, newY, position.getZ());
   }

   /**
    * The given {@link Position} is moved forward for the given amount
    * 
    * @param position
    *        the {@link Position} to move forward
    * @param amount
    *        the amount of times it is moved forward
    * 
    * @return a new {@link Position}
    */
   public Position movePositionForward(Position position, int amount) {
      Position position2MoveForward = position;
      for (int i = 0; i < amount; i++) {
         position2MoveForward = movePositionForward(position2MoveForward);
      }
      return position2MoveForward;
   }

   /**
    * Creates a new {@link Position} by moving the given position for the given distance
    * 
    * @param pos
    *        the Position to move
    * @param distance
    *        the distance
    * 
    * @return a new {@link Position} by moving the given position for the given distance
    */
   public Position movePositionForward4Distance(Position pos, double distance) {
      Position originPos = Positions.of(pos);
      double currentDistance = originPos.calcDistanceTo(pos);
      while (currentDistance < distance) {
         pos = movePositionForward(pos);
         currentDistance = originPos.calcDistanceTo(pos);
      }
      return pos;
   }

   /**
    * Creates a new {@link Position} by moving the given position backward for the given distance
    * 
    * @param pos
    *        the Position to move
    * @param distance
    *        the distance
    * 
    * @return a new {@link Position} by moving the given position for the given distance
    * 
    */
   public Position movePositionBackward4Distance(Position pos, double distance) {
      return movePositionForward4Distance(pos.rotate(180), distance)
            .rotate(180);
   }

   /**
    * Calculates the angle between the two Vectors which can be created between the
    * given {@link Position}s. The direction of the first position is considered.
    * 
    * @param pos
    *        the position
    * @param otherPosition
    *        the other {@link Position}
    *        {@link Grid}
    * @return the calculated angle with a precision of three decimal places
    */
   public double calcAngleBetweenPositions(Position pos, Position otherPosition) {
      Float64Vector moveable2GridElemVector = getVectorFromMoveable2GridElement(pos, otherPosition);
      Float64Vector moveableDirectionVector = pos.getDirection().getVector();
      return calcAngleBetweenVectors(moveable2GridElemVector, moveableDirectionVector);
   }

   private static Float64Vector getVectorFromMoveable2GridElement(Position pos, Position gridElemPos) {
      Float64Vector moveableVector = pos.getVector();
      Float64Vector gridElemVector = gridElemPos.getVector();
      return gridElemVector.minus(moveableVector);
   }


   /**
    * Builds a {@link List} with {@link Position} which are placed between the two given {@link Position}s
    * 
    * @param pathPos1
    *        the start {@link Position}
    * @param pathPos2
    *        the end {@link Position}
    * @param distanceBetweenPosOnColDetectionPath
    *        the minimal distance between two Positions
    * @return
    */
   public List<Position> buildPositionsBetweenTwoPositions(Position pathPos1, Position pathPos2, double distanceBetweenPosOnColDetectionPath) {
      Position nextPathElement = Positions.of(pathPos1);
      List<Position> pathBetweenTwoPositions = new ArrayList<>();
      pathBetweenTwoPositions.add(pathPos1);
      Float64Vector nexPathPosVector = pathPos1.getVector();
      Float64Vector vectorFromPos1ToPos2 = getUnitVector(pathPos2, nexPathPosVector);

      // Now move the Position 1 forward to Position 2 and add all those elements in between 
      double distancePos2ToPos1 = pathPos2.calcDistanceTo(pathPos1);
      while (distancePos2ToPos1 >= distanceBetweenPosOnColDetectionPath) {
         nexPathPosVector = nexPathPosVector.plus(vectorFromPos1ToPos2);
         nextPathElement = Positions.of(nexPathPosVector, 9);
         distancePos2ToPos1 = pathPos2.calcDistanceTo(nextPathElement);
         pathBetweenTwoPositions.add(nextPathElement);
      }
      return pathBetweenTwoPositions;
   }

   private static Float64Vector getUnitVector(Position pathPos2, Float64Vector nexPathPosVector) {
      Float64Vector pos1ToPos2Vector = pathPos2.getVector().minus(nexPathPosVector);
      return VectorUtil.getUnitVector(pos1ToPos2Vector);
   }
}
