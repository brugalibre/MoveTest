/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
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
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class RandomMoveableLauncherWithEndPoint {

    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	int height = 4;
	int width = 4;
	MainWindow mainWindow = new MainWindow(700, 700);

	CollisionDetectionHandler collisionDetector = getCollisionDetectionHandler(mainWindow);
	MirrorGrid grid = MirrorGridBuilder.builder().withMaxX(700).withMaxY(700)//
		.withCollisionDetectionHandler(collisionDetector).build();

	Position pos = Positions.getRandomPosition(grid.getDimension(), height, width);
	Position pos1 = Positions.getRandomPosition(grid.getDimension(), height, width);
	Position pos2 = Positions.getRandomPosition(grid.getDimension(), height, width);
	List<Position> endPosList = Arrays.asList(pos, pos1, pos2);
	EndPointMoveable moveable = getMoveable(pos, grid, height, width);
	   
	MoveableController moveableController = MoveableControllerBuilder.builder()//
		.withStrategie(MovingStrategie.FORWARD)
		.withEndPointMoveable(moveable)//
		.withEndPositions(endPosList)//
		.withPostMoveForwardHandler(getPostMoveFowardHandler(mainWindow))//
		.build();
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);
	List<GridElement> gridElements = getAllGridElements(grid, endPosList, height, width);
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	renderers.add(moveablePainter);
	renderers.add(new GridPainter(grid));

	mainWindow.addSpielfeld(renderers, width, height);
	SwingUtilities.invokeLater(() -> mainWindow.show());

	prepareAndMoveMoveables(moveableController, mainWindow);
    }

    private static PostMoveForwardHandler getPostMoveFowardHandler(MainWindow mainWindow) {
	return moveableRes -> {
	    moveGridElementsForward();
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

	int amount = 0;//80;
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}

	return allGridElement;
    }

    private static void prepareAndMoveMoveables(MoveableController moveableController, MainWindow mainWindow)
	    throws InterruptedException {
	turnGridElements();
	moveableController.leadMoveable();
    }

    private static void turnGridElements() {
	GRID_ELEMENTS.stream()//
		.map(MoveableObstacleImpl.class::cast)//
		.forEach(obstacle -> obstacle.makeTurn(MathUtil.getRandom(360)));
    }

    private static void moveGridElementsForward() {
	GRID_ELEMENTS.stream()//
		.map(MoveableObstacleImpl.class::cast)//
		.forEach(obstacle -> obstacle.moveForward());
    }

    private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements, int height,
	    int width) {
	return gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : gridElement instanceof Moveable ? Color.RED : Color.BLUE;
	   
    }

    private static EndPointMoveable getMoveable(Position initEndPos, Grid grid, int height, int width) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.06, 0.7d, 60, 70, 50, 15);
	Position pos = Positions.of(height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc());
	return MoveableBuilder.builder(grid, pos)//
		.withHandler(new EvasionStateMachine(detector, initEndPos, config))//
		.widthEndPosition(initEndPos)//
		.buildEndPointMoveable();
    }
}
