package com.myownb3.piranha.core.weapon;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * {@link AutoDetectable} defines objects which are capable to detect other {@link GridElement} as their targets and take further actions,
 * completely on their own
 * 
 * @author Dominic
 *
 */
public interface AutoDetectable {

   /**
    * Makes this {@link AutoDetectable} to scan it's environment for possible targets. It automatically detects and acquires the nearest
    * {@link GridElement}.
    * 
    */
   void autodetect();
}
