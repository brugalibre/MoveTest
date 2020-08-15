/**
 * 
 */
package com.myownb3.piranha.ui.render;

import java.awt.Rectangle;

import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;

/**
 * @author Dominic
 *
 */
public interface Renderer<T> {

   /**
    * @return the value of this Renderer
    */
   T getValue();

   /**
    * Render on the given {@link RenderContext}
    * 
    * @param graphicsCtx
    *        - the context to render content on
    */
   public void render(RenderContext graphicsCtx);

   /**
    * Sets the bounds the layout manager has assigned to this renderer. Those, of
    * course, have to be considered in the rendering process.
    * 
    * @param bounds
    *        the new bounds for the renderer.
    */
   public void setBounds(Rectangle bounds);

   /**
    * Returns the {@link ColorSetMode} of this {@link Renderer}
    * 
    * @return the {@link ColorSetMode} of this {@link Renderer}
    */
   public ColorSetMode getColorSetMode();

   /**
    * Sets the {@link ColorSetMode} of this {@link Renderer}
    * 
    * @param drawMode
    *        the new {@link ColorSetMode}
    */
   public void setColorSetMode(ColorSetMode drawMode);

   /**
    * @return the distance from this {@link Renderer} to the ground
    */
   public double getHightFromGround();

   /***
    * @return <code>true</code> if this {@link Renderer} can be removed or <code>false</code> if not
    */
   public default boolean canBeRemoved() {
      return false;
   }
}
