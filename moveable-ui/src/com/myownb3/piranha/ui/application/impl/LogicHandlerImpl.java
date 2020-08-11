package com.myownb3.piranha.ui.application.impl;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewAutoDetectablePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.render.Renderer;

public class LogicHandlerImpl implements LogicHandler {

   private MoveableAdder moveableAdder;
   private Grid grid;
   private int cycleCounter;
   private List<Renderer<? extends GridElement>> renderers;
   private HashSet<String> existingProjectiles;
   private MainWindow mainWindow;

   public LogicHandlerImpl(Grid grid, MainWindow mainWindow, List<Renderer<? extends GridElement>> renderers, MoveableAdder moveableAdder) {
      this.grid = grid;
      this.moveableAdder = moveableAdder;
      this.renderers = renderers;
      this.cycleCounter = 0;
      this.mainWindow = mainWindow;
      this.existingProjectiles = new HashSet<>();
   }

   @Override
   public void onCylce() {
      cycleCounter++;
      callAutodetect();
      SwingUtilities.invokeLater(() -> mainWindow.refresh());
      addNewAutoDetectablePainters(grid, renderers, existingProjectiles);
      removeDestroyedPainters(renderers);
      if (moveableAdder.isCycleOver(cycleCounter)) {
         checkAndAddNewMoveables();
         cycleCounter = 0;
      }
   }

   private void callAutodetect() {
      grid.getAllGridElements(null).parallelStream()
            .filter(isGridElementAlive())
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(AutoDetectable::autodetect);
   }

   private void checkAndAddNewMoveables() {
      moveableAdder.check4NewMoveables2Add(grid, DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig());
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }
}
