/**
 * 
 */
package com.myownb3.piranha.detector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.util.MathUtil;

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
    public int getEvasionDistance() {
        return evasionDistance;
    }
    
    @Override
    public void detectObject(GridElement gridElement, Position position) {

	boolean isDetected = false;

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);
	boolean isPotentialCollisionCourse = false;

	if (detectorReach >= distance) {

	    int degValue = MathUtil.calcAngleBetweenVectors(position, gridElemPos);
	    isDetected = degValue <= (detectorAngle / 2);
	    if (isDetected && evasionDistance >= distance) {
		isPotentialCollisionCourse = degValue <= (evasionAngle / 2);
	    }
	}
	detectionMap.put(gridElement, isDetected);
	isEvasionMap.put(gridElement, isDetected && isPotentialCollisionCourse);
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

	    if (isInUpperBounds) {
		avoidAngle = -angleInc;// Turn to the right
	    } else {
		avoidAngle = angleInc;// Turn to the left
		assert (!isInUpperBounds);
	    }
	}
	return avoidAngle;
    }

    private Optional<GridElement> getEvasionGridElement() {
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

    private boolean isWithinUpperBorder(double ourAngle, double gridElementAngle, double detectorAngle) {
	return ourAngle + (detectorAngle / 2) >= gridElementAngle;
    }
}
