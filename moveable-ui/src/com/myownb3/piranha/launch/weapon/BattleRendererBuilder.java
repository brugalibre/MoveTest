package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.weapon.tank.TankGridElementImagePainter;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementImagePainter;

public class BattleRendererBuilder {

   private BattleRendererBuilder() {
      // private
   }

   public static List<Renderer<?>> createRenderer4TankBattleApplication(TankBattleApplication tankBattleApplication) {
      List<GridElement> gridElements = new ArrayList<>(tankBattleApplication.getTurretGridElements());
      gridElements.addAll(tankBattleApplication.getTankGridElements());
      return gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement)))
            .collect(Collectors.toList());
   }

   public static List<Renderer<?>> createImageRenderer4TankBattleApplication(TankBattleApplication tankBattleApplication) {
      List<Renderer<?>> renderers = new ArrayList<>();
      renderers.addAll(tankBattleApplication.getTurretGridElements()
            .stream()
            .map(TurretGridElementImagePainter::new)
            .collect(Collectors.toList()));
      renderers.addAll(tankBattleApplication.getTankGridElements()
            .stream()
            .map(TankGridElementImagePainter::new)
            .collect(Collectors.toList()));
      return renderers;
   }
}
