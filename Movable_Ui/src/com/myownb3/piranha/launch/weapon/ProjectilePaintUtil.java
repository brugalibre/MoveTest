package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;

public class ProjectilePaintUtil {

   public static void addNewProjectilePainters(Grid grid, List<Renderer> renderers, Set<String> existingProjectiles,
         List<Moveable> moveables) {
      List<AbstractGridElement> newProjectiles = getNewProjectiles(grid, existingProjectiles);
      addNewProjectilePainters(renderers, existingProjectiles, moveables, newProjectiles);
   }

   private static void addNewProjectilePainters(List<Renderer> renderers, Set<String> existingProjectiles, List<Moveable> moveables,
         List<AbstractGridElement> newProjectiles) {
      newProjectiles.stream()
            .forEach(gridElement -> {
               existingProjectiles.add(gridElement.getName());
               synchronized (renderers) {
                  renderers.add(new GridElementPainter(gridElement, getColor(gridElement), 1, 1));
               }
               synchronized (moveables) {
                  moveables.add((Moveable) gridElement);
               }
            });
   }

   public static List<Renderer> getRenderer4DestroyedProjectiles(List<Renderer> renderers) {
      return renderers.stream()
            .filter(GridElementPainter.class::isInstance)
            .map(GridElementPainter.class::cast)
            .filter(gridElementPainter -> gridElementPainter.getValue() instanceof Destructible)
            .filter(destructible -> ((Destructible) destructible.getValue()).isDestroyed())
            .collect(Collectors.toList());
   }

   private static List<AbstractGridElement> getNewProjectiles(Grid grid, Set<String> existingProjectiles) {
      return getNewOrExistingProjectiles(grid, existingProjectiles, gridElement -> !existingProjectiles.contains(gridElement.getName()));
   }

   public static List<AbstractGridElement> getNewOrExistingProjectiles(Grid grid, Set<String> existingProjectiles,
         Predicate<? super AbstractGridElement> isNewOrExisting) {
      return grid.getAllGridElements(null)
            .stream()
            .map(AbstractGridElement.class::cast)
            .filter(Projectile.class::isInstance)
            .filter(isNewOrExisting)
            .collect(Collectors.toList());
   }

   public static void removeDestroyedPainters(List<Renderer> renderers) {
      List<Renderer> destroyedProjectiles = getRenderer4DestroyedProjectiles(renderers);
      synchronized (renderers) {
         renderers.removeAll(destroyedProjectiles);
      }
   }
}
