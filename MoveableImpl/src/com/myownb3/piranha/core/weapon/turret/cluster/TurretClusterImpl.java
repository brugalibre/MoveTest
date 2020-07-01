package com.myownb3.piranha.core.weapon.turret.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.weapon.turret.cluster.shape.TurretClusterShapeImpl.TurretClusterShapeBuilder;

public class TurretClusterImpl implements TurretCluster {

   private List<Turret> turrets;
   private TurretClusterShape turretClusterShape;

   private TurretClusterImpl(List<Turret> turrets, TurretClusterShape turretClusterShape) {
      this.turrets = turrets;
      this.turretClusterShape = turretClusterShape;
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

   public static class TurretClusterBuilder {

      private List<Turret> turrets;
      private Position position;

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

      public TurretClusterImpl build() {
         TurretClusterShape turretClusterShape = buildTurretClusterShape();
         return new TurretClusterImpl(turrets, turretClusterShape);
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
