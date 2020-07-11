package com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;

/**
 * The {@link TIEFighterShape} represents the {@link Shape} of a {@link TFighter}
 * 
 * @author Dominic
 *
 */
public interface TIEFighterShape extends Shape {

   /**
    * @return the {@link Shape} of the cockpit
    */
   Circle getBallCockpit();

   /**
    * @return the {@link Shape} of the left wing
    */
   Rectangle getLeftWing();

   /**
    * @return the {@link Shape} of the right wing
    */
   Rectangle getRightWing();
}
