/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Objects;

import com.myownb3.piranha.grid.AbstractGridElement;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.moveables.helper.MoveableHelper;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

    private MoveableHelper helper;

    public AbstractMoveable(Grid grid, Position position, MoveableHelper helper) {
	super(grid, position);
	this.helper = helper;
	this.helper.checkPostConditions(grid, this);
    }

    public AbstractMoveable(Grid grid, Position position) {
	this(grid, position, new MoveableHelper());
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

    public static class MoveableBuilder {

	private Moveable moveable;

	public static MoveableBuilder builder() {
	    return new MoveableBuilder(new DefaultGrid(), Positions.of(0, 0));
	}

	public MoveableBuilder(Grid grid) {
	    this(grid, Positions.of(0, 0));
	}

	public MoveableBuilder(Grid grid, Position position) {
	    moveable = new SimpleMoveable(grid, position);
	}

	public MoveableBuilder withPosition(Position position) {
	    ((AbstractMoveable) moveable).position = position;
	    return this;
	}

	public MoveableBuilder withHelper(MoveableHelper helper) {
	    ((AbstractMoveable) moveable).helper = helper;
	    return this;
	}

	public Moveable build() {
	    MoveableHelper helper = ((AbstractMoveable) moveable).helper;
	    Objects.requireNonNull(helper, "A Moveable always needs a MoveableHelper!");
	    helper.checkPostConditions(((AbstractMoveable) moveable).grid, moveable);
	    return this.moveable;
	}

	private class SimpleMoveable extends AbstractMoveable {

	    private SimpleMoveable(Grid grid, Position position) {
		super(grid, position);
	    }
	}
    }
}