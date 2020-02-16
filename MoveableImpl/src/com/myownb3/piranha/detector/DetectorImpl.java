/**
 * 
 */
package com.myownb3.piranha.detector;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
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
    public void detectObject(GridElement gridElement, Position position) {

	boolean isDetected = false;

	Position gridElemPos = gridElement.getPosition();
	double distance = gridElemPos.calcDistanceTo(position);
	boolean isPotentialCollisionCourse = false;

	if (detectorReach >= distance) {
	    double degValue = MathUtil.calcAngleBetweenPositions(position, gridElemPos);
	    boolean isEvasion = false;
	    isDetected = degValue <= (detectorAngle / 2);
	    if (isDetected && evasionDistance >= distance) {
		isPotentialCollisionCourse = degValue <= (evasionAngle / 2);
		isEvasion = isDetected && isPotentialCollisionCourse;
	    }
	    detectionMap.put(gridElement, isDetected);
	    isEvasionMap.put(gridElement, isEvasion);
	    return;
	}
	detectionMap.remove(gridElement);
	isEvasionMap.remove(gridElement);
    }

    @Override
    public double getEvasionAngleRelative2(Position position) {

	double avoidAngle = 0;
	double ourAngle = position.getDirection().getAngle();

	Optional<GridElement> evasionGridElement = getNearestEvasionGridElement(position);
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

    private Optional<GridElement> getNearestEvasionGridElement(Position position) {
	List<GridElement> gridElements = getEvasionGridElements();
	return getNearestEvasionGridElement(position, gridElements);
    }

    @Visible4Testing
    Optional<GridElement> getNearestEvasionGridElement(Position position,
	    List<GridElement> gridElements) {
	Map<GridElement, Double> gridElement2DistanceMap = fillupMap(position, gridElements);
	return gridElement2DistanceMap.keySet()
		.stream()
		.sorted(sort4Distance(gridElement2DistanceMap))
		.findFirst();
    }

    private Map<GridElement, Double> fillupMap(Position position, List<GridElement> gridElements) {
	Map<GridElement, Double> gridElement2DistanceMap = new HashMap<>();
	for (GridElement gridElement : gridElements) {
	    Position gridElemPos = gridElement.getPosition();
	    double distance = gridElemPos.calcDistanceTo(position);
	    gridElement2DistanceMap.put(gridElement, Double.valueOf(distance));
	}
	return gridElement2DistanceMap;
    }

    private static Comparator<? super GridElement> sort4Distance(Map<GridElement, Double> gridElement2DistanceMap) {
	return (g1, g2) -> {
	    Double distanceGridElem1ToPoint = gridElement2DistanceMap.get(g1);
	    Double distanceGridElem2ToPoint = gridElement2DistanceMap.get(g2);
	    return distanceGridElem1ToPoint.compareTo(distanceGridElem2ToPoint);
	};
    }

    private List<GridElement> getEvasionGridElements() {
	return isEvasionMap.keySet()//
		.stream()//
		.filter(gridElement -> isEvasionMap.get(gridElement))//
		.collect(Collectors.toList());
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
