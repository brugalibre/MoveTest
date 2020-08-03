package com.myownb3.piranha.core.battle.weapon.turret.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShapeImpl.TurretClusterShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class TurretClusterImpl implements TurretCluster {

   private List<Turret> turrets;
   private TurretClusterShape turretClusterShape;
   private Optional<DestructionHelper> destructionHelperOpt;

   private TurretClusterImpl(List<Turret> turrets, TurretClusterShape turretClusterShape, DestructionHelper destructionHelper) {
      this.turrets = turrets;
      this.turretClusterShape = turretClusterShape;
      this.destructionHelperOpt = Optional.ofNullable(destructionHelper);
   }

   @Override
   public Shape getShape() {
      return turretClusterShape;
   }

   @Override
   public boolean isAcquiring() {
      return turrets.stream()
            .anyMatch(Turret::isAcquiring);
   }

   @Override
   public boolean isShooting() {
      return turrets.stream()
            .anyMatch(Turret::isShooting);
   }

   @Override
   public void autodetect() {
      turrets.stream()
            .forEach(Turret::autodetect);
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return getFirstTurret().getBelligerentParty();
   }

   @Override
   public List<Turret> getTurrets() {
      return turrets;
   }

   private Turret getFirstTurret() {
      return turrets.get(0);
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelperOpt.ifPresent(destructionHelper -> destructionHelper.onCollision(gridElements));
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelperOpt.map(DestructionHelper::isDestroyed)
            .orElse(false);
   }

   public static class TurretClusterBuilder {

      private List<Turret> turrets;
      private Position position;
      private DestructionHelper destructionHelper;

      private TurretClusterBuilder() {
         this.turrets = new ArrayList<>();
      }

      public TurretClusterBuilder withTurret(Turret turret) {
         this.turrets.add(turret);
         return this;
      }

      public TurretClusterBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      public TurretClusterBuilder withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
         return this;
      }

      public TurretClusterImpl build() {
         TurretClusterShape turretClusterShape = buildTurretClusterShape();
         return new TurretClusterImpl(turrets, turretClusterShape, destructionHelper);
      }

      private TurretClusterShape buildTurretClusterShape() {
         return TurretClusterShapeBuilder.builder()
               .withPosition(position)
               .withTurretShapes(turrets.stream()
                     .map(Turret::getShape)
                     .collect(Collectors.toList()))
               .build();
      }

      public static TurretClusterBuilder builder() {
         return new TurretClusterBuilder();
      }
   }
}
