/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape.position;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionCollisionDetectorImpl.PositionCollisionDetectorBuilder;

/**
 * The {@link PositionShape} is the default {@link Shape} which each
 * {@link GridElement} has to have. It's shape is defined by a single Positions
 * which represents the {@link Position} of the {@link GridElement} itself
 * 
 * @author Dominic
 *
 */
public class PositionShape extends AbstractShape {

   /**
    * Creates a new {@link PositionShape}
    * 
    * @param gridElemPos
    */
   public PositionShape(Position gridElemPos) {
      super(Collections.singletonList(requireNonNull(gridElemPos)));
   }

   @Override
   protected CollisionDetector buildCollisionDetector() {
      return PositionCollisionDetectorBuilder.builder()
            .withCollisionDistance(2)
            .build();
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return Collections.singletonList(getPosition());
   }

   @Override
   public void check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition, List<Avoidable> allAvoidables) {
      // Since the 'newPosition' is already transformed, we can call the detector directly
      collisionDetector.checkCollision(collisionDetectionHandler, gridElement, getPosition(), newPosition, allAvoidables);
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      requireNonNull(gridElement, "A Shape needs a GridElement when calling 'detectObject'");
      detector.detectObject(gridElement, getPosition(), detectorPosition);
      return detector.hasObjectDetected(gridElement);
   }


   @Override
   public void transform(Position position) {
      this.path = Collections.singletonList(position);
   }

   @Override
   public Position getFurthermostBackPosition() {
      return getPosition();
   }

   @Override
   public Position getFurthermostFrontPosition() {
      return getPosition();
   }

   public Position getPosition() {
      return path.get(0);
   }
}
