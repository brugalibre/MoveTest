package com.myownb3.piranha.ui.application.impl;

import java.util.List;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.launch.weapon.listener.DelegateOnGunFireListener;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.UIRefresher;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * The {@link UILogicUtil} contains method for refreshing the UI (repaint or the current state) and as well as invoking the main game logic
 * and adding therefore new {@link Renderer}s
 * 
 * @author Dominic
 *
 */
public class UILogicUtil {

   private UILogicUtil() {
      // private 
   }

   public static void startLogicHandler(Grid grid, MainWindow mainWindow, List<Renderer<?>> renderers,
         TankBattleApplication tankBattleApplication, DelegateOnGunFireListener delegateOnGunFireListener, int cycleTime) {
      new Thread(() -> {
         LogicHandler logicHandler = new LogicHandlerImpl(mainWindow, renderers, tankBattleApplication);
         addHandlers(grid, delegateOnGunFireListener, logicHandler);
         while (true) {
            logicHandler.onCylce();
            sleep(cycleTime);
         }
      }, "LogicHandlerImpl").start();
   }

   private static void addHandlers(Grid grid, DelegateOnGunFireListener delegateOnGunFireListener, LogicHandler logicHandler) {
      grid.addOnGridElementAddListener(logicHandler);
      delegateOnGunFireListener.setOnGunFireListener(logicHandler);
   }

   public static void startUIRefresher(MainWindow mainWindow, int cycleTime) {
      new Thread(() -> {
         UIRefresher uiRefresher = new UIRefresher(mainWindow);
         while (true) {
            uiRefresher.run();
            sleep(cycleTime);
         }
      }, "UI-Refresher").start();
   }

   private static void sleep(int cycleTime) {
      try {
         Thread.sleep(cycleTime);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

}
