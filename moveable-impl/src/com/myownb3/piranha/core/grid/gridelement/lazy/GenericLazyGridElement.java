package com.myownb3.piranha.core.grid.gridelement.lazy;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * 
 * The {@link LazyGridElement} is used for layz acces to a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public class GenericLazyGridElement<T extends GridElement> {

   private T gridElement;

   /**
    * @return the {@link GridElement} of this {@link LazyGridElement}
    */
   public T getGridElement() {
      return this.gridElement;
   }

   /**
    * @param gridElement
    *        the {@link GridElement} of this {@link LazyGridElement}
    */
   public void setGridElement(T gridElement) {
      this.gridElement = gridElement;
   }
}
