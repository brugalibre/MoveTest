/**
 * 
 */
package com.myownb3.piranha.ui.render;

import java.awt.Rectangle;

/**
 * @author Dominic
 *
 */
public interface Renderer {

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
}
