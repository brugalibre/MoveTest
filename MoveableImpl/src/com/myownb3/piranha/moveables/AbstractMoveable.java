/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.AbstractGridElement;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.helper.Helper;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

    private Helper helper;

    public AbstractMoveable(Grid grid, Position position, Helper helper) {
	super(grid, position);
	this.helper = helper;
	this.helper.checkPostConditions(grid, this);
    }

    public AbstractMoveable(Grid grid, Position position) {
	this(grid, position, new Helper());
    }

    @Override
    public void moveForward() {
	helper.moveForward(grid, this, getUpdater());
    }

    @Override
    public void moveForward(int amount) {
	helper.moveForward(grid, this, amount, getUpdater());
    }

    @Override
    public void moveBackward() {
	helper.moveBackward(this, grid, getUpdater());
    }

    @Override
    public void moveBackward(int amount) {
	helper.moveBackward(grid, this, amount, getUpdater());
    }

    @Override
    public void turnLeft() {
	makeTurn(90);
    }

    @Override
    public void makeTurn(double degree) {
	helper.makeTurn(grid, this, degree);
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }

    @FunctionalInterface
    public static interface Updater {
	public void update(Moveable moveable, Position pos);
    }

    /**
     * @return a Callback handler in order to update a {@link Moveable} after
     *         certain operations are done. This is necessary because those
     *         operations happening outside this immutable Moveable
     */
    private Updater getUpdater() {
	return (moveable, pos) -> {
	    ((AbstractMoveable) moveable).position = pos;
	};
    }
}