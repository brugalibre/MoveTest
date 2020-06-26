package com.myownb3.piranha.core.weapon.turret;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;

/**
 * The interface {@link Turret} represents a automated turret. A {@link Turret} consist always of three elements:
 * <ul>
 * <li>a {@link GunCarriage} which is responsible for turning the {@link Gun} around</li>
 * <li>a {@link Gun} which does the actual shooting</li>
 * </ul>
 * 
 * @author Dominic
 *
 */
public interface Turret extends AutoDetectable/*TODO die muss weg! RoboticTurret*/, Belligerent {

   /**
    * @return the {@link Shape} of this {@link Turret}
    */
   Shape getShape();

   /**
    * @return <code>true</code> if this {@link Turret} is currently acquiring a target or <code>false</code> if not
    */
   boolean isAcquiring();

   /**
    * @return <code>true</code> if this {@link Turret} is currently shooting or <code>false</code> if not
    */
   boolean isShooting();
}
