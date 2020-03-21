/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
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
   public void check4Collision(CollisionDetector collisionDetector, Position newPosition, List<Avoidable> allAvoidables) {
      // Since the 'newPosition' is already transformed, we can call the detector directly
      collisionDetector.checkCollision(gridElement, getPosition(), newPosition, allAvoidables);
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
