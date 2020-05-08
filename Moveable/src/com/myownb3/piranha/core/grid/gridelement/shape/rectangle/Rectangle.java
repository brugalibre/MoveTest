package com.myownb3.piranha.core.grid.gridelement.shape.rectangle;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link Rectangle} defines a rectangular {@link Shape}
 * 
 * @author Dominic
 *
 */
public interface Rectangle extends Shape, Cloneable {

   /**
    * Provides one by one copy of this {@link Rectangle} instance
    * 
    * @return one by one copy of this {@link Rectangle} instance
    */
   Rectangle clone();

   /**
    * @return the upper left Position of this {@link Rectangle}
    */
   Position getUpperLeftPosition();

   /**
    * @return the width of this {@link Rectangle}
    */
   double getWidth();

   /**
    * @return the height of this {@link Rectangle}
    */
   double getHeight();
}
