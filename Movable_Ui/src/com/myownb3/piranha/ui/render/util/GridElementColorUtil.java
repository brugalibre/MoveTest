package com.myownb3.piranha.ui.render.util;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.ui.grid.gridelement.EndPositionGridElement;

public class GridElementColorUtil {

   private GridElementColorUtil() {
      // private
   }

   /**
    * Returns the {@link Color} which is used to render the given {@link GridElement}
    * 
    * @param gridElement
    *        the GridElement
    * @return the {@link Color} which is used to render the given {@link GridElement}
    */
   public static Color getColor(GridElement gridElement) {

      if (gridElement instanceof Obstacle) {
         return Color.RED;
      } else if (gridElement instanceof Moveable) {
         return new Color(0, 206, 209).darker();
      } else if (gridElement instanceof EndPositionGridElement) {
         return new Color(34, 139, 34);
      } else if (gridElement instanceof GridElement) {
         return Color.BLACK;
      }
      throw new IllegalStateException("Unknown GridElement '" + gridElement + "'!");
   }

   /**
    * @return the {@link Color} which is used to render the path of a {@link Moveable}
    */
   public static Color getPositionListColor() {
      return Color.GREEN;
   }
}