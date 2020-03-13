/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class RandomMoveableLauncherWithEndPoint {
    private static int padding = 30;
    
    public static void main(String[] args) throws InterruptedException {

	int height = 4;
	int width = 4;
	int mainWindowWidth = 700;
	int mainWindowHeight = 700;
	
	MainWindow mainWindow = new MainWindow(mainWindowWidth, mainWindowHeight, padding, height);
	MirrorGrid grid = buildMirrorGrid(mainWindow, mainWindowWidth);
	Position startPos = Positions.getRandomPosition(grid.getDimension(), height, width);
	List<Position> endPosList = getEndPosList(height, width, grid);
	List<GridElement> gridElements = getAllGridElements(grid, endPosList, height, width);

	MoveableController moveableController = buildMoveableController(grid, startPos, endPosList,
		getPostMoveFowardHandler(mainWindow, gridElements));
	List<Renderer> renderers = getRenderers(height, width, grid, gridElements, moveableController.getMoveable());

	mainWindow.addSpielfeld(renderers, grid);
	SwingUtilities.invokeLater(() -> mainWindow.show());

	prepareAndMoveMoveables(moveableController, mainWindow, gridElements);
    }

    private static MirrorGrid buildMirrorGrid(MainWindow mainWindow, int mainWindowWidth) {
	CollisionDetectionHandler collisionDetector = getCollisionDetectionHandler(mainWindow);
	return MirrorGridBuilder.builder()//
		.withMaxX(mainWindowWidth - padding)//
		.withMaxY(mainWindowWidth - padding)//
		.withMinY(padding)//
		.withMinX(padding)//
		.withCollisionDetectionHandler(collisionDetector)//
		.build();
    }

    private static MoveableController buildMoveableController(MirrorGrid grid, Position startPos,
	    List<Position> endPosList, PostMoveForwardHandler postMoveFowardHandler) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.06, 0.7d, 60, 70, 50, 15);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc());
	return MoveableControllerBuilder.builder()//
		.withStrategie(MovingStrategie.FORWARD)//
		.withEndPositions(endPosList)//
		.withPostMoveForwardHandler(postMoveFowardHandler)//
        		.withEndPointMoveable()//
        		.withGrid(grid)//
        		.withStartPosition(startPos)//
        		.withHandler(new EvasionStateMachine(detector, config))//
        		.buildAndReturnParentBuilder()
        	.build();//
    }

    private static List<Position> getEndPosList(int height, int width, MirrorGrid grid) {
	int amountOfEndPos = 3;
	List<Position> endPosList = new ArrayList<>(amountOfEndPos);
	for (int i = 0; i < amountOfEndPos; i++) {
	    endPosList.add(Positions.getRandomPosition(grid.getDimension(), height, width));
	}
	return endPosList;
    }

    private static PostMoveForwardHandler getPostMoveFowardHandler(MainWindow mainWindow,
	    List<GridElement> gridElements) {
	return moveableRes -> {
	    moveGridElementsForward(gridElements);
	    SwingUtilities.invokeLater(() -> mainWindow.refresh());
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		// ignore
	    }
	};
    }

    private static CollisionDetectionHandler getCollisionDetectionHandler(MainWindow mainWindow) {
	return (a, b) -> {
	    SwingUtilities.invokeLater(() -> mainWindow.showCollisionInfo());
	};
    }

    private static List<GridElement> getAllGridElements(DefaultGrid grid, List<Position> endPosList, int height,
	    int width) {
	List<GridElement> allGridElement = endPosList.stream()//
		.map(endPos -> new SimpleGridElement(grid, endPos))//
		.collect(Collectors.toList());

//	int amount = 0;
	int amount = 80;
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition);

	    allGridElement.add(obstacle);
	}

	return allGridElement;
    }

    private static void prepareAndMoveMoveables(MoveableController moveableController, MainWindow mainWindow,
	    List<GridElement> allGridElements) throws InterruptedException {
	turnGridElements(allGridElements);
	while (true) {
	    moveableController.leadMoveable();
	    Thread.sleep(5);
	}
    }

    private static void turnGridElements(List<GridElement> allGridElements) {
	allGridElements.stream()//
		.filter(MoveableObstacleImpl.class::isInstance)
		.map(MoveableObstacleImpl.class::cast)//
		.forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
    }

    private static void moveGridElementsForward(List<GridElement> allGridElements) {
	allGridElements.stream()//
		.filter(MoveableObstacleImpl.class::isInstance)
		.map(MoveableObstacleImpl.class::cast)//
		.forEach(obstacle -> obstacle.moveForward());
    }

    private static List<Renderer> getRenderers(int height, int width, MirrorGrid grid, List<GridElement> gridElements,
	    Moveable moveable) {
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	renderers.add(new GridElementPainter(moveable, getColor(moveable), height, width));
	return renderers;
    }
    
    private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements, int height,
	    int width) {
	return gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK
		: gridElement instanceof Moveable ? Color.RED : Color.GREEN.darker();
    }
}
