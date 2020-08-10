package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;

public class ProjectilePaintUtil {

   private ProjectilePaintUtil() {
      // privatos!
   }

   public static void addNewAutoDetectablePainters(Grid grid, List<Renderer<? extends GridElement>> renderers, Set<String> existingProjectiles) {
      List<AbstractGridElement> newProjectiles = getNewProjectiles(grid, existingProjectiles);
      addNewProjectilePainters(renderers, existingProjectiles, newProjectiles);
   }

   private static void addNewProjectilePainters(List<Renderer<? extends GridElement>> renderers, Set<String> existingAutoDetectables,
         List<AbstractGridElement> newAutoDetectables) {
      newAutoDetectables.stream()
            .forEach(gridElement -> {
               existingAutoDetectables.add(gridElement.getName());
               synchronized (renderers) {
                  renderers.add(new GridElementPainter(gridElement, getColor(gridElement), 1, 1));
               }
            });
   }

   public static List<Renderer<? extends GridElement>> getRenderer4DestroyedProjectiles(List<Renderer<? extends GridElement>> renderers) {
      return renderers.stream()
            .filter(Renderer::canBeRemoved)
            .collect(Collectors.toList());
   }

   private static List<AbstractGridElement> getNewProjectiles(Grid grid, Set<String> existingAutoDetectables) {
      return getNewAutoDetectables(grid, gridElement -> !existingAutoDetectables.contains(gridElement.getName()));
   }

   public static List<AbstractGridElement> getNewAutoDetectables(Grid grid, Predicate<? super AbstractGridElement> isNewAutoDetectable) {
      Predicate<GridElement> isTank = TankGridElement.class::isInstance;
      Predicate<GridElement> isTurret = TurretGridElement.class::isInstance;
      return grid.getAllGridElements(null)
            .stream()
            .filter(AutoDetectable.class::isInstance)
            .filter(isTank.negate())
            .filter(isTurret.negate())
            .map(AbstractGridElement.class::cast)
            .filter(isNewAutoDetectable)
            .collect(Collectors.toList());
   }

   public static void removeDestroyedPainters(List<Renderer<? extends GridElement>> renderers) {
      List<Renderer<? extends GridElement>> destroyedProjectiles = getRenderer4DestroyedProjectiles(renderers);
      synchronized (renderers) {
         renderers.removeAll(destroyedProjectiles);
      }
   }
}
