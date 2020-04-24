package com.myownb3.piranha.grid.gridelement.position;

import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * The {@link PositionHelper} contains methods in order to shift or create new {@link Position}s
 * 
 * @author Dominic
 *
 */
public class PositionHelper {

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
      Float64Vector nexPathPosVector = getVector(pathPos1);
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
      Float64Vector pos1ToPos2Vector = getVector(pathPos2).minus(nexPathPosVector);
      return VectorUtil.getUnitVector(pos1ToPos2Vector);
   }
}
