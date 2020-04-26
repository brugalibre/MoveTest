package com.myownb3.piranha.detector.strategy;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl;
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
}
