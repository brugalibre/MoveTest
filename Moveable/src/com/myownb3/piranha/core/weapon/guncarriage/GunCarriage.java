package com.myownb3.piranha.core.weapon.guncarriage;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The {@link GunCarriage} contains a {@link Gun} and is responsible for turning this {@link Gun} around
 * 
 * @author Dominic
 *
 */
public interface GunCarriage {

   /**
    * Fires one salve of the {@link Gun} of this {@link GunCarriage}- depending on the actual {@link Gun}-implementation
    */
   void fire();

   /**
    * This will lead this {@link GunCarriage} align it's orientation according the given target-Position
    * 
    * @param targetPos
    *        the {@link Position} this {@link GunCarriage} has to aim
    */
   void aimTargetPos(Position targetPos);

   /**
    * This will lead this {@link GunCarriage} to turn in order to reach it's parking {@link Position}
    * 
    * @param targetPos
    *        the {@link Position} this {@link GunCarriage} has to aim
    */
   void turn2ParkPosition();

   /**
    * Evaluates and sets the {@link Position} for this {@link GunCarriage} regarding it's dimension
    * This also evaluates and sets the {@link Position} for any mounted {@link Gun} of this {@link GunCarriage}
    * 
    * @param position
    *        the new {@link Position} e.g. from moved {@link Turret}
    */
   void evalAndSetPosition(Position position);

   /**
    * @return <code>true</code> if this {@link GunCarriage} is in it's parking {@link Position} of <code>false</code> if not
    * 
    */
   boolean isInParkingPosition();

   /**
    * @param acquiredTargetPos
    *        the target {@link Position}
    * @return <code>true</code> if this {@link GunCarriage} has acquired the given {@link Position} or <code>false</code> if not
    */
   boolean hasTargetLocked(Position acquiredTargetPos);

   /**
    * @return the {@link Shape} of this {@link GunCarriage}
    */
   Shape getShape();

   /**
    * @return the {@link Gun} of this {@link GunCarriage}
    */
   Gun getGun();

   /**
    * @return the {@link Position} of this {@link GunCarriage}
    */
   Position getPosition();
}
