package com.myownb3.piranha.ui.application.evasionstatemachine.config;

import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;

public class DefaultConfig {

   private DefaultConfig() {
      // private
   }

   public static DefaultConfig INSTANCE = new DefaultConfig();
   private static final EvasionStateMachineConfig defaultConfig;

   static {
      defaultConfig = new EvasionStateMachineConfigImpl(1, 10, 0.06, 0.7d, 30, 25, 180, 170, 2);
   }

   public EvasionStateMachineConfig getDefaultEvasionStateMachineConfig() {
      return defaultConfig;
   }
}
