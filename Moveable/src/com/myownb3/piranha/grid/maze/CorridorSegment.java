package com.myownb3.piranha.grid.maze;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;

public interface CorridorSegment {

   /**
    * @return the left wall as a {@link GridElement}
    */
   GridElement getCorridorSegmentWallLeft();

   /**
    * @return the right wall as a {@link GridElement}
    */
   GridElement getCorridorSegmentWallRight();

   /**
    * @return the center of this {@link CorridorSegment}
    */
   Position getCorridorSegCenter();

}
