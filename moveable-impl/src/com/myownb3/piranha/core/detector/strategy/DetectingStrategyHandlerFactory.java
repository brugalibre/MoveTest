package com.myownb3.piranha.core.detector.strategy;

import com.myownb3.piranha.core.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.core.detector.cluster.tripple.StaticSupportiveFlanksDetectingStrategyHandler;
import com.myownb3.piranha.core.detector.cluster.tripple.SupportiveFlanksDetectingStrategyHandler;

/**
 * The {@link DetectingStrategyHandlerFactory} is responsible for creating a {@link DetectingStrategyHandler} for each
 * {@link DetectingStrategy}
 * 
 * @author Dominic
 *
 */
public class DetectingStrategyHandlerFactory {

   private DetectingStrategyHandlerFactory() {
      // private
   }

   /**
    * Creates a new {@link DetectingStrategyHandler} for the given {@link DetectingStrategy}
    * 
    * @param detectingStrategy
    *        the {@link DetectingStrategy}
    * @return a new {@link DetectingStrategyHandler} for the given {@link DetectingStrategy}
    */
   @SuppressWarnings("unchecked")
   public static <T extends DetectingStrategyHandler> T getHandler(DetectingStrategy detectingStrategy) {
      switch (detectingStrategy) {
         case SUPPORTIVE_FLANKS:
            return (T) new SupportiveFlanksDetectingStrategyHandler();
         case SUPPORTIVE_FLANKS_WITH_DETECTION:
            return (T) new StaticSupportiveFlanksDetectingStrategyHandler();
         default:
            throw new IllegalArgumentException("Unknown Strategy '" + detectingStrategy + "'");
      }
   }
}
