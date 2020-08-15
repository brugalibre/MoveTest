package com.myownb3.piranha.ui.render.impl.shape;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.impl.Drawable;

public abstract class AbstractShapePainter<E extends Shape> extends Drawable<E> {

   public AbstractShapePainter(E value) {
      super(value);
   }

   @Override
   public double getHightFromGround() {
      return value.getCenter().getZ();
   }
}
