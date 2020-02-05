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
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
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
public class EndPointMoveableLauncher {

    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	DefaultGrid grid = new MirrorGrid(700, 700);
	int height = 4;
	int width = 4;

	Position endPos = Positions.of(600, 600);
	EndPointMoveable moveable = getMoveable(endPos, grid, height, width);
	List<GridElement> gridElements = getAllGridElements(grid, height, width);
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);

	List<Renderer> renderers = getRenderers(grid, height, width, gridElements, moveablePainter);

	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(),
		grid.getDimension().getHeight());
	SwingUtilities.invokeLater(() -> mainWindow.show());

	prepareAndMoveMoveables(moveable, mainWindow);
    }

    private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width) {

	List<GridElement> allGridElement = new ArrayList<>();

	int amount = 80;
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    Obstacle obstacle = new MoveableObstacleImpl(grid, randomPosition);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}

	return allGridElement;
    }

    private static void prepareAndMoveMoveables(EndPointMoveable endPointMoveable, MainWindow mainWindow) throws InterruptedException {

	turnGridElements();

	int counter = 0;
	while (true) {
	    endPointMoveable.moveForward2EndPos();
	    if (counter % 5 == 0) {
		moveGridElementsForward();
	    }
	    counter++;
	    mainWindow.refresh();
	    Thread.sleep(1);
	}
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


    private static List<Renderer> getRenderers(DefaultGrid grid, int height, int width, List<GridElement> gridElements,
	    GridElementPainter moveablePainter) {
	List<Renderer> renderers = gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
	renderers.add(moveablePainter);
	renderers.add(new GridPainter(grid));
	return renderers;
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : Color.RED;
    }

    private static EndPointMoveable getMoveable(Position endPos, Grid grid, int height, int width) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 40, 80, 70, 5);
	Position pos = Positions.of(height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc() + height);
	EndPointMoveable moveable = (EndPointMoveable) new MoveableBuilder(grid, pos)//
		.withHandler(new EvasionStateMachine(detector, config))//
		.widthEndPosition(endPos)
		.build();
	moveable.makeTurn(MathUtil.getRandom(360));
	return moveable;
    }
}
