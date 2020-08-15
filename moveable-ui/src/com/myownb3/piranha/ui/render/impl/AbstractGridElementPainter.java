/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

/**
 * @author Dominic
 *
 */
public abstract class AbstractGridElementPainter<T extends GridElement> extends Drawable<T> {

   protected Drawable<? extends Shape> shapePainter;

   public AbstractGridElementPainter(T gridElement, Color color) {
      super(gridElement);
      this.shapePainter = ShapePainterFactory.getShapePainter(gridElement, color);

   }

   @Override
   public void render(RenderContext graphicsCtx) {
      shapePainter.render(graphicsCtx);
   }

   @Override
   public double getHightFromGround() {
      return value.getPosition().getZ();
   }
}
