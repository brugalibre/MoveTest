package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static com.myownb3.piranha.core.battle.weapon.gun.projectile.descent.DescentAutoDetectable.getDescentAutoDetectable;
import static com.myownb3.piranha.core.battle.weapon.gun.projectile.timetolife.TimeToLifeAutoDetectable.getTimeToLifeAutoDetectable;

import com.myownb3.piranha.core.battle.destruction.Health;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * Contains the logic which is executed for a decoy flare each cycle
 * 
 * @author DStalder
 *
 */
public class DecoyFlareAutoDetectable {

   private DecoyFlareAutoDetectable() {
      // private
   }

   /**
    * Creates the {@link AutoDetectable} which is executed for a decoy. This includes a descent maneuver as well as a the logic which
    * destroys a decoy flare after a
    * specific amount of time
    * 
    * @param decoyShape
    *        the shape of a decoy flare
    * @param health
    *        {@link Health} of our decoy flare
    * @param targetHeight
    *        the final height of the decoy flare
    * @param decoyTimeToLife
    *        the TTL of the decoy flare
    * @return a {@link AutoDetectable} which is executed for a decoy
    */
   public static AutoDetectable getDecoyFlareAutoDetectable(Shape decoyShape, Health health, double targetHeight, int decoyTimeToLife) {
      AutoDetectable descentAutoDetectable = getDescentAutoDetectable(decoyShape, 15 * decoyShape.getDimensionRadius(), targetHeight);
      AutoDetectable timeToLifeAutoDetectable = getTimeToLifeAutoDetectable(health, decoyTimeToLife);
      return () -> {
         descentAutoDetectable.autodetect();
         timeToLifeAutoDetectable.autodetect();
      };
   }

}
