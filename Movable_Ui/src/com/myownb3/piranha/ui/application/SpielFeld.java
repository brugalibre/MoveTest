/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.Graphicsontext;

/**
 * @author Dominic
 *
 */
public class SpielFeld extends JComponent {

    private static final long serialVersionUID = 1L;
    private List<Renderer> renderers;

    /**
     * 
     */
    public SpielFeld(List<Renderer> renderers) {
	this.renderers = renderers;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
	super.paintComponent(graphics);

	Graphicsontext graphicsontext = new Graphicsontext(graphics);
	renderers.forEach(renderer -> renderer.render(graphicsontext));
    }
}
