package com.myownb3.piranha.ui.application.impl;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewAutoDetectablePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;

import java.util.HashSet;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;

public class LogicHandlerImpl implements LogicHandler {

   private Grid grid;
   private List<Renderer<? extends GridElement>> renderers;
   private HashSet<String> existingProjectiles;
   private MainWindow mainWindow;
   private TankBattleApplication tankBattleApplication;

   public LogicHandlerImpl(Grid grid, MainWindow mainWindow, List<Renderer<? extends GridElement>> renderers,
         TankBattleApplication tankBattleApplication) {
      this.grid = grid;
      this.renderers = renderers;
      this.mainWindow = mainWindow;
      this.tankBattleApplication = tankBattleApplication;
      this.existingProjectiles = new HashSet<>();
   }

   @Override
   public void onCylce() {
      SwingUtilities.invokeLater(() -> mainWindow.refresh());
      addNewAutoDetectablePainters(grid, renderers, existingProjectiles);
      removeDestroyedPainters(renderers);
      tankBattleApplication.run();
   }

}
