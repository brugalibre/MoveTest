/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.MoveableObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
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

    public static void main(String[] args) throws InterruptedException {

	Position endPos = Positions.of(400, 10);
	Position endPos2 = Positions.of(400, 400);
	DefaultGrid grid = MirrorGridBuilder.builder(510, 510).build();
	GridElement endPosMarker = new SimpleGridElement(grid, endPos);
	GridElement endPosMarker2 = new SimpleGridElement(grid, endPos2);
	int height = 4;
	int width = 4;

	List<EndPointMoveable> moveables = new ArrayList<>(2);
	EndPointMoveable moveable = getMoveable(endPos, grid, 200, 200);
	EndPointMoveable moveable2 = getMoveable(endPos2, grid, 200, 200);
	moveables.add(moveable);
	moveables.add(moveable2);
	
	List<GridElement> gridElements = getAllGridElements(grid, height, width);
	gridElements.add(endPosMarker);
	gridElements.add(endPosMarker2);
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);
	GridElementPainter moveablePainter2 = new GridElementPainter(moveable2, getColor(moveable2), height, width);

	List<Renderer> renderers = getRenderers(grid, height, width, gridElements);
	renderers.add(moveablePainter);
	renderers.add(moveablePainter2);

	MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight());
	mainWindow.addSpielfeld(renderers, width, height);
	showGuiAndStartPainter(mainWindow);
	List<Position> positions = prepareAndMoveMoveables(moveables, mainWindow);
	preparePositionListPainter(renderers, positions);
    }

    private static void preparePositionListPainter(List<Renderer> renderers, List<Position> positions) {
	PositionListPainter renderer = renderers.stream()//
		.filter(PositionListPainter.class::isInstance)//
		.map(PositionListPainter.class::cast)//
		.findFirst()//
		.get();
	renderer.setPositions(positions);
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

	int angle = 45;
	int signum = calcSignum();
	Position randomPosition = Positions.of(300, 110);
	Position randomPosition2 = Positions.of(300, 300);
	MoveableObstacleImpl obstacle = new MoveableObstacleImpl(grid, randomPosition);
	MoveableObstacleImpl obstacle2 = new MoveableObstacleImpl(grid, randomPosition2);
	if (signum < 0) {
	    obstacle.makeTurn(angle);
	    obstacle2.makeTurn(angle);
	} else {
	    obstacle.makeTurn(angle - 180);
	    obstacle2.makeTurn(angle - 180);
	}

	allGridElement.add(obstacle);
	allGridElement.add(obstacle2);
	return allGridElement;
    }

    private static List<Position> prepareAndMoveMoveables(List<EndPointMoveable> endPointMoveables,
	    MainWindow mainWindow) throws InterruptedException {
	Map<Moveable, Boolean> isMoveableDoneMap = new HashMap<>();
	for (EndPointMoveable endPointMoveable : endPointMoveables) {
	    isMoveableDoneMap.put(endPointMoveable, false);
	}
	while (true) {
	    List<EndPointMoveable> filterDoneMoveables = filterDoneMoveables(endPointMoveables, isMoveableDoneMap);
	    for (EndPointMoveable endPointMoveable : filterDoneMoveables) {
		MoveResult moveResult = endPointMoveable.moveForward2EndPos();
		isMoveableDoneMap.put(endPointMoveable, moveResult.isDone());
		Thread.sleep(2);
	    }
	    if (filterDoneMoveables.isEmpty()) {
		break;
	    }
	}
	return endPointMoveables.stream().map(Moveable::getPositionHistory).flatMap(List::stream)
		.collect(Collectors.toList());
    }

    private static List<EndPointMoveable> filterDoneMoveables(List<EndPointMoveable> endPointMoveables,
	    Map<Moveable, Boolean> isMoveableDoneMap) {
	List<EndPointMoveable> filteredPointMoveables = new ArrayList<EndPointMoveable>();
	for (EndPointMoveable endPointMoveable : endPointMoveables) {
	    if (!isMoveableDoneMap.get(endPointMoveable)) {
		filteredPointMoveables.add(endPointMoveable);
	    }
	}
	return filteredPointMoveables;
    }

    private static int calcSignum() {
	double random = Math.random();
	int signum = -1;
	if (random > 0.5) {
	    signum = 1;
	}
	return signum;
    }

    private static List<Renderer> getRenderers(DefaultGrid grid, int height, int width,
	    List<GridElement> gridElements) {
	List<Renderer> renderers = gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
	renderers.add(new GridPainter(grid));
	renderers.add(new PositionListPainter(Collections.emptyList(), Color.GREEN, height, width));
	return renderers;
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : gridElement instanceof Moveable ? Color.RED : Color.BLUE;
    }

    private static EndPointMoveable getMoveable(Position endPos, Grid grid, int height, int width) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.06, 0.7d, 60, 70, 50, 15);
	Position pos = Positions.of(height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc());
	EndPointMoveable moveable = EndPointMoveableBuilder.builder()
		.withEndPosition(endPos)//
		.withGrid(grid)//
		.withPosition(pos)
		.withHandler(new EvasionStateMachine(detector, endPos, config))//
		.widthEndPosition(endPos)//
		.build();
	moveable.prepare();
	return moveable;
    }
}
