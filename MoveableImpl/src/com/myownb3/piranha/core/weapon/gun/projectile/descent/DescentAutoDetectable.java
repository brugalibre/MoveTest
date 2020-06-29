package com.myownb3.piranha.core.weapon.gun.projectile.descent;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.AutoDetectable;

public class DescentAutoDetectable {

   private DescentAutoDetectable() {
      // private
   }

   /**
    * Creates a A {@link AutoDetectable} {@link DescentHandler} which is responsible for the descent maneuver as a {@link AutoDetectable}
    * 
    * @param shape
    *        the {@link Shape} which is involved in the descent maneuver
    * @param targetHeight
    *        the target height
    * @param distanceBeforeDescent
    *        the distance before descent
    * @return an {@link AutoDetectable}
    */
   public static AutoDetectable getDescentAutoDetectable(Shape shape, double targetHeight, double distanceBeforeDescent) {
      DescentHandler descentHandler = new DescentHandler(shape.getCenter(), distanceBeforeDescent, targetHeight);
      return () -> {
         Position position = descentHandler.evlPositionForNewHeight(shape.getCenter());
         shape.transform(position);
      };
   }
}
