/**
 * 
 */
package com.myownb3.piranha.ui.render.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
public class MoveablePainter extends AbstractGridElementPainter<Moveable> {

    private List<Position> positions2Paint;

    /**
     * @param gridElement
     * @param color
     * @param height
     * @param width
     */
    public MoveablePainter(Moveable gridElement, Color color, int height, int width) {
	super(gridElement, color, height, width);
	positions2Paint = new ArrayList<>();
    }

    /**
     * @return the positions2Paint
     */
    public Position getPositions2Paint() {
	synchronized (positions2Paint) {
	    return positions2Paint.remove(0);
	}
    }
    
    public final void addPositions2Paint(List<Position> positions2Paint) {
	synchronized (positions2Paint) {
	    this.positions2Paint.addAll(positions2Paint);
	}
    }
}
