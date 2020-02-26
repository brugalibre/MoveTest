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
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
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
public class RandomMoveableLauncher {

    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	MirrorGrid grid = MirrorGridBuilder.builder()
		.withMaxX(700)
		.withMaxY(700)//
		.build();
	int height = 4;
	int width = 4;

	Moveable moveable = getMoveable(grid, height, width);
	List<GridElement> gridElements = getAllGridElements(grid, height, width);
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);

	renderers.add(moveablePainter);
	renderers.add(new GridPainter(grid));

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

    private static void prepareAndMoveMoveables(Moveable moveable, MainWindow mainWindow) throws InterruptedException {

	moveable.makeTurn(MathUtil.getRandom(360));
	turnGridElements();

	int counter = 0;
	while (true) {
	    moveable.moveForward();

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

    private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements, int height,
	    int width) {
	return gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : Color.RED;
    }

    private static Moveable getMoveable(Grid grid, int height, int width) {

	Dimension dimension = grid.getDimension();
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 40, 80, 70, 5);
	Position pos = Positions.getRandomPosition(dimension, height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc() + height);
	return MoveableBuilder.builder(grid, pos)//
		.withHandler(new EvasionStateMachine(detector, config))//
		.build();
    }
}
