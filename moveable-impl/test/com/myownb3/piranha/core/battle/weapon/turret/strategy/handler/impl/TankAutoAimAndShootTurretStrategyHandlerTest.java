package com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder.ParkingAngleEvaluator;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShapeImpl;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScanner;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

class TankAutoAimAndShootTurretStrategyHandlerTest {

   @Test
   void testHandleTankStrategy_HasNot2Return2ParkingPos_BecauseIsShooting() {

      // Given
      Position pos = Positions.of(5, 5);

      ParkingAngleEvaluator parkingAngleEvaluator = () -> 45.0;
      TurretShape turretShape = mockTurretShape(pos);

      GunCarriage gunCarriage = mock(GunCarriage.class);
      TurretScanner turretScanner = mock(TurretScanner.class);
      when(turretScanner.getNearestDetectedTargetPos()).thenReturn(Optional.empty());
      TankAutoAimAndShootTurretStrategyHandler handler = new TankAutoAimAndShootTurretStrategyHandler(
            TurretStrategyHandleInput.of(gunCarriage, turretScanner, turretShape, parkingAngleEvaluator));
      handler.state = TurretState.SHOOTING;

      // When
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage, never()).turn2ParkPosition(Mockito.anyDouble());
   }

   @Test
   void testHandleTankStrategy_HasNot2Return2ParkingPos() {

      // Given
      Position pos = Positions.of(5, 5);
      double currentTurretAngle = pos.getDirection().getAngle();

      ParkingAngleEvaluator parkingAngleEvaluator = () -> currentTurretAngle;
      TurretShape turretShape = mockTurretShape(pos);

      GunCarriage gunCarriage = mock(GunCarriage.class);
      TankAutoAimAndShootTurretStrategyHandler handler = new TankAutoAimAndShootTurretStrategyHandler(
            TurretStrategyHandleInput.of(gunCarriage, mock(TurretScanner.class), turretShape, parkingAngleEvaluator));

      // When
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage, never()).turn2ParkPosition(Mockito.anyDouble());
   }

   @Test
   void testHandleTankStrategy_Has2Return2ParkingPos() {

      // Given
      Position pos = Positions.of(5, 5);
      double currentTurretAngle = 45.0;

      ParkingAngleEvaluator parkingAngleEvaluator = () -> currentTurretAngle;
      TurretShape turretShape = mockTurretShape(pos);

      GunCarriage gunCarriage = mock(GunCarriage.class);
      TankAutoAimAndShootTurretStrategyHandler handler = new TankAutoAimAndShootTurretStrategyHandler(
            TurretStrategyHandleInput.of(gunCarriage, mock(TurretScanner.class), turretShape, parkingAngleEvaluator));

      // When
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage).turn2ParkPosition(eq(currentTurretAngle));
   }

   private TurretShape mockTurretShape(Position pos) {
      TurretShape turretShape = mock(TurretShapeImpl.class);
      when(turretShape.getCenter()).thenReturn(pos);
      return turretShape;
   }

}
