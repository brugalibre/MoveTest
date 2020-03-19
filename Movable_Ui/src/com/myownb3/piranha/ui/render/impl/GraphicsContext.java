/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Graphics;

import com.myownb3.piranha.ui.render.RenderContext;

/**
 * @author Dominic
 *
 */
public class GraphicsContext implements RenderContext {

   private Graphics graphics;

   public GraphicsContext(Graphics graphics) {
      this.graphics = graphics;
   }

   public final Graphics getGraphics() {
      return this.graphics;
   }
}
