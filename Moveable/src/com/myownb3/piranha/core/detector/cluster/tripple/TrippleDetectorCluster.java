package com.myownb3.piranha.core.detector.cluster.tripple;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.init.Initializable;

/**
 * The {@link TrippleDetectorCluster} contains three {@link IDetector}. One is located in the center and two are placed to it's left and
 * right side. Together they cover a 180 degree angle whereas the detector located in the center has the greater reach than the ones located
 * on it's flank
 * 
 * @author Dominic
 *
 */
public interface TrippleDetectorCluster extends Detector, Initializable {

   /**
    * 
    * @return the {@link IDetector} which is placed in the center
    */
   IDetectorInfo getCenterDetector();

   /**
    * 
    * @return the {@link IDetector} which is placed on the left hand side
    */
   IDetectorInfo getLeftSideDetector();

   /**
    * 
    * @return the {@link IDetector} which is placed on the right hand side
    */
   IDetectorInfo getRightSideDetector();

}
