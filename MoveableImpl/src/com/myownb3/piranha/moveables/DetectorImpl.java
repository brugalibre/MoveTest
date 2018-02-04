/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class DetectorImpl implements Detector {

    private int detectorReach;
    private int avoidingDistance;
    private int detectorAngle;
    private double angleInc;

    private Map<GridElement, Boolean> detectionMap;
    private Map<GridElement, Boolean> isEvasionMap;

    public DetectorImpl() {
	this(8, 45, 11.25);
    }

    public DetectorImpl(int detectorReach, int detectorAngle, double angleInc) {
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.avoidingDistance = 2 * detectorReach / 3;
	this.angleInc = angleInc;
	detectionMap = new HashMap<>();
	isEvasionMap = new HashMap<>();
    }

    @Override
    public void detectObject(GridElement gridElement, Position position) {

	boolean isDetected = false;

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);

	if (distance <= detectorReach) {

	    double gridElementAngle = gridElemPos.calcAbsolutAngle();
	    double ourAngle = position.getDirection().getAngle();

	    isDetected = isWithinUpperBorder(ourAngle, gridElementAngle)
		    && gridElementAngle >= ourAngle - (detectorAngle / 2);
	}
	detectionMap.put(gridElement, isDetected);
	isEvasionMap.put(gridElement, isDetected && avoidingDistance >= distance);
    }

    @Override
    public double getEvasionAngleRelative2(Position position) {

	double avoidAngle = 0;
	double ourAngle = position.getDirection().getAngle();

	for (GridElement gridElement : getEvasionGridElements()) {

	    Position gridElemPos = gridElement.getPosition();
	    double gridElementAngle = gridElemPos.calcAbsolutAngle();

	    boolean isInUppoerBounds = isWithinUpperBorder(ourAngle, gridElementAngle) && gridElementAngle >= ourAngle;

	    if (isInUppoerBounds /* && isInLowerBounds, might be true here as well */) {
		avoidAngle = -angleInc;// Turn to the right
	    } else {
		avoidAngle = angleInc;// Turn to the left
		assert (!isInUppoerBounds);
	    }
	    break;// no support for more than one GridElement to avoid
	}
	return avoidAngle;
    }

    @Override
    public final boolean isEvasion(GridElement gridElement) {
	Boolean isEvasion = isEvasionMap.get(gridElement);
	return isEvasion == null ? false : isEvasion;
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement) {
	Boolean hasObjectDetected = detectionMap.get(gridElement);
	return hasObjectDetected == null ? false : hasObjectDetected;
    }

    private List<GridElement> getEvasionGridElements() {
	return isEvasionMap.keySet()//
		.stream()//
		.filter(gridElement -> isEvasionMap.get(gridElement))//
		.collect(Collectors.toList());
    }

    private boolean isWithinUpperBorder(double ourAngle, double gridElementAngle) {
	return ourAngle + (detectorAngle / 2) >= gridElementAngle;
    }
}
