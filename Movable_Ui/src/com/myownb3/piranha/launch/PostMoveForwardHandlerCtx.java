package com.myownb3.piranha.launch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

public class PostMoveForwardHandlerCtx {

   private MoveableController moveableController;
   private Grid grid;
   private MainWindow mainWindow;
   private List<Renderer<? extends GridElement>> renderers;
   private Set<String> existingProjectiles;
   private List<GridElement> gridElements;
   private List<Renderer<PositionListPainter>> endPosRenderers;

   public PostMoveForwardHandlerCtx() {
      this.renderers = new ArrayList<>();
      this.endPosRenderers = new ArrayList<>();
      this.existingProjectiles = new HashSet<>();
      this.gridElements = new ArrayList<>();
   }

   public MoveableController getMoveableController() {
      return moveableController;
   }

   public void setMoveableController(MoveableController moveableController) {
      this.moveableController = moveableController;
   }

   public Grid getGrid() {
      return grid;
   }

   public void setGrid(Grid grid) {
      this.grid = grid;
   }

   public MainWindow getMainWindow() {
      return mainWindow;
   }

   public void setMainWindow(MainWindow mainWindow) {
      this.mainWindow = mainWindow;
   }

   public Set<String> getExistingProjectiles() {
      return existingProjectiles;
   }

   public List<Renderer<? extends GridElement>> getRenderers() {
      return renderers;
   }

   public List<GridElement> getGridElements() {
      return gridElements;
   }

   public List<Renderer<PositionListPainter>> getEndPositionRenderers() {
      return endPosRenderers;
   }
}
