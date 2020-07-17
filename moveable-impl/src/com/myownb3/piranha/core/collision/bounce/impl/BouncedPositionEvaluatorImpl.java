package com.myownb3.piranha.core.collision.bounce.impl;

import static com.myownb3.piranha.util.vector.VectorUtil.getUnitVector;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.collision.bounce.BouncedPositionEvaluator;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

/**
 * The {@link BouncedPositionEvaluatorImpl} evaluate a bounced {@link Position} after a collision
 * 
 * @author Dominic
 *
 */
public class BouncedPositionEvaluatorImpl implements BouncedPositionEvaluator {

   @Override
   public Position calculateBouncedPosition(PathSegment pathSegment, Position gridElemPosition) {
      Float64Vector normalVectorAtBeginPos = pathSegment.getNormalVectorAtBeginPos();
      return rotatePosition(gridElemPosition, getUnitVector(normalVectorAtBeginPos));
   }

   private Position rotatePosition(Position gridElemPosition, Float64Vector unitNorm2PathSegVector) {
      Float64Vector currentVelocity = gridElemPosition.getDirection().getVector();

      double vectorVTimesVectorN = currentVelocity.times(unitNorm2PathSegVector).doubleValue();
      Float64Vector currentVelocityUComponent = unitNorm2PathSegVector.times(vectorVTimesVectorN);
      Float64Vector currentVelocityWComponent = currentVelocity.minus(currentVelocityUComponent);
      Float64Vector newVelocityAsVector = currentVelocityWComponent.minus(currentVelocityUComponent);
      Direction newVelocityDirection = Directions.of(newVelocityAsVector.getValue(0), newVelocityAsVector.getValue(1));
      return Positions.of(newVelocityDirection, gridElemPosition.getX(), gridElemPosition.getY(), gridElemPosition.getZ());
   }
}
