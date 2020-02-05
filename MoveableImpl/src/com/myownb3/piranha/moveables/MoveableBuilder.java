/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Objects;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;

/**
 * The {@link MoveableBuilder} is a tool to build a {@link SimpleMoveable}
 * 
 * @author Dominic
 *
 */
public class MoveableBuilder {

    private AbstractMoveable moveable;
    private MoveablePostActionHandler handler;
    private Position position;
    private Grid grid;
    private Position endPos;
    private int movingIncrement;

    public static MoveableBuilder builder() {
	return new MoveableBuilder(new DefaultGrid(), Positions.of(0, 0));
    }

    public MoveableBuilder(Grid grid) {
	this(grid, Positions.of(0, 0));
    }

    public MoveableBuilder(Grid grid, Position position) {
	this.grid = Objects.requireNonNull(grid, "Attribute 'grid' must not be null!");
	this.position = Objects.requireNonNull(position, "Attribute 'position' must not be null!");
	movingIncrement = 1;
    }

    public MoveableBuilder withHandler(MoveablePostActionHandler handler) {
	this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
	return this;
    }

    public Moveable build() {
	moveable = new SimpleMoveable(grid, position, handler);
	handler.handlePostConditions(moveable.getGrid(), moveable);
	return this.moveable;
    }
    
    public MoveableBuilder withMovingIncrement(int movingIncrement) {
	this.movingIncrement = movingIncrement;
	return this;
    }
    
    public EndPointMoveable buildEndPointMoveable() {
	Objects.requireNonNull(endPos, "Attribute 'endPos' must not be null!");
	moveable = new EndPointMoveableImpl(grid, position, handler, endPos, movingIncrement);
	handler.handlePostConditions(moveable.getGrid(), moveable);
	return (EndPointMoveable) this.moveable;
    }

    private class SimpleMoveable extends AbstractMoveable {
	private SimpleMoveable(Grid grid, Position position, MoveablePostActionHandler handler) {
	    super(grid, position, handler);
	}
    }

    public MoveableBuilder widthEndPosition(Position endPos) {
	this.endPos = endPos;
	return this;
    }
}