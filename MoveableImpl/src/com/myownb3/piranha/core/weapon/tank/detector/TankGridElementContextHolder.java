package com.myownb3.piranha.core.weapon.tank.detector;

import com.myownb3.piranha.core.weapon.tank.TankGridElement;

@FunctionalInterface
public interface TankGridElementContextHolder {

   /**
    * @return the {@link TankGridElement}
    */
   TankGridElement getTankGridElement();
}
