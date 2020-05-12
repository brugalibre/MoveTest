package com.myownb3.piranha.core.grid.collision.bounce;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link BouncedPositionEvaluator} is responsible for calculating a bounced {@link Position} after a collision
 * 
 * @author Dominic
 *
 */
public interface BouncedPositionEvaluator {

   /**
    * Calculates a bounced {@link Position} according to the collision with the given {@link Float64Vector} of a {@link PathSegment}
    * 
    * @param pathSegment
    *        the {@link PathSegment} with which the {@link GridElement} is collided
    * @param gridElemPosition
    *        the {@link Position} of the {@link GridElement}
    * @return a bounced {@link Position}
    */
   Position calculateBouncedPosition(PathSegment pathSegment, Position gridElemPosition);

}
