/**
 * 
 */
package com.myownb3.piranha.moveables;

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

    public DetectorImpl() {
	this(8, 45);
    }

    public DetectorImpl(int detectorReach, int detectorAngle) {
	this.detectorReach = detectorReach;
	this.detectorAngle = detectorAngle;
	this.avoidingDistance = 2 * detectorReach / 3;
    }

    @Override
    public boolean hasObjectDetected(GridElement gridElement, Position position) {
	Position gridElemPos = gridElement.getPosition();

	double distance = gridElemPos.calcDistanceTo(position);
	if (distance > detectorReach) {
	    return false;
	}

	double gridElementAngle = gridElemPos.calcAbsolutAngle();
	double ourAngle = position.getDirection().getAngle();

	return ourAngle + (detectorAngle / 2) >= gridElementAngle && gridElementAngle >= ourAngle - (detectorAngle / 2);
    }

    @Override
    public boolean isAvoiding(GridElement gridElement, Position position) {

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);

	return avoidingDistance >= distance;
    }
}
