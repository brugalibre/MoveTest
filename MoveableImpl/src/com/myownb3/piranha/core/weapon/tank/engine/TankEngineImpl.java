package com.myownb3.piranha.core.weapon.tank.engine;

import com.myownb3.piranha.core.moveables.EndPointMoveable;

public class TankEngineImpl implements TankEngine {

   private EndPointMoveable endPointMoveable;

   private TankEngineImpl(EndPointMoveable endPointMoveable) {
      this.endPointMoveable = endPointMoveable;
   }

}
