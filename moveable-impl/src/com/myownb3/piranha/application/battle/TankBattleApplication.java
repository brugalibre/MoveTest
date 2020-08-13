package com.myownb3.piranha.application.battle;

import java.util.List;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;

/**
 * The {@link BattleApplication} defines a runnable application for battles using {@link Tank}s {@link Turret}s and so on
 * 
 * @author DStalder
 *
 */
public interface TankBattleApplication extends Application {

   /**
    * @return all {@link TankGridElement} of this {@link TankBattleApplication}
    */
   List<TankGridElement> getTankGridElements();

   /**
    * @return all {@link TurretGridElement} of this {@link TankBattleApplication}
    */
   List<TurretGridElement> getTurretGridElements();
}
