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
public class Graphicsontext implements RenderContext {

    private Graphics graphics;

    public Graphicsontext(Graphics graphics) {
	this.graphics = graphics;
    }

    public final Graphics getGraphics() {
	return this.graphics;
    }
}
