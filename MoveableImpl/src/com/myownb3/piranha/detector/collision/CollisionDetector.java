/**
 * 
 */
package com.myownb3.piranha.detector.collision;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * The {@link CollisionDetector} helps to detect collisions on the {@link Grid}
 * 
 * @author Dominic
 *
 */
public class CollisionDetector {

    private Optional<CollisionDetectionHandler> collisionDetectionHandlerOpt;
    private int collisionDistance;

    private CollisionDetector(CollisionDetectionHandler collisionDetectionHandler, int collisionDistance) {
	super();
	collisionDetectionHandlerOpt = Optional.of(collisionDetectionHandler);
	this.collisionDistance = collisionDistance;
    }

    /**
     * Checks for every given {@link Avoidable} if there is a collision when moving
     * from the old to the new Position
     * 
     * @param oldPosition   the Position before the movement
     * @param newPosition   the new Position after the movement
     * @param allAvoidables all {@link Avoidable} on the Grid
     */
    public void checkCollision(Position oldPosition, Position newPosition, List<Avoidable> allAvoidables) {
	Float64Vector lineFromOldToNew = VectorUtil.getVector(oldPosition.getDirection());
	allAvoidables.forEach(checkCollisionWithAvoidable(oldPosition, newPosition, lineFromOldToNew));
    }

    private Consumer<? super Avoidable> checkCollisionWithAvoidable(Position oldPosition, Position newPosition,
	    Float64Vector lineFromOldToNew) {
	return avoidable -> {
	    boolean isCollision = isCollision(oldPosition, newPosition, lineFromOldToNew, avoidable);
	    if (isCollision) {
		collisionDetectionHandlerOpt.ifPresent(
			collisionDetectionHandler -> collisionDetectionHandler.handleCollision(avoidable, newPosition));
	    }
	};
    }

    private boolean isCollision(Position oldPosition, Position newPosition, Float64Vector lineFromOldToNew,
	    Avoidable avoidable) {
	if (hasSameCoordinates(newPosition, avoidable.getPosition())) {
	    return true;
	}
	boolean isPositionOnLine = isPositionOnLine(oldPosition, lineFromOldToNew, avoidable);
	boolean isCloseEnoughToAvoidable = isCloseEnoughToAvoidable(newPosition, avoidable);
	return isPositionOnLine && isCloseEnoughToAvoidable;
    }

    private boolean hasSameCoordinates(Position newPosition, Position avoidablePosition) {
	return newPosition.getX() == avoidablePosition.getX() && newPosition.getY() == avoidablePosition.getY();
    }

    private boolean isCloseEnoughToAvoidable(Position newPosition, Avoidable avoidable) {
	return newPosition.calcDistanceTo(avoidable.getPosition()) <= collisionDistance;
    }

    private static boolean isPositionOnLine(Position oldPosition, Float64Vector lineFromOldToNew, Avoidable avoidable) {
	double avoidableDistanceToLine = round(
		calcDistanceFromPositionToLine(avoidable.getPosition(), oldPosition, lineFromOldToNew), 10);
	return avoidableDistanceToLine == 0.0d;
    }

    public static class CollisionDetectorBuilder {

	private CollisionDetectionHandler handler;
	private int collisionDistance;

	private CollisionDetectorBuilder() {
	    super();
	    collisionDistance = 2;
	}

	public static CollisionDetectorBuilder builder() {
	    return new CollisionDetectorBuilder();
	}

	public CollisionDetectorBuilder withDefaultCollisionHandler() {
	    handler = (avoidable, newPosition) -> {
		throw new CollisionDetectedException("Collision with Avoidable '" + avoidable.getPosition()
			+ "', on Position x='" + newPosition.getX() + "', y='" + newPosition.getY() + "'");
	    };
	    return this;
	}

	public CollisionDetectorBuilder withCollisionHandler(CollisionDetectionHandler collisionDetectionHandler) {
	    handler = collisionDetectionHandler;
	    return this;
	}

	public CollisionDetectorBuilder withCollisionDistance(int collisionDistance) {
	    this.collisionDistance = collisionDistance;
	    return this;
	}

	public CollisionDetector build() {
	    return new CollisionDetector(handler, collisionDistance);
	}
    }
}