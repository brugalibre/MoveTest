/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.SwappingGrid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
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
public class MoveableLauncher {

    /**
     * 
     */
    private static final DetectorImpl DETECTOR =  new DetectorImpl(40, 80, 70, 5);
    private static final List<Obstacle> GRID_ELEMENTS = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

	DefaultGrid grid = new MirrorGrid(800, 800);
	int height = 5;
	int width = 5;

	List<Moveable> moveables = getMoveables(grid, height, width);
	List<GridElement> gridElements = getAllGridElements(moveables, grid, height, width);
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	renderers.add(new GridPainter(grid));

	
	MainWindow mainWindow = new MainWindow(renderers, grid.getDimension().getWidth(), grid.getDimension().getHeight());
	mainWindow.show();

	prepareAndMoveMoveables(moveables, mainWindow);
    }

    public static void visualizePositionsWithJFreeChart(List<Position> posList, Obstacle obstacle) throws InterruptedException {
	visualizePositionsWithJFreeChart(posList, Collections.singletonList(obstacle));
    }

    public static void visualizePositionsWithJFreeChart(List<Position> posList, List<Obstacle> obstacles) throws InterruptedException {
	DefaultGrid grid = new SwappingGrid(500, 500);

	List<GridElement> gridElements = posList.stream()//
		.map(pos -> new ObstacleImpl(grid, pos))//
		.collect(Collectors.toList());//

	MainWindow mainWindow = new MainWindow(obstacles, gridElements, grid.getDimension().getWidth(), grid.getDimension().getHeight());
	mainWindow.show();
	Thread.sleep(Integer.MAX_VALUE);
    }

    private static List<GridElement> getAllGridElements(List<Moveable> moveables, DefaultGrid grid, int height, int width) {

	List<GridElement> allGridElement = new ArrayList<>(moveables);

	int amount = 40;//(int) (Math.random() * 10 + 3);
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    Obstacle obstacle = new ObstacleImpl(grid, randomPosition);

	    allGridElement.add(obstacle);
	    GRID_ELEMENTS.add(obstacle);
	}

	return allGridElement;
    }

    private static void prepareAndMoveMoveables(List<Moveable> moveables, MainWindow mainWindow) throws InterruptedException {

	moveables.stream()//
		.forEach(moveable -> moveable.makeTurn(MathUtil.getRandom(360)));

	while (true) {
	    moveables.stream()//
		    .forEach(moveable -> {
//			for (int i = 0; i <= 10; i++) {
			    moveable.moveForward();
			    mainWindow.refresh();
//			}
		    });

	    int timer =/* isEvasion ? 150 :*/ 1;
	    Thread.sleep(timer);
	}
    }

    private static <T extends GridElement> List<Renderer> getRenderers(List<GridElement> gridElements, int height, int width) {

	return gridElements.stream()//
		.map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), height, width))//
		.collect(Collectors.toList());
    }

    private static Color getColor(GridElement gridElement) {
	return gridElement instanceof Moveable ? Color.RED : Color.BLACK;
    }

    private static List<Moveable> getMoveables(Grid grid, int height, int width) {

	Dimension dimension = grid.getDimension();
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 8, 80, 70, 11.25);
	List<Moveable> moveables = new ArrayList<>();
	int amount = 1;// (int) (Math.random() * 10 + 5);
	for (int i = 0; i < amount; i++) {
	    Position pos = Positions.getRandomPosition(dimension, height, width);
	    Moveable moveable = new MoveableBuilder(grid, pos)//
		    .withHandler(new EvasionStateMachine(DETECTOR, config))//
		    .build();
	    moveables.add(moveable);
	}

	return moveables;
    }
}
