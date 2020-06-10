package com.myownb3.piranha.core.weapon.tank;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The {@link Tank} interface represents a tank which usually has a {@link Turret} on top
 * 
 * @author Dominic
 *
 */
public interface Tank extends AutoDetectable, Belligerent {

   /**
    * @return the Shape of this {@link Tank}
    */
   TankShape getShape();

   /**
    * @return the {@link Turret} of this {@link Tank}
    */
   Turret getTurret();

   /**
    * @return the {@link Position} of this {@link Tank}
    */
   Position getPosition();

   /**
    * @return the {@link TankEngine} of this {@link Tank}
    */
   TankEngine getTankEngine();
}
