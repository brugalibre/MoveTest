package com.myownb3.piranha.core.grid.gridelement.factory;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.LazyGridElement;

/**
 * Base class for all factory's for {@link GridElement}s
 * 
 * @author DStalder
 *
 */
public abstract class AbstractGridElementFactory {

   protected Grid grid;


   public void registerGrid(Grid grid) {
      this.grid = requireNonNull(grid);
   }

   protected void checkGrid() {
      if (isNull(grid)) {
         throw new IllegalStateException(
               "Use '" + this.getClass().getSimpleName() + ".INSTANCE.registerGrid(myGrid)' before creating any GridElements!");
      }
   }

   protected OnDestroyedCallbackHandler getDefaultOnDestroyedCallbackHandler(LazyGridElement lazyGridElement) {
      return () -> grid.remove(lazyGridElement.getGridElement());
   }

   /**
    * removes a previously registered {@link Grid}
    */
   public void deregisterGrid() {
      this.grid = null;
   }

}
