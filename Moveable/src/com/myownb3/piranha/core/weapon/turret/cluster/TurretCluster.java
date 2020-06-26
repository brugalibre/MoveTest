package com.myownb3.piranha.core.weapon.turret.cluster;

import java.util.List;

import com.myownb3.piranha.core.weapon.turret.Turret;

public interface TurretCluster extends Turret {

   /**
    * @return the {@link Turret}s of this {@link TurretCluster}
    */
   List<Turret> getTurrets();

}
