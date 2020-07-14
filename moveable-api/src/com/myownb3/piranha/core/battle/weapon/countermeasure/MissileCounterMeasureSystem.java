package com.myownb3.piranha.core.battle.weapon.countermeasure;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;

/**
 * The {@link MissileCounterMeasureSystem} is responsible for initiate appropriate counter measures when {@link Projectile} like
 * {@link ProjectileTypes#MISSILE} are detected
 * 
 * @author Dominic
 *
 */
public interface MissileCounterMeasureSystem extends AutoDetectable {
   // no-op
}
