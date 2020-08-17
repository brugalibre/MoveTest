package com.myownb3.piranha.core.detector.cluster.tripple;

import com.myownb3.piranha.core.detector.IDetector;

/**
 * The {@link IDetectorInfo} contains details about an {@link IDetector} and it's corresponding offset within a
 * {@link TrippleDetectorCluster}
 * 
 * An offset of 0 degrees means that the {@link IDetector} is placed at the center where as a offset from +90 degrees means, that the
 * {@link IDetector} is looking into the left direction
 * 
 * @author Dominic
 *
 */
public interface IDetectorInfo {

   /**
    * @return the {@link IDetector} itself
    */
   IDetector getDetector();

   /**
    * @return the offset angle
    */
   double getOffsetAngle();
}
