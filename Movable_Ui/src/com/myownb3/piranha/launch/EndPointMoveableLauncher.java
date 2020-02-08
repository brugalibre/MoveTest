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

	Position endPos = Positions.of(700, 700);
	DefaultGrid grid = new MirrorGrid((int) endPos.getX(), (int) endPos.getY());
	int height = 4;
	int width = 4;

	EndPointMoveable moveable = getMoveable(endPos, grid, 200, 200);
	List<GridElement> gridElements = getAllGridElements(grid, height, width);
	GridElementPainter moveablePainter = new GridElementPainter(moveable, getColor(moveable), height, width);

	List<Renderer> renderers = getRenderers(grid, height, width, gridElements, moveablePainter);

	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(),
		grid.getDimension().getHeight());
	showGuiAndStartPainter(mainWindow);
	prepareAndMoveMoveables(moveable, mainWindow);
    }

    private static void showGuiAndStartPainter(MainWindow mainWindow) {
	SwingUtilities.invokeLater(() -> mainWindow.show());
	new Thread(() ->{	while (true) {
	    SwingUtilities.invokeLater(() -> mainWindow.refresh());
	    try {
		Thread.sleep(2);
	    } catch (InterruptedException e) {
	    }
	}}).start();
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
	    int factor = Math.max(1, (int) MathUtil.getRandom(10));
	    int randomNo = Math.max(1, (int) MathUtil.getRandom(50));
//	    obstacle.moveForward(randomNo * factor);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}
	return allGridElement;
    }

    private static void prepareAndMoveMoveables(EndPointMoveable endPointMoveable, MainWindow mainWindow)
	    throws InterruptedException {
	while (true) {
	    endPointMoveable.moveForward2EndPos();
	    Thread.sleep(2);
	}
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
	return renderers;
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Obstacle ? Color.BLACK : Color.RED;
    }

    private static EndPointMoveable getMoveable(Position endPos, Grid grid, int height, int width) {
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(1, 0.05, 0.7d, 60, 150, 70, 50, 15);
	Position pos = Positions.of(height, width);
	Detector detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		config.getEvasionAngle(), config.getEvasionAngleInc());
	EndPointMoveable moveable = new MoveableBuilder(grid, pos)//
		.withHandler(new EvasionStateMachine(detector, endPos, config))//
		.widthEndPosition(endPos)//
		.buildEndPointMoveable();
	moveable.prepare();
	return moveable;
    }
}
