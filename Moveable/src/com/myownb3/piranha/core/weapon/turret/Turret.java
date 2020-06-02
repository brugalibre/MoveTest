package com.myownb3.piranha.core.weapon.turret;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
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
public interface Turret {

   /**
    * Makes this {@link Turret} to scan it's environment for possible targets. It automatically detects and acquires the nearest
    * {@link GridElement}.
    * As soon as a {@link GridElement} is acquired, the {@link Turret} begins firing
    * 
    */
   void autodetect();

   /**
    * @return the {@link GunCarriage} of this {@link Turret}
    */
   GunCarriage getGunCarriage();

   /**
    * By definition the foremost {@link Position} of a {@link Turret} is the foremost Position of it's {@link Gun}
    * 
    * @return returns the foremost Position of it's {@link GunCarriage}
    */
   Position getForemostPosition();

   /**
    * @return the {@link Position} of this {@link Turret}
    */
   Position getPosition();

   /**
    * @return the {@link Shape} of this {@link Turret}
    */
   Shape getShape();
}
