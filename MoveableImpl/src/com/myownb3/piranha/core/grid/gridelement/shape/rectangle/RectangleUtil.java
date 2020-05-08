package com.myownb3.piranha.core.grid.gridelement.shape.rectangle;

import java.util.List;

import com.myownb3.piranha.core.grid.position.Position;

public class RectangleUtil {

   private RectangleUtil() {
      // private
   }

   /**
    * Returns the next {@link Position} along the rectangles path or it returns the first Position
    * The returned value is always a copy of the actual element within the given list
    * 
    * @param path
    *        the path
    * @param index
    *        the current index
    * @return the next {@link Position} along the rectangles path or it returns the first Position
    */
   public static Position getNextPosition(List<Position> path, int index) {
      Position pathPos2;
      if (index + 1 < path.size()) {
         pathPos2 = path.get(index + 1);
      } else {
         pathPos2 = path.get(0);
      }
      return pathPos2;
   }
}
