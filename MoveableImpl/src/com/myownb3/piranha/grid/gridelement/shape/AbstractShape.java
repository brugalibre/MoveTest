/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * @author Dominic
 *
 */
public abstract class AbstractShape implements Shape {

   protected List<Position> path;
   protected GridElement gridElement;
   protected CollisionDetector collisionDetector;

   /**
    * Creates a new {@link AbstractShape}
    */
   public AbstractShape(List<Position> path) {
      this.path = path;
      this.collisionDetector = buildCollisionDetector();
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      requireNonNull(gridElement, "A Shape needs a GridElement when calling 'detectObject'");
      List<Position> path4Detection = buildPath4Detection();
      detector.detectObjectAlongPath(gridElement, path4Detection, detectorPosition);
      return detector.hasObjectDetected(gridElement);
   }

   /**
    * Provides the {@link CollisionDetector} used by this {@link Shape}
    * 
    * @return the {@link CollisionDetector}
    */
   protected abstract CollisionDetector buildCollisionDetector();

   /**
    * Builds a List of {@link Position} which is used to verify if the {@link GridElement} of this {@link Shape}
    * has been detected
    * 
    * @return the path of this Shape
    */
   protected abstract List<Position> buildPath4Detection();

   @Override
   public List<Position> getPath() {
      return Collections.unmodifiableList(path);
   }

   public void setGridElement(GridElement gridElement) {
      this.gridElement = requireNonNull(gridElement);
   }
}
