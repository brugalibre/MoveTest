package com.myownb3.piranha.core.weapon.turret;

import java.util.List;
import java.util.Objects;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TurretGridElement implements Turret, GridElement {

   private Turret turret;
   private GridElement turretGridElement;

   private TurretGridElement(Grid grid, Turret turret) {
      turretGridElement = SimpleGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(turret.getShape().getCenter())
            .withShape(turret.getShape())
            .build();
      this.turret = turret;
   }

   @Override
   public void autodetect() {
      turret.autodetect();
   }

   @Override
   public GunCarriage getGunCarriage() {
      return turret.getGunCarriage();
   }

   @Override
   public TurretState getTurretStatus() {
      return turret.getTurretStatus();
   }

   @Override
   public Position getPosition() {
      return turretGridElement.getPosition();
   }

   @Override
   public Position getForemostPosition() {
      return turretGridElement.getForemostPosition();
   }

   @Override
   public TurretShape getShape() {
      return turret.getShape();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      turretGridElement.onCollision(gridElements);
   }

   @Override
   public Position getRearmostPosition() {
      return turretGridElement.getRearmostPosition();
   }

   @Override
   public Grid getGrid() {
      return turretGridElement.getGrid();
   }

   @Override
   public double getDimensionRadius() {
      return turretGridElement.getDimensionRadius();
   }

   @Override
   public void hasGridElementDetected(GridElement gridElement, Detector detector) {
      turretGridElement.hasGridElementDetected(gridElement, detector);
   }

   @Override
   public boolean isDetectedBy(Position detectionPos, Detector detector) {
      return turretGridElement.isDetectedBy(detectionPos, detector);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return turretGridElement.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   public static class TurretGridElementBuilder {

      private Grid grid;
      private Turret turret;
      private GridElement gridElement;

      private TurretGridElementBuilder() {
         // private
      }

      public TurretGridElementBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TurretGridElementBuilder withTurretGridElement(GridElement gridElement) {
         this.gridElement = gridElement;
         return this;
      }

      public TurretGridElementBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TurretGridElement build() {
         if (Objects.nonNull(gridElement)) {
            // 4 Testing purpose
            TurretGridElement turretGridElement = new TurretGridElement(grid, turret);
            turretGridElement.turretGridElement = gridElement;
            return turretGridElement;
         }
         return new TurretGridElement(grid, turret);
      }

      public static TurretGridElementBuilder builder() {
         return new TurretGridElementBuilder();
      }
   }
}
