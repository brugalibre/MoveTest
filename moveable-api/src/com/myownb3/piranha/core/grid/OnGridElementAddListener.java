package com.myownb3.piranha.core.grid;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link OnGridElementAddListener} listens for added {@link GridElement} on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface OnGridElementAddListener {

   /**
    * Is called as soon as the given {@link GridElement} is added
    * 
    * @param gridElement
    *        the added {@link GridElement}
    */
   void onGridElementAdd(GridElement gridElement);
}
