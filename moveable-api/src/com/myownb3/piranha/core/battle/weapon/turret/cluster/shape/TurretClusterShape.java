package com.myownb3.piranha.core.battle.weapon.turret.cluster.shape;

import java.util.List;

import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretCluster;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * Defines the {@link Shape} of a {@link TurretCluster}
 * 
 * @author DStalder
 *
 */
public interface TurretClusterShape extends Shape {

   /**
    * @return the {@link Shape}s for all the {@link Turret}s of this {@link TurretClusterShape}
    */
   List<Shape> getTurretShapes();
}
