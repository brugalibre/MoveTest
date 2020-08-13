package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;

public class BattleRendererBuilder {

   private BattleRendererBuilder() {
      // private
   }

   public static List<Renderer<? extends GridElement>> createRenderer4TankBattleApplication(TankBattleApplication tankBattleApplication) {
      List<GridElement> gridElements = new ArrayList<>(tankBattleApplication.getTurretGridElements());
      gridElements.addAll(tankBattleApplication.getTankGridElements());
      return gridElements.stream()
            .map(gridElement -> new GridElementPainter(gridElement, getColor(gridElement), 1, 1))
            .collect(Collectors.toList());
   }
}
