/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.ui.render.impl.GridPainter;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
    private JFrame mainWindow;
    private JPanel content;

    public MainWindow() {

	content = new JPanel();

	GridPainter gridPainter = new GridPainter(new DefaultGrid());

	mainWindow = new JFrame();
	setLocation();
	mainWindow.add(content);
    }

    private void setLocation() {
	int top = (Toolkit.getDefaultToolkit().getScreenSize().height - mainWindow.getSize().height) / 2;
	int left = (Toolkit.getDefaultToolkit().getScreenSize().width - mainWindow.getSize().width) / 2;
	mainWindow.setLocation(left, top);
    }

    /**
     * Lets the current shown window disappears. If the given boolean is true, the
     * {@link BusinessDay} is checked for redundant entry
     * 
     * @param done
     */
    public void dispose() {
	mainWindow.dispose();
    }
}
