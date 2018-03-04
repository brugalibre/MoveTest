/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.MirrorGrid;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.SwappingGrid;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.DetectorImpl;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
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

    public static <T extends GridElement> void main(String[] args) throws InterruptedException {

	DefaultGrid grid = new MirrorGrid(200, 200);
	int height = 5;
	int width = 5;

	List<Moveable> moveables = getMoveables(grid, height, width);
	List<GridElement> gridElements = getAllGridElements(moveables, grid, height, width);
	List<Renderer> renderers = getRenderers(gridElements, height, width);
	renderers.add(new GridPainter(grid));
	MainWindow mainWindow = new MainWindow(renderers);
	mainWindow.show();

	prepareAndMoveMoveables(moveables, mainWindow);
    }

    private static List<GridElement> getAllGridElements(List<Moveable> moveables, DefaultGrid grid, int height,
	    int width) {

	List<GridElement> allGridElement = new ArrayList<>(moveables);

	int amount = 6;// (int) (Math.random() * 3 + 3);
	for (int i = 0; i < amount; i++) {
	    Position randomPosition = Positions.getRandomPosition(grid.getDimension(), height, width);
	    allGridElement.add(new ObstacleImpl(grid, randomPosition));
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
	    Thread.sleep(10);
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
	int amount = 10;// (int) (Math.random() * 10 + 5);
	for (int i = 0; i < amount; i++) {
	    Position pos = Positions.getRandomPosition(dimension, height, width);
	    Moveable moveable = new MoveableBuilder(grid, pos)//
		    .withHandler(new EvasionStateMachine(new DetectorImpl(8, 90, 15, /* 11.25 */ 5.625)))//
		    .build();
	    moveables.add(moveable);
	}

	return moveables;
    }
}
