/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

    Helper helper;

    /**
     * @param grid
     * @param position
     */
    public AbstractMoveable(Grid grid, Position position, Helper helper) {
	super(position, grid);
	this.helper = helper;
	this.helper.checkPostConditions(this);
    }

    /**
     * @param grid
     * @param position
     */
    public AbstractMoveable(Grid grid, Position position) {
	this(grid, position, new Helper());
    }

    @Override
    public void moveForward() {
	helper.moveForward(this);
    }

    @Override
    public void moveForward(int amount) {
	helper.moveForward(this, amount);
    }

    @Override
    public void moveBackward() {
	helper.moveBackward(this);
    }

    @Override
    public void moveBackward(int amount) {
	helper.moveBackward(this, amount);
    }

    @Override
    public void turnLeft() {
	makeTurn(90);
    }

    @Override
    public void makeTurn(double degree) {
	helper.makeTurn(this, degree);
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }
}