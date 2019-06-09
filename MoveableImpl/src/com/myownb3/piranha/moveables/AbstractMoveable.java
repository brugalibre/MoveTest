/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.ArrayList;
import java.util.List;
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
    private List<Position> positionHistory;

    public AbstractMoveable(Grid grid, Position position, MoveablePostActionHandler handler) {
	super(grid, position);
	this.handler = handler;
	this.handler.handlePostConditions(grid, this);
	positionHistory = new ArrayList<>();
    }

    public AbstractMoveable(Grid grid, Position position) {
	this(grid, position, (g, m) -> {/* This empty handler does nothing */
	});
    }

    @Override
    public void moveMakeTurnAndForward(double degree) {
	makeTurnInternal(degree);
	moveForward();
    }

    @Override
    public void moveForward() {
	position = grid.moveForward(position);
	trackPosition(position);
	handler.handlePostConditions(grid, this);
    }

    @Override
    public void moveForward(int amount) {
	moveForwardOrBackwardInternal(amount, () -> moveForward());
    }

    @Override
    public void moveBackward() {
	position = grid.moveBackward(position);
	trackPosition(position);
	handler.handlePostConditions(grid, this);
    }

    @Override
    public void moveBackward(int amount) {
	moveForwardOrBackwardInternal(amount, () -> moveBackward());
    }

    private void moveForwardOrBackwardInternal(int amount, Runnable runnable) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    runnable.run();
	}
    }

    @Override
    public List<Position> getPositionHistory() {
	return positionHistory;
    }

    @Override
    public void turnLeft() {
	makeTurn(90);
    }

    @Override
    public void makeTurn(double degree) {
	makeTurnInternal(degree);
	if (degree != 0) {
	    handler.handlePostConditions(grid, this);
	}
    }

    private void makeTurnInternal(double degree) {
	if (degree != 0) {
	    position.rotate(degree);
	    trackPosition(position);
	}
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

    private void trackPosition(Position position) {
	positionHistory.add(position);
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
	    Objects.requireNonNull(grid, "Attribute 'grid' must not be null!");
	    Objects.requireNonNull(position, "Attribute 'position' must not be null!");
	    moveable = new SimpleMoveable(grid, position);
	}

	public MoveableBuilder withHandler(MoveablePostActionHandler handler) {
	    ((AbstractMoveable) moveable).handler = Objects.requireNonNull(handler,
		    "A Moveable always needs a MoveablePostActionHandler!");
	    return this;
	}

	public Moveable build() {
	    MoveablePostActionHandler handler = ((AbstractMoveable) moveable).handler;
	    Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
	    handler.handlePostConditions(((AbstractMoveable) moveable).grid, moveable);
	    return this.moveable;
	}

	private class SimpleMoveable extends AbstractMoveable {

	    private SimpleMoveable(Grid grid, Position position) {
		super(grid, position);
	    }
	}
    }
}