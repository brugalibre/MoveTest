/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.detector;

import java.util.Optional;

import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;

/**
 * 
 * The {@link MoveablePainterConfig} contain the configuration for the {@link MoveablePainter}
 * 
 * @author Dominic
 *
 */
public class DetectorPainterConfig {

   private DetectorConfig detectorConfig;
   private Optional<TrippleDetectorCluster> detectorCluster;

   private DetectorPainterConfig(Optional<TrippleDetectorCluster> detectorCluster, DetectorConfig detectorConfig) {

      this.detectorConfig = detectorConfig;
      this.detectorCluster = detectorCluster;
   }

   public static DetectorPainterConfig of(Optional<TrippleDetectorCluster> detectorCluster, DetectorConfig detectorConfig) {
      return new DetectorPainterConfig(detectorCluster, detectorConfig);
   }

   /**
    * 
    * @return the {@link TrippleDetectorCluster} of this {@link MoveablePainterConfig}
    */
   public final Optional<TrippleDetectorCluster> getDetectorCluster() {
      return detectorCluster;
   }

   public DetectorConfig getDetectorConfig() {
      return detectorConfig;
   }
}
