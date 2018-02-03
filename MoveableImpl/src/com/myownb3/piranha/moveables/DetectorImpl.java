/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.HashMap;
import java.util.Map;

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

    private Map<GridElement, Boolean> detectionMap;
    private Map<GridElement, Boolean> isAvoidingMap;

    public DetectorImpl() {
	this(8, 45);
    }

    public DetectorImpl(int detectorReach, int detectorAngle) {
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.avoidingDistance = 2 * detectorReach / 3;
	detectionMap = new HashMap<>();
	isAvoidingMap = new HashMap<>();
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
	isAvoidingMap.put(gridElement, hasObjectDetected(gridElement) && avoidingDistance >= distance);
    }

    @Override
    public final boolean isAvoiding(GridElement gridElement) {
	Boolean isAvoiding = isAvoidingMap.get(gridElement);
	return isAvoiding == null ? false : isAvoiding;
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement) {
	Boolean hasObjectDetected = detectionMap.get(gridElement);
	return hasObjectDetected == null ? false : hasObjectDetected;
    }
}
