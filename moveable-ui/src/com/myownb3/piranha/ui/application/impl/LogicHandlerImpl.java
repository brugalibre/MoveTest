package com.myownb3.piranha.ui.application.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.explosion.MuzzleFlashPainter;
import com.myownb3.piranha.ui.render.impl.factory.GridElementPainterFactory;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class LogicHandlerImpl implements LogicHandler {

   private List<Renderer<?>> renderers;
   private Application application;
   private Predicate<GridElement> gridElementAddFilter;
   private Object lock;

   public LogicHandlerImpl(List<Renderer<?>> renderers, Application application) {
      this.renderers = renderers;
      this.application = application;
      this.gridElementAddFilter = getDefaultGridElementFilter();
      this.lock = new Object();
   }

   @Override
   public void onCylce() {
      removeDestroyedPainters(renderers);
      application.run();
   }

   private void removeDestroyedPainters(List<Renderer<?>> renderers) {
      synchronized (lock) {
         List<Renderer<?>> renderer2Remove = getRenderers2Remove(renderers);
         renderers.removeAll(renderer2Remove);
      }
   }

   private static List<Renderer<?>> getRenderers2Remove(List<Renderer<?>> renderers) {
      return renderers.stream()
            .filter(Renderer::canBeRemoved)
            .collect(Collectors.toList());
   }

   @Override
   public void onFire(Position furthermostGunPos) {
      WorkerThreadFactory.INSTANCE.executeAsync(() -> {
         onFireInternal(furthermostGunPos);
         return null;
      });
   }

   private void onFireInternal(Position furthermostGunPos) {
      MuzzleFlashPainter muzzleFlashPainter = new MuzzleFlashPainter(() -> furthermostGunPos);
      synchronized (lock) {
         renderers.add(muzzleFlashPainter);
      }
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
         GridElementPainter newGridElementPainter = GridElementPainterFactory.createGridElementPainter(gridElement);
         synchronized (lock) {
            renderers.add(newGridElementPainter);
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
