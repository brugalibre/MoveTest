/**
 * 
 */
package com.myownb3.piranha.util;

import static java.lang.Math.toDegrees;

import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.vector.Float64Vector;
import org.jscience.mathematics.vector.Vector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
public class MathUtil {

    public static double round(double value, int places) {

	if (places < 0 || places > 10) {
	    throw new IllegalArgumentException("The amount of decimal places must be between 0 and 10!");
	}
	double factor = calcFactor(places);
	return (double) Math.round(value * factor) / factor;
    }

    private static double calcFactor(int places) {
	double factor = 1;

	for (int i = 1; i <= places; i++) {
	    factor = factor * 10;
	}
	return factor;
    }

    public static double roundThreePlaces(double value) {
	return round(value, 3);
    }

    /**
     * Returns a random number considering the given offset
     * 
     * @param offset
     *            the offset
     * @return a random number
     */
    public static double getRandom(int offset) {
	return Math.random() * offset;
    }

    /**
     * Calculates the angle between the two Vectors which can be created between the given {@link Position}s.
     * The direection of the {@link Moveable}s position is considered.
     * 
     * @param moveablePosition the position from a {@link Moveable}
     * @param gridElementPos the position of a {@link GridElement} on a {@link Grid} 
     * @return the calculated angle with a precision of three decimal places
     */
    public static double calcAngleBetweenPositions(Position moveablePosition, Position gridElementPos) {
	Vector<Float64> moveable2GridElemVector = getVectorFromMoveable2GridElement(moveablePosition, gridElementPos);
	Vector<Float64> moveableDirectionVector = getMoveableDirectionVector(moveablePosition.getDirection());

	double moveableVectorTimesGridElemVector = moveableDirectionVector.times(moveable2GridElemVector).doubleValue();
	double moveable2GridElemVectorLenght = Math.sqrt(moveable2GridElemVector.times(moveable2GridElemVector).doubleValue());
	double moveableVectorLenght = Math.sqrt(moveableDirectionVector.times(moveableDirectionVector).doubleValue());

	return calcAngleBetweenVectors(moveableVectorTimesGridElemVector, moveable2GridElemVectorLenght, moveableVectorLenght);
    }

    private static double calcAngleBetweenVectors(double moveableVectorTimesGridElemVector, double moveable2GridElemVectorLenght, double moveableVectorLenght) {
	double radValue = Math.acos(moveableVectorTimesGridElemVector / (moveableVectorLenght * moveable2GridElemVectorLenght));
	return roundThreePlaces(toDegrees(radValue));
    }

    private static Vector<Float64> getMoveableDirectionVector(Direction moveableDirection) {
	return Float64Vector.valueOf(moveableDirection.getForwardX(), moveableDirection.getForwardY());
    }

    private static Vector<Float64> getVectorFromMoveable2GridElement(Position moveablePosition, Position gridElemPos) {
	Vector<Float64> moveableVector = Float64Vector.valueOf(moveablePosition.getX(), moveablePosition.getY());
	Vector<Float64> gridElemVector = Float64Vector.valueOf(gridElemPos.getX(), gridElemPos.getY());
	return gridElemVector.minus(moveableVector);
    }
}
