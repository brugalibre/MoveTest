package com.myownb3.piranha.ui.application;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewAutoDetectablePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;

public class LogicHandler {

   private long cycleTime;
   private MoveableAdder moveableAdder;
   private List<Renderer<? extends GridElement>> renderers;
   private Grid grid;
   private MainWindow mainWindow;
   private int padding;
   private boolean isRunning;
   private boolean withAbbort;

   public LogicHandler(MainWindow mainWindow, Grid grid, List<Renderer<? extends GridElement>> renderers, MoveableAdder moveableAdder,
         long cycleTime, int padding, boolean withAbbort) {
      this.mainWindow = mainWindow;
      this.grid = grid;
      this.renderers = renderers;
      this.moveableAdder = moveableAdder;
      this.cycleTime = cycleTime;
      this.padding = padding;
      this.isRunning = true;
      this.withAbbort = withAbbort;
   }

   public void start() {
      Set<String> existingProjectiles = new HashSet<>();
      new Thread(() -> {
         int cycleCounter = 0;
         while (isRunning) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            addNewAutoDetectablePainters(grid, renderers, existingProjectiles);
            removeDestroyedPainters(renderers);
            grid.getAllGridElements(null).parallelStream()
                  .filter(isGridElementAlive())
                  .filter(AutoDetectable.class::isInstance)
                  .map(AutoDetectable.class::cast)
                  .forEach(AutoDetectable::autodetect);
            cycleCounter++;
            if (moveableAdder.isCycleOver(cycleCounter)) {
               checkAndAddNewMoveables(padding);
               cycleCounter = 0;
            }
            if (withAbbort && checkGameDone(grid, mainWindow)) {
               isRunning = false;
            }
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "LogicHandler").start();
   }

   private void checkAndAddNewMoveables(int padding) {
      List<GridElement> newMoveables =
            moveableAdder.check4NewMoveables2Add(grid, DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig(), padding);
      for (GridElement gridElement : newMoveables) {
         renderers.add(new GridElementPainter(gridElement, GridElementColorUtil.getColor(gridElement), 0, 0));
      }
   }

   private static boolean checkGameDone(Grid grid, MainWindow mainWindow) {
      long amountOfGalacticEmpire = new ArrayList<>(grid.getAllGridElements(null)).parallelStream()
            .filter(isGridElementAlive())
            .filter(isBelligerent(BelligerentPartyConst.GALACTIC_EMPIRE))
            .count();
      long amountOfRebels = new ArrayList<>(grid.getAllGridElements(null)).parallelStream()
            .filter(isGridElementAlive())
            .filter(isBelligerent(BelligerentPartyConst.REBEL_ALLIANCE))
            .count();

      if (amountOfGalacticEmpire == 0 || amountOfRebels == 0) {
         mainWindow.showDoneInfo();
         return true;
      }
      return false;
   }

   private static Predicate<? super GridElement> isBelligerent(BelligerentParty belligerentParty) {
      return gridElement -> gridElement instanceof Belligerent && ((Belligerent) gridElement).getBelligerentParty() == belligerentParty;
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }
}
