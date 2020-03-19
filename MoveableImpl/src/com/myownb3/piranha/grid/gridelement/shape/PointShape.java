/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * The {@link PointShape} is the default {@link Shape} which each
 * {@link GridElement} has to have. It's shape is defined by a single Positions
 * which represents the {@link Position} of the {@link GridElement} itself
 * 
 * @author Dominic
 *
 */
public class PointShape extends AbstractShape {

   /**
    * Creates a new {@link PointShape}
    * 
    * @param gridElemPos
    */
   public PointShape(Position gridElemPos) {
      super(Collections.singletonList(requireNonNull(gridElemPos)));
   }

   @Override
   public void transform(Position position) {
      this.path = Collections.singletonList(position);
   }

   @Override
   public Position getPositionOnPathFor(Position position) {
      return getPosition();
   }

   public Position getPosition() {
      return path.get(0);
   }
}
