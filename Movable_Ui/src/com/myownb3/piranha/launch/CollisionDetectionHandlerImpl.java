/**
 * 
 */
package com.myownb3.piranha.launch;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.grid.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.collision.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.ui.application.MainWindow;

class CollisionDetectionHandlerImpl implements CollisionDetectionHandler {

   private MainWindow mainWindow;
   private Stoppable stoppable;
   private MoveableController moveableController;

   public CollisionDetectionHandlerImpl(Stoppable stoppable, MainWindow mainWindow) {
      this.stoppable = stoppable;
      this.mainWindow = mainWindow;
   }

   @Override
   public CollisionDetectionResult handleCollision(GridElement otherGridElement, GridElement movedGridElement, Position newPosition) {
      if (stoppable.isRunning()) {
         if (isCollisionWithMoveable(movedGridElement)) {
            stoppable.stop();
            if (moveableController != null) {
               moveableController.stop();
            }
            SwingUtilities.invokeLater(() -> mainWindow.showCollisionInfo());
         }
         SwingUtilities.invokeLater(() -> mainWindow.showCollisionInfo());
      }
      return new CollisionDetectionResultImpl(false, newPosition);
   }

   private static boolean isCollisionWithMoveable(GridElement gridElement) {
      return gridElement instanceof EndPointMoveable;
   }

   public final void setMoveableController(MoveableController moveableController) {
      this.moveableController = moveableController;
   }
}
