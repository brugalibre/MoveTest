package com.myownb3.piranha.ui.application.evasionstatemachine.config;

import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;

public class DefaultConfig {

   private DefaultConfig() {
      // private
   }

   public static final DefaultConfig INSTANCE = new DefaultConfig();
   private static final EvasionStateMachineConfig defaultConfig;

   static {
      int detectorReach = 30;
      defaultConfig = EvasionStateMachineConfigBuilder.builder()
            .withReturningAngleIncMultiplier(1)
            .withOrientationAngle(10)
            .withReturningMinDistance(0.06)
            .withReturningAngleMargin(0.7d)
            .withPassingDistance(25)
            .withPostEvasionReturnAngle(4)
            .withDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(detectorReach)
                  .withEvasionDistance(2 * detectorReach / 3)
                  .withDetectorAngle(180)
                  .withEvasionAngle(170)
                  .withEvasionAngleInc(2).build())
            .build();
   }

   public EvasionStateMachineConfig getDefaultEvasionStateMachineConfig() {
      return defaultConfig;
   }
}
