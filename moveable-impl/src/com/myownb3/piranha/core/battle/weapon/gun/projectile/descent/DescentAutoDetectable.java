package com.myownb3.piranha.core.battle.weapon.gun.projectile.descent;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class DescentAutoDetectable {

   private DescentAutoDetectable() {
      // private
   }

   /**
    * Creates a A {@link AutoDetectable} {@link DescentHandler} which is responsible for the descent maneuver as a {@link AutoDetectable}
    * 
    * @param shape
    *        the {@link Shape} which is involved in the descent maneuver
    * @param distanceBeforeDescent
    *        the distance before descent
    * @param targetHeight
    *        the target height
    * @return an {@link AutoDetectable}
    */
   public static AutoDetectable getDescentAutoDetectable(Shape shape, double distanceBeforeDescent, double targetHeight) {
      DescentHandler descentHandler = new DescentHandler(shape.getCenter(), distanceBeforeDescent, targetHeight);
      return () -> {
         Position position = descentHandler.evlPositionForNewHeight(shape.getCenter());
         if (!position.equals(shape.getCenter())) {
            shape.transform(position);
         }
      };
   }
}
