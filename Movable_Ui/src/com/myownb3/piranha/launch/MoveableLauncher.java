/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.Obstacle;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.SwappingGrid;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.DetectorImpl;
import com.myownb3.piranha.moveables.helper.EvasionStateMachine;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.GridPainter;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class MoveableLauncher {

    /**
     * 
     */
    private static final DetectorImpl DETECTOR = new DetectorImpl(16, 90, 15, /* 11.25 */ 5.625);
    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	DefaultGrid grid = new SwappingGrid(800, 800);
	int height = 5;
	int width = 5;

	List<Moveable> moveables = getMoveables(grid, height, width);
	List<GridElement> gridElements = getAllGridElements(moveables, grid, height, width);
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	renderers.add(new GridPainter(grid));
	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(),
		grid.getDimension().getHeight());
	mainWindow.show();

	prepareAndMoveMoveables(moveables, mainWindow);
    }

    public static void visualizePositions(List<Position> posList, Obstacle obstacle) throws InterruptedException {

	DefaultGrid grid = new SwappingGrid(500, 500);
	int height = 5;
	int width = 5;

	List<GridElement> gridElements = posList.stream()//
		.map(pos -> new ObstacleImpl(grid, pos))//
		.collect(Collectors.toList());//
	List<Renderer> renderers = getRenderers(gridElements, height, width);

	renderers.add(new GridElementPainter(obstacle, Color.RED, height, width));
	renderers.add(new GridPainter(grid));
	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(),
		grid.getDimension().getHeight());
	mainWindow.show();

	prepareAndMoveMoveables(Collections.emptyList(), mainWindow);
    }

    private static List<GridElement> getAllGridElements(List<Moveable> moveables, DefaultGrid grid, int height,
	    int width) {

	List<GridElement> allGridElement = new ArrayList<>(moveables);

	int amount = 16;// (int) (Math.random() * 3 + 3);
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    Obstacle obstacle = new ObstacleImpl(grid, randomPosition);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}

	return allGridElement;
    }

    private static void prepareAndMoveMoveables(List<Moveable> moveables, MainWindow mainWindow)
	    throws InterruptedException {

	moveables.stream()//
		.forEach(moveable -> moveable.makeTurn(MathUtil.getRandom(360)));

	while (true) {
	    moveables.stream()//
		    .forEach(Moveable::moveForward);
	    mainWindow.refresh();

	    boolean isEvasion = GRID_ELEMENTS.stream().anyMatch(obstacle -> DETECTOR.isEvasion(obstacle));
	    boolean hasDetected = GRID_ELEMENTS.stream().anyMatch(obstacle -> DETECTOR.hasObjectDetected(obstacle));
	    int timer = (isEvasion || hasDetected) ? 30 : 10;
	    Thread.sleep(timer);
	}
    }

    private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements, int height,
	    int width) {

	return gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Moveable ? Color.RED : Color.BLACK;
    }

    private static List<Moveable> getMoveables(Grid grid, int height, int width) {

	Dimension dimension = grid.getDimension();

	List<Moveable> moveables = new ArrayList<>();
	int amount = 1;// (int) (Math.random() * 10 + 5);
	for (int i = 0; i < amount; i++) {
	    Position pos = Positions.getRandomPosition(dimension, height, width);
	    Moveable moveable = new MoveableBuilder(grid, pos)//
		    .withHandler(new EvasionStateMachine(DETECTOR))//
		    .build();
	    moveables.add(moveable);
	}

	return moveables;
    }
}
