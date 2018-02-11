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
import com.myownb3.piranha.moveables.helper.MoveablePostActionHandler;

/**
 * The {@link AbstractMoveable} is responsible for doing the basic move elements
 * on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveable extends AbstractGridElement implements Moveable {

    private MoveablePostActionHandler handler;

    public AbstractMoveable(Grid grid, Position position, MoveablePostActionHandler handler) {
	super(grid, position);
	this.handler = handler;
	this.handler.handlePostConditions(grid, this);
    }

    public AbstractMoveable(Grid grid, Position position) {
	this(grid, position, (g, m) -> {/* This empty handler does nothing */
	});
    }

    @Override
    public void moveForward() {
	position = grid.moveForward(position);
	handler.handlePostConditions(grid, this);
    }

    @Override
    public void moveForward(int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveForward();
	}
    }

    @Override
    public void moveBackward() {
	position = grid.moveBackward(position);
	handler.handlePostConditions(grid, this);
    }

    @Override
    public void moveBackward(int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveBackward();
	}
    }

    @Override
    public void turnLeft() {
	makeTurn(90);
    }

    @Override
    public void makeTurn(double degree) {
	position.rotate(degree);
	handler.handlePostConditions(grid, this);
    }

    @Override
    public void turnRight() {
	makeTurn(-90);
    }

    private void verifyAmount(int amount) {
	if (amount <= 0) {
	    throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
	}
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

	public MoveableBuilder withHandler(MoveablePostActionHandler handler) {
	    ((AbstractMoveable) moveable).handler = handler;
	    return this;
	}

	public Moveable build() {
	    MoveablePostActionHandler helper = ((AbstractMoveable) moveable).handler;
	    Objects.requireNonNull(helper, "A Moveable always needs a MoveableHelper!");
	    helper.handlePostConditions(((AbstractMoveable) moveable).grid, moveable);
	    return this.moveable;
	}

	private class SimpleMoveable extends AbstractMoveable {

	    private SimpleMoveable(Grid grid, Position position) {
		super(grid, position);
	    }
	}
    }
}