/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Graphics2D;

import com.myownb3.piranha.ui.render.RenderContext;

/**
 * @author Dominic
 *
 */
public class Graphics2DContext implements RenderContext {

   private Graphics2D graphics2d;

   public Graphics2DContext(Graphics2D graphics2d) {
      this.graphics2d = graphics2d;
   }

   public final Graphics2D getGraphics2d() {
      return this.graphics2d;
   }
}
