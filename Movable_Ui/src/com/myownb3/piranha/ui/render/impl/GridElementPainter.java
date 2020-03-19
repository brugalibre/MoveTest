/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.GridElement;

/**
 * @author Dominic
 *
 */
public class GridElementPainter extends AbstractGridElementPainter<GridElement> {

   /**
    * @param gridElement
    * @param color
    * @param height
    * @param width
    */
   public GridElementPainter(GridElement gridElement, Color color, int height, int width) {
      super(gridElement, color, height, width);
   }

}
