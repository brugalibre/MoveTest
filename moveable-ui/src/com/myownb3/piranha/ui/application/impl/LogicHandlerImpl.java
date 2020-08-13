package com.myownb3.piranha.ui.application.impl;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class LogicHandlerImpl implements LogicHandler {

   private List<Renderer<? extends GridElement>> renderers;
   private MainWindow mainWindow;
   private TankBattleApplication tankBattleApplication;
   private Predicate<GridElement> gridElementAddFilter;
   private Object lock;

   public LogicHandlerImpl(MainWindow mainWindow, List<Renderer<? extends GridElement>> renderers, TankBattleApplication tankBattleApplication) {
      this.renderers = renderers;
      this.mainWindow = mainWindow;
      this.tankBattleApplication = tankBattleApplication;
      this.gridElementAddFilter = getDefaultGridElementFilter();
      this.lock = new Object();
   }

   @Override
   public void onCylce() {
      SwingUtilities.invokeLater(() -> mainWindow.refresh());
      removeDestroyedPainters(renderers);
      tankBattleApplication.run();
   }

   public void removeDestroyedPainters(List<Renderer<? extends GridElement>> renderers) {
      synchronized (lock) {
         List<Renderer<? extends GridElement>> renderer2Remove = getRenderers2Remove(renderers);
         renderers.removeAll(renderer2Remove);
      }
   }

   private static List<Renderer<? extends GridElement>> getRenderers2Remove(List<Renderer<? extends GridElement>> renderers) {
      return renderers.stream()
            .filter(Renderer::canBeRemoved)
            .collect(Collectors.toList());
   }

   @Override
   public void onGridElementAdd(GridElement gridElement) {
      WorkerThreadFactory.INSTANCE.executeAsync(() -> {
         onGridElementAddInternal(gridElement);
         return null;
      });
   }

   private void onGridElementAddInternal(GridElement gridElement) {
      if (gridElementAddFilter.test(gridElement)) {
         synchronized (lock) {
            renderers.add(new GridElementPainter(gridElement, getColor(gridElement), 1, 1));
         }
      }
   }

   /**
    * Tanks & Turrets are already added by the the specific launcher of the application
    * Here we only add automatically added projectiles, obstacles and so on
    */
   private Predicate<GridElement> getDefaultGridElementFilter() {
      Predicate<GridElement> isTank = TankGridElement.class::isInstance;
      Predicate<GridElement> isTurret = TurretGridElement.class::isInstance;
      return isTank.negate().and(isTurret.negate());
   }
}
