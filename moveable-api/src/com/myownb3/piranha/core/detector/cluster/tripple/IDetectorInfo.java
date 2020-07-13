package com.myownb3.piranha.core.detector.cluster.tripple;

import com.myownb3.piranha.core.detector.IDetector;

/**
 * The {@link IDetectorInfo} contains details about an {@link IDetector} and it's corresponding offset within a
 * {@link TrippleDetectorCluster}
 * 
 * @author Dominic
 *
 */
public interface IDetectorInfo {

   IDetector getDetector();

   double getOffsetAngle();
}
