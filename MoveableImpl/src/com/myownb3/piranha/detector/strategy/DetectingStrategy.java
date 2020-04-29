package com.myownb3.piranha.detector.strategy;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;

/**
 * When using a {@link TrippleDetectorCluster} e.g. the {@link TrippleDetectorClusterImpl} we need a strategy in order to define which
 * {@link Detector} is used first
 * 
 * @author Dominic
 *
 */
public enum DetectingStrategy {

   /** Only for testing purpose * */
   NONE,

   /**
    * This strategy means, center detector is treated as the main {@link Detector} whereas the {@link Detector} on the flanks are
    * supportive e.g. they make sure a {@link Moveable} does not collide when making a turn
    */
   SUPPORTIVE_FLANKS,
   /**
    * This strategy behaves the same than SUPPORTIVE_FLANKS. Additionally when a turning left or right in order to avoid an evasion
    * with this strategy we make sure not to face another {@link GridElement} after turning
    */
   SUPPORTIVE_FLANKS_WITH_DETECTION,
}
