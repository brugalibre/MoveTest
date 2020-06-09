package com.myownb3.piranha.core.grid.gridelement.position;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link PositionTransformator} is used whenever you have a {@link GridElement} or {@link Shape} whose (depending) {@link Position}
 * rely on a other {@link Position}.
 * If this other {@link Position} is moved or turned, the {@link PositionTransformator} makes shure that it's depending {@link Position} is
 * transformed according the transformation rule between those two Position
 * 
 * @author Dominic
 *
 */
public interface PositionTransformator {

   /**
    * Transforms the given Position according the rules of this {@link PositionTransformator}
    * 
    * @param position
    *        the {@link Position} to transform
    * @return a transformed {@link Position}
    */
   Position transform(Position position);
}
