package com.myownb3.piranha.core.battle.weapon.tank;

import static java.util.Objects.nonNull;

import com.myownb3.piranha.core.grid.position.Position;

public class TankHolder {
   private Tank tank;
   private TankGridElement tankGridElement;

   /**
    * Set and return the given {@link Tank}
    * 
    * @param tank
    *        the tank
    * @return the very same {@link Tank}
    */
   public Tank setAndReturnTank(Tank tank) {
      this.tank = tank;
      return tank;
   }

   /**
    * @return the Position of the {@link Tank} of this {@link TankHolder}
    */
   public Position getPosition() {
      return nonNull(tank) ? tank.getPosition() : null;
   }

   public TankGridElement getTankGridElement() {
      return tankGridElement;
   }

   public void setTankGridElement(TankGridElement tankGridElement) {
      this.tankGridElement = tankGridElement;
   }
}
