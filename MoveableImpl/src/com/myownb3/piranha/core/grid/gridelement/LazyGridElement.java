package com.myownb3.piranha.core.grid.gridelement;

/**
 * 
 * The {@link LazyGridElement} is used for layz acces to a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public class LazyGridElement {

   private GridElement gridElement;

   /**
    * @return the {@link GridElement} of this {@link LazyGridElement}
    */
   public GridElement getGridElement() {
      return this.gridElement;
   }

   /**
    * @param gridElement
    *        the {@link GridElement} of this {@link LazyGridElement}
    */
   public void setGridElement(GridElement gridElement) {
      this.gridElement = gridElement;
   }
}
