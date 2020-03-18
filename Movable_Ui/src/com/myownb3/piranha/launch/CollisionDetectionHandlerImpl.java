/**
 * 
 */
package com.myownb3.piranha.launch;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.MoveableController;
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
    public void handleCollision(Avoidable avoidable, Position newPosition) {
	if (stoppable.isRunning()) {
	    stoppable.stop();
	    if (moveableController != null) {
		moveableController.stop();
	    }
	    SwingUtilities.invokeLater(() -> mainWindow.showCollisionInfo());
	}
    }

    public final void setMoveableController(MoveableController moveableController) {
	this.moveableController = moveableController;
    }
}