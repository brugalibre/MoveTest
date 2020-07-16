package com.myownb3.piranha.ui.application;

import javax.swing.SwingUtilities;

public class UIRefresher {

   private MainWindow mainWindow;
   private int cycleTime;

   public UIRefresher(MainWindow mainWindow, int cycleTime) {
      this.mainWindow = mainWindow;
      this.cycleTime = cycleTime;
   }

   public void start() {
      new Thread(() -> {
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }
         }
      }, "UI-Refresher").start();
   }
}
