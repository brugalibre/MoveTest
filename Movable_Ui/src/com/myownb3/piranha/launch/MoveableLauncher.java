/**
 * 
 */
package com.myownb3.piranha.launch;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Dimension;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.SwappingGrid;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;
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

    public static void main(String[] args) throws InterruptedException {

	DefaultGrid grid = new SwappingGrid(200, 200);
	int height = 5;
	int width = 5;

	List<Moveable> moveables = getMoveables(grid, height, width);
	List<Renderer> renderers = getRenderers(moveables, height, width);
	renderers.add(new GridPainter(grid));
	MainWindow mainWindow = new MainWindow(renderers);
	mainWindow.show();

	prepareAndMoveMoveables(moveables, mainWindow);
    }

    /**
     * @param moveables
     * @param mainWindow
     * @throws InterruptedException
     */
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

    private static List<Renderer> getRenderers(Collection<Moveable> moveables, int height, int width) {

	return moveables.stream()//
		.map(moveable -> new GridElementPainter(moveable, Color.RED, height, width))//
		.collect(Collectors.toList());
    }

    private static List<Moveable> getMoveables(Grid grid, int height, int width) {
	Dimension dimension = grid.getDimension();
	return Arrays.asList(new MoveableBuilder(grid, Positions.getRandomPosition(dimension, height, width)).build(),
		new MoveableBuilder(grid, Positions.getRandomPosition(dimension, height, width)).build(),
		new MoveableBuilder(grid, Positions.getRandomPosition(dimension, height, width)).build(),
		new MoveableBuilder(grid, Positions.getRandomPosition(dimension, height, width)).build());
    }
}
