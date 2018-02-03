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
    private Map<GridElement, Boolean> isEvasioningMap;

    public DetectorImpl() {
	this(8, 45, 11.25);
    }

    public DetectorImpl(int detectorReach, int detectorAngle, double angleInc) {
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.avoidingDistance = 2 * detectorReach / 3;
	detectionMap = new HashMap<>();
	isEvasioningMap = new HashMap<>();
	this.angleInc = angleInc;
    }

    @Override
    public void detectObject(GridElement gridElement, Position position) {

	boolean isDetected = false;

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);

	if (distance <= detectorReach) {

	    double gridElementAngle = gridElemPos.calcAbsolutAngle();
	    double ourAngle = position.getDirection().getAngle();

	    isDetected = ourAngle + (detectorAngle / 2) >= gridElementAngle
		    && gridElementAngle >= ourAngle - (detectorAngle / 2);
	}
	detectionMap.put(gridElement, isDetected);
	isEvasioningMap.put(gridElement, isDetected && avoidingDistance >= distance);
    }

    @Override
    public double getEvasioningAngleRelative2(Position position) {

	double avoidAngle = 0;
	double ourAngle = position.getDirection().getAngle();

	for (GridElement gridElement : getEvasioningGridElements()) {

	    Position gridElemPos = gridElement.getPosition();
	    double gridElementAngle = gridElemPos.calcAbsolutAngle();

	    boolean isInUppoerBounds = (ourAngle + detectorAngle / 2) >= gridElementAngle
		    && gridElementAngle >= ourAngle;

	    if (isInUppoerBounds /* && isInLowerBounds, might be true here as well */) {
		avoidAngle = -angleInc;// Turn to the right
	    } else {
		avoidAngle = angleInc;// Turn to the left
		assert (!isInUppoerBounds);
	    }
	}
	return avoidAngle;
    }

    @Override
    public final boolean isEvasioning(GridElement gridElement) {
	Boolean isEvasioning = isEvasioningMap.get(gridElement);
	return isEvasioning == null ? false : isEvasioning;
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement) {
	Boolean hasObjectDetected = detectionMap.get(gridElement);
	return hasObjectDetected == null ? false : hasObjectDetected;
    }

    private List<GridElement> getEvasioningGridElements() {
	return isEvasioningMap.keySet()//
		.stream()//
		.filter(gridElement -> isEvasioningMap.get(gridElement))//
		.collect(Collectors.toList());
    }
}
