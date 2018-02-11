/**
 * 
 */
package com.myownb3.piranha.moveables.detector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class DetectorImpl implements Detector {

    private int detectorReach;
    private double detectorAngle;
    private double angleInc;
    private int evasionDistance;
    private double evasionAngle;

    private Map<GridElement, Boolean> detectionMap;
    private Map<GridElement, Boolean> isEvasionMap;

    /**
     * Default Constructor, only used for Tests
     */
    public DetectorImpl() {
	this(8, 45, 11.25);
    }

    public DetectorImpl(int detectorReach, int detectorAngle, int evasionAngle, double angleInc) {
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.evasionAngle = evasionAngle;
	this.evasionDistance = 2 * detectorReach / 3;
	this.angleInc = angleInc;
	detectionMap = new HashMap<>();
	isEvasionMap = new HashMap<>();
    }

    public DetectorImpl(int detectorReach, int detectorAngle, double angleInc) {
	this(detectorReach, detectorAngle, detectorAngle, angleInc);
    }

    @Override
    public void detectObject(GridElement gridElement, Position position) {

	boolean isDetected = false;

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);
	boolean isPotentialCollisionCourse = false;

	if (detectorReach >= distance) {

	    double gridElementAngle = gridElemPos.calcAbsolutAngle();
	    double ourAngle = position.getDirection().getAngle();

	    isDetected = isDetected(gridElementAngle, ourAngle, detectorAngle);
	    isPotentialCollisionCourse = isDetected && isPotentialCollisionCourse(distance, gridElementAngle, ourAngle);
	}
	detectionMap.put(gridElement, isDetected);
	isEvasionMap.put(gridElement, isDetected && isPotentialCollisionCourse);
    }

    private boolean isDetected(double gridElementAngle, double ourAngle, double detectorAngle) {
	return isWithinUpperBorder(ourAngle, gridElementAngle, detectorAngle)
		&& gridElementAngle >= ourAngle - (detectorAngle / 2);
    }

    private boolean isPotentialCollisionCourse(double distance, double gridElementAngle, double ourAngle) {
	return evasionDistance >= distance && isDetected(gridElementAngle, ourAngle, evasionAngle);
    }

    @Override
    public double getEvasionAngleRelative2(Position position) {

	double avoidAngle = 0;
	double ourAngle = position.getDirection().getAngle();

	Optional<GridElement> evasionGridElement = getEvasionGridElement();
	if (evasionGridElement.isPresent()) {
	    GridElement gridElement = evasionGridElement.get();
	    Position gridElemPos = gridElement.getPosition();
	    double gridElementAngle = gridElemPos.calcAbsolutAngle();

	    boolean isInUpperBounds = isWithinUpperBorder(ourAngle, gridElementAngle, detectorAngle)
		    && gridElementAngle >= ourAngle;

	    if (isInUpperBounds /* && isInLowerBounds, might be true here as well */) {
		avoidAngle = -angleInc;// Turn to the right
	    } else {
		avoidAngle = angleInc;// Turn to the left
		assert (!isInUpperBounds);
	    }
	}
	return avoidAngle;
    }

    public Optional<GridElement> getEvasionGridElement() {
	return isEvasionMap.keySet()//
		.stream()//
		.filter(gridElement -> isEvasionMap.get(gridElement))//
		.findFirst();
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

    @Override
    public double getAngleInc() {
	return angleInc;
    }

    private boolean isWithinUpperBorder(double ourAngle, double gridElementAngle, double detectorAngle) {
	return ourAngle + (detectorAngle / 2) >= gridElementAngle;
    }
}
