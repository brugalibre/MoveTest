package com.myownb3.piranha.application.battle;

import java.util.List;

import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link BattleApplication} defines a runnable application for battles using {@link Tank}s {@link Turret}s and so on
 * 
 * @author DStalder
 *
 */
public interface TankBattleApplication {

   /**
    * Starts this {@link TankBattleApplication} Application
    */
   public void run();

   /**
    * @return all the {@link GridElement} which take part in this {@link BattleApplication}
    */
   List<GridElement> getAllGridElements();

   /**
    * @return all {@link TankGridElement} of this {@link TankBattleApplication}
    */
   List<TankGridElement> getTankGridElements();

   /**
    * @return all {@link TurretGridElement} of this {@link TankBattleApplication}
    */
   List<TurretGridElement> getTurretGridElements();

}
