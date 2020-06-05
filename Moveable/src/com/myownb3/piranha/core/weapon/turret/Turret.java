package com.myownb3.piranha.core.weapon.turret;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;

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
public interface Turret extends AutoDetectable {

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
    * @return the {@link TurretShape} of this {@link Turret}
    */
   TurretShape getShape();
}
