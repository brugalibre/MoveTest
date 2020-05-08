package com.myownb3.piranha.ui.render.impl.shape;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public enum PaintMode {

   /** Defines that any {@link Shape} painter will always draw the path */
   PAINT_PATH,

   /** Defines that a {@link Shape} painter will paint the shape as it's defined by it's geometric regardless of it's actual path */
   SHAPE;
}
