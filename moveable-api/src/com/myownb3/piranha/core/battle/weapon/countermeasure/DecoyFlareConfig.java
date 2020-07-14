package com.myownb3.piranha.core.battle.weapon.countermeasure;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;

/**
 * Defines the configuration of a decoy flare
 * 
 * @author Dominic
 *
 */
public interface DecoyFlareConfig extends ProjectileConfig {

   /**
    * @return the {@link BelligerentParty} of the {@link DecoyFlareConfig}
    */
   BelligerentParty getBelligerentParty();

   /**
    * @return the amoung of decoy flares which are deployed
    */
   int getAmountDecoyFlares();

   /**
    * @return the angle the decoy flares spread when deploying
    * @return
    */
   double getDecoyFlareSpreadAngle();

   /**
    * Returns the TTL for a decoy flare considering the given offest. Which means, that the actual TTL is within the range
    * {@link DecoyFlareConfig#getDecoyFlareTimeToLife()} until {@link DecoyFlareConfig#getDecoyFlareTimeToLife()} + <code>offset</code>
    * 
    * @param offset
    *        the offset about which the actual TTL may differ
    * @return the TTL for a decoy flare
    */
   int getDecoyFlareTimeToLife();

   /**
    * @return the TTL for a decoy flare
    */
   int getDecoyFlareTimeToLife(int offset);
}
