/**
 * 
 */
package com.myownb3.piranha.launch;

import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.ui.application.MainWindow;

class CollisionDetectionHandlerImpl extends BouncingCollisionDetectionHandlerImpl {

   private MainWindow mainWindow;
   private Stoppable stoppable;
   private MoveableController moveableController;

   public CollisionDetectionHandlerImpl(Stoppable stoppable, MainWindow mainWindow) {
      super(new BouncedPositionEvaluatorImpl());
      this.stoppable = stoppable;
      this.mainWindow = mainWindow;
   }

   @Override
   public CollisionDetectionResult handleCollision(List<CollisionGridElement> collisionGridElements, GridElement movedGridElement,
         Position newPosition) {
      if (stoppable.isRunning()) {
         if (isCollisionWithMoveable(movedGridElement, collisionGridElements)) {
            stoppable.stop();
            if (moveableController != null) {
               moveableController.stop();
            }
            SwingUtilities.invokeLater(() -> mainWindow.showCollisionInfo());
         } else {
            return super.handleCollision(collisionGridElements, movedGridElement, newPosition);
         }
      }
      return new CollisionDetectionResultImpl(newPosition);
   }


   private boolean isCollisionWithMoveable(GridElement gridElement, List<CollisionGridElement> otherGridElements) {
      return isNotCollisionWithWall(otherGridElements, gridElement)
            && (isEndPointMoveable(gridElement) || otherGridElements.stream()
                  .map(CollisionGridElement::getGridElement)
                  .anyMatch(this::isEndPointMoveable));
   }

   private boolean isNotCollisionWithWall(List<CollisionGridElement> otherGridElements, GridElement gridElement) {
      return isNotCollisionWithWall(otherGridElements) && isNotCollisionWithWall(gridElement);
   }

   private boolean isNotCollisionWithWall(List<CollisionGridElement> otherGridElements) {
      CollisionGridElement collisionGridElement = otherGridElements.get(0);
      return isNotCollisionWithWall(collisionGridElement.getGridElement());
   }

   private boolean isNotCollisionWithWall(GridElement gridElement) {
      return !(gridElement instanceof WallGridElement);
   }

   private boolean isEndPointMoveable(GridElement gridElement) {
      return gridElement instanceof EndPointMoveable;
   }

   public final void setMoveableController(MoveableController moveableController) {
      this.moveableController = moveableController;
   }
}
