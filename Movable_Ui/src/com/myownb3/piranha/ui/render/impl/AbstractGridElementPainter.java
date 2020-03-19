/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

/**
 * @author Dominic
 *
 */
public class AbstractGridElementPainter<T extends GridElement> extends Drawable<T> implements Renderer {

   private Drawable<? extends Shape> shapePainter;

   public AbstractGridElementPainter(T gridElement, Color color, int height, int width) {
      super(gridElement);
      Shape shape = ((SimpleGridElement) gridElement).getShape();
      this.shapePainter = ShapePainterFactory.getShapePainter(shape, color, height, width);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      shapePainter.render(graphicsCtx);
   }
}
