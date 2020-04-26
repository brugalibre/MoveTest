package com.myownb3.piranha.ui.application.evasionstatemachine.config;

import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigBuilder;

public class DefaultConfig {

   private DefaultConfig() {
      // private
   }

   public static DefaultConfig INSTANCE = new DefaultConfig();
   private static final EvasionStateMachineConfig defaultConfig;

   static {
      defaultConfig = EvasionStateMachineConfigBuilder.builder()
            .withReturningAngleIncMultiplier(1)
            .withOrientationAngle(10)
            .withReturningMinDistance(0.06)
            .withReturningAngleMargin(0.7d)
            .withDetectorReach(30)
            .withPassingDistance(25)
            .withDetectorAngle(180)
            .withEvasionAngle(170)
            .withEvasionAngleInc(2)
            .build();
   }

   public EvasionStateMachineConfig getDefaultEvasionStateMachineConfig() {
      return defaultConfig;
   }
}
