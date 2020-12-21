package com.myownb3.piranha.core.battle.weapon.tank;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;

/**
 * The {@link Tank} interface represents a tank which usually has a {@link Turret} on top
 * 
 * @author Dominic
 *
 */
public interface Tank extends AutoDetectable, Belligerent, Destructible, CollisionSensitive {

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
    * @return the {@link MoveableEngine} of this {@link Tank}
    */
   MoveableEngine getMoveableEngine();
}
