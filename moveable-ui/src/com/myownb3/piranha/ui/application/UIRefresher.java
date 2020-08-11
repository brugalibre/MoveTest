package com.myownb3.piranha.ui.application;

import javax.swing.SwingUtilities;

public class UIRefresher implements Runnable {

   private MainWindow mainWindow;

   public UIRefresher(MainWindow mainWindow) {
      this.mainWindow = mainWindow;
   }

   @Override
   public void run() {
      SwingUtilities.invokeLater(() -> mainWindow.refresh());
   }
}
