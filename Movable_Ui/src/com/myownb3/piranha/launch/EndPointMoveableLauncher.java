/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
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
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableLauncher {

    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	Position endPos = Positions.of(400, 400);
	DefaultGrid grid = new MirrorGrid(510, 510);
	GridElement endPosMarker = new SimpleGridElement(grid, endPos);
	int height = 4;
	int width = 4;

	EndPointMoveable moveable = getMoveable(endPos, grid, 200, 200);
	List<GridElement> gridElements = getAllGridElements(grid, height, width);
	gridElements.add(endPosMarker);
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);

	List<Renderer> renderers = getRenderers(grid, height, width, gridElements, moveablePainter);

	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(),
		grid.getDimension().getHeight());
	showGuiAndStartPainter(mainWindow);
	List<Position> positions = prepareAndMoveMoveables(moveable, mainWindow);
	preparePositionListPainter(renderers, positions);
    }

    private static void preparePositionListPainter(List<Renderer> renderers, List<Position> positions) {
	PositionListPainter renderer = renderers.stream().filter(PositionListPainter.class::isInstance)//
		.map(PositionListPainter.class::cast)//
		.findFirst()//
		.get();
	renderer.setPositions (positions);
    }

    private static void showGuiAndStartPainter(MainWindow mainWindow) {
	SwingUtilities.invokeLater(() -> mainWindow.show());
	new Thread(() -> {
	    while (true) {
		SwingUtilities.invokeLater(() -> mainWindow.refresh());
		try {
		    Thread.sleep(2);
		} catch (InterruptedException e) {
		}
	    }
	}).start();
    }

    private static List<GridElement> getAllGridElements(DefaultGrid grid, int height, int width) {

	List<GridElement> allGridElement = new ArrayList<>();

	int amount = 1;
	int angle = 45;
	for (int i = 0; i < amount; i++) {
	    int signum = calcSignum();
	    Position randomPosition = Positions.of(300, 300);
	    MoveableObstacleImpl obstacle = new MoveableObstacleImpl(grid, randomPosition);
	    if (signum < 0) {
		obstacle.makeTurn(angle);
	    } else {
		obstacle.makeTurn(angle - 180);
	    }
//	    int factor = Math.max(1, (int) MathUtil.getRandom(10));
//	    int randomNo = Math.max(1, (int) MathUtil.getRandom(50));
//	    obstacle.moveForward(randomNo * factor);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}
	return allGridElement;
    }

    private static List<Position> prepareAndMoveMoveables(EndPointMoveable endPointMoveable, MainWindow mainWindow)
	    throws InterruptedException {
	while (true) {
	    MoveResult moveResult = endPointMoveable.moveForward2EndPos();
	    if (moveResult.isDone()) {
		break;
	    }
	    Thread.sleep(2);
	}
	return endPointMoveable.getPositionHistory();
    }

    private static int calcSignum() {
	double random = Math.random();
	int signum = -1;
	if (random > 0.5) {
	    signum = 1;
	}
	return signum;
    }

    private static List<Renderer> getRenderers(DefaultGrid grid, int height, int width, List<GridElement> gridElements,
	    GridElementPainter moveablePainter) {
	List<Renderer> renderers = gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
	renderers.add(moveablePainter);
	renderers.add(new GridPainter(grid));
	renderers.add(new PositionListPainter(Collections.emptyList(), Color.GREEN, height, width));
	return renderers;
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : 
	    gridElement instanceof Moveable ? Color.RED : Color.BLUE;
    }

    private static EndPointMoveable getMoveable(Position endPos, Grid grid, int height, int width) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.05, 0.7d, 60, 70, 50, 15);
	Position pos = Positions.of(height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc());
	EndPointMoveable moveable = MoveableBuilder.builder(grid, pos)//
		.withHandler(new EvasionStateMachine(detector, endPos, config))//
		.widthEndPosition(endPos)//
		.buildEndPointMoveable();
	moveable.prepare();
	return moveable;
    }
}
