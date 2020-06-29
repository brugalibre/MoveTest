package com.myownb3.piranha.core.weapon.turret.turretscanner;

import static com.myownb3.piranha.core.weapon.turret.states.TurretState.SCANNING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner.TurretScannerBuilder;

class TurretScannerTest {

   @Test
   void testScan_UnknownState() {

      // Given
      Position pos = Positions.of(0, 0);
      Position gridElemPos = Positions.of(1, 1);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTurret(pos)
            .withTargetPos(gridElemPos)
            .withDetectedGridElement()
            .withPlacedDetector(pos)
            .withGrid()
            .withGridElementEvaluator()
            .withTurretScanner()
            .build();

      // When
      Executable exe = () -> {
         tcb.turretScanner.scan(TurretState.NONE);
      };

      // Then
      assertThrows(IllegalStateException.class, exe);
   }

   @Test
   void testScan_TargetAlreadyAcquired() {

      // Given
      Position pos = Positions.of(0, 0);
      Position gridElemPos = Positions.of(1, 1);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTurret(pos)
            .withTargetPos(gridElemPos)
            .withDetectedGridElement()
            .withPlacedDetector(pos)
            .withGrid()
            .withGridElementEvaluator()
            .withTurretScanner()
            .build();

      Position expectedAcquiredTargetPos = Positions.of(1, 1.1);

      // When
      TurretState newState = tcb.turretScanner.scan(SCANNING); // 1.
      when(tcb.detectedTarget.getPosition()).thenReturn(gridElemPos.movePositionForward());
      newState = tcb.turretScanner.scan(newState); // 2.
      newState = tcb.turretScanner.scan(newState); // 3. Still be acquiring, this must not change

      // Then
      Position actualAcquiredTargetPos = tcb.turretScanner.getNearestDetectedTargetPos().get();
      assertThat(actualAcquiredTargetPos, is(expectedAcquiredTargetPos));
      assertThat(newState, is(TurretState.ACQUIRING));
   }

   @Test
   void testScan_AcquireNonMovingTarget() {

      // Given
      Position pos = Positions.of(0, 0);
      Position gridElemPos = Positions.of(0, 1);

      Turret turret = mockTurret(pos);
      Obstacle simpleGridElement = mockGridElement(turret, gridElemPos, ObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      Position expectedAcquiredTargetPos = gridElemPos;

      // When
      TurretState turretState = turretScanner.scan(TurretState.SCANNING);
      when(simpleGridElement.getPosition()).thenReturn(gridElemPos.rotate(50));// Even with rotating, the target does not move
      turretState = turretScanner.scan(turretState);

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(nearestDetectedTargetPos.isPresent(), is(true));
      assertThat(nearestDetectedTargetPos.get(), is(expectedAcquiredTargetPos));
      assertThat(turretState, is(TurretState.ACQUIRING));
   }

   @Test
   void testScan_AcquireAMovingTarget() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElemPos = Positions.of(0, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 1.5);
      Position gridElemPosAfterTwoCycle = Positions.of(0, 2);

      Turret turret = mockTurret(pos);
      Moveable simpleGridElement = mockGridElement(turret, startGridElemPos, MoveableObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(15)))
            .build();

      Position expectedAcquiredTargetPos = Positions.of(0, 1.6071428571);

      // When
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING);
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);
      TurretState turretStateAfterSecondScan = turretScanner.scan(turretStateAfterFirstScan);
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterTwoCycle);

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(nearestDetectedTargetPos.isPresent(), is(true));
      assertThat(nearestDetectedTargetPos.get(), is(expectedAcquiredTargetPos));
      assertThat(turretStateAfterFirstScan, is(TurretState.TARGET_DETECTED));
      assertThat(turretStateAfterSecondScan, is(TurretState.ACQUIRING));
   }

   @Test
   void testScan_AcquireAMovingTarget_TargetFasterThenProjectile_LeadIsTargetPos() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElemPos = Positions.of(0, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 20.5);

      Turret turret = mockTurret(pos);
      Moveable simpleGridElement = mockGridElement(turret, startGridElemPos, MoveableObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      Position expectedAcquiredTargetPos = gridElemPosAfterOneCycle;

      // When
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING);
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);
      TurretState turretStateAfterSecondScan = turretScanner.scan(turretStateAfterFirstScan);

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(nearestDetectedTargetPos.isPresent(), is(true));
      assertThat(nearestDetectedTargetPos.get(), is(expectedAcquiredTargetPos));
      assertThat(turretStateAfterFirstScan, is(TurretState.TARGET_DETECTED));
      assertThat(turretStateAfterSecondScan, is(TurretState.ACQUIRING));
   }

   @Test
   void testScan_AcquireAMovingTargetUntilItDisappears() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElemPos = Positions.of(0, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 1.5);

      Turret turret = mockTurret(pos);
      Moveable simpleGridElement = mockGridElement(turret, startGridElemPos, MoveableObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      // When
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING); // 1. ccan
      when(simpleGridElement.isDetectedBy(any(), any())).thenReturn(false); // simulate that the moveable moved far away
      TurretState turretStateAfterSecondScan = turretScanner.scan(turretStateAfterFirstScan);// 2. acquire

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(turretStateAfterFirstScan, is(TurretState.TARGET_DETECTED));
      assertThat(turretStateAfterSecondScan, is(TurretState.SCANNING));
      assertThat(nearestDetectedTargetPos.isPresent(), is(false));
   }

   @Test
   void testScan_AcquireAndShootAMovingTargetUntilItDisappears() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElemPos = Positions.of(0, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 1.5);
      Position gridElemPosAfterTwoCycle = Positions.of(0, 2);

      Turret turret = mockTurret(pos);
      Moveable simpleGridElement = mockGridElement(turret, startGridElemPos, MoveableObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      // When
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING); // 1. ccan
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);
      turretScanner.scan(turretStateAfterFirstScan);// 2. acquire
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterTwoCycle);
      TurretState turretStateAfterThirdScan = turretScanner.scan(TurretState.SHOOTING);// 3. shoot
      when(simpleGridElement.isDetectedBy(any(), any())).thenReturn(false); // simulate that the moveable moved far away
      TurretState turretStateAfterFourthScan = turretScanner.scan(turretStateAfterThirdScan);// 4. Scanning

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(nearestDetectedTargetPos.isPresent(), is(false));
      assertThat(turretStateAfterThirdScan, is(TurretState.SHOOTING));
      assertThat(turretStateAfterFourthScan, is(TurretState.SCANNING));
   }

   @Test
   void testScan_AcquireAndShootAMovingTargetUntilWeIdentifyAnother() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElem1Pos = Positions.of(0, 1);
      Position startGridElem2Pos = Positions.of(1, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 1.5);
      Position gridElemPosAfterTwoCycle = Positions.of(0, 2);

      Turret turret = mockTurret(pos);
      Moveable firstTargetGridElement = mockGridElement(turret, startGridElem1Pos, MoveableObstacleImpl.class, true, 1);
      Moveable secondTargetGridElement = mockGridElement(turret, startGridElem2Pos, MoveableObstacleImpl.class, false, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Arrays.asList(firstTargetGridElement, secondTargetGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      // When
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING); // 1. ccan
      when(firstTargetGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);
      turretScanner.scan(turretStateAfterFirstScan);// 2. acquire
      when(firstTargetGridElement.getPosition()).thenReturn(gridElemPosAfterTwoCycle);
      TurretState turretStateAfterThirdScan = turretScanner.scan(TurretState.SHOOTING);// 3. shoot

      when(firstTargetGridElement.isDetectedBy(any(), any())).thenReturn(false); // simulate that the moveable moved far away
      when(secondTargetGridElement.isDetectedBy(any(), any())).thenReturn(true); // simulate that this moveable moveable moved into the range
      TurretState turretStateAfterFourthScan = turretScanner.scan(turretStateAfterThirdScan);// 4. Scanning

      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(nearestDetectedTargetPos.isPresent(), is(false));
      assertThat(turretStateAfterThirdScan, is(TurretState.SHOOTING));
      assertThat(turretStateAfterFourthScan, is(TurretState.SCANNING));
   }

   @Test
   void testScan_AcquireAndShootAMovingTargetAndThenItStopsMooving() {

      // Given
      Position pos = Positions.of(0, 0);
      Position startGridElemPos = Positions.of(0, 1);
      Position gridElemPosAfterOneCycle = Positions.of(0, 3.5);
      Position gridElemPosAfterTwoCycle = Positions.of(0, 5);
      Position gridElemPosAfterTwoCycleWithLead = Positions.of(0, 5);

      Turret turret = mockTurret(pos);
      Moveable simpleGridElement = mockGridElement(turret, startGridElemPos, MoveableObstacleImpl.class, true, 1);
      TurretScanner turretScanner = TurretScannerBuilder.builder()
            .withTurret(turret)
            .withGridElementEvaluator((p, d) -> Collections.singletonList(simpleGridElement))
            .withDetector(mockDetector())
            .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
            .build();

      // When
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterOneCycle);// Move the Target
      TurretState turretStateAfterFirstScan = turretScanner.scan(TurretState.SCANNING); // 1. scanning
      when(simpleGridElement.getPosition()).thenReturn(gridElemPosAfterTwoCycle);// Move the Target
      turretScanner.scan(turretStateAfterFirstScan);// 2. detecting the target
      Optional<Position> positionTurretIsAimingBeforeFireing = turretScanner.getNearestDetectedTargetPos();

      turretScanner.scan(TurretState.SHOOTING);// 3. shoot, target did not move
      Optional<Position> positionTurretShotAt = turretScanner.getNearestDetectedTargetPos();

      // Then
      assertThat(positionTurretIsAimingBeforeFireing.isPresent(), is(true));
      assertThat(positionTurretIsAimingBeforeFireing.get(), is(gridElemPosAfterTwoCycleWithLead));
      assertThat(positionTurretShotAt.isPresent(), is(true));
      assertThat(positionTurretShotAt.get(), is(gridElemPosAfterTwoCycle));
   }

   private static ProjectileConfig mockProjectileConfig(int velocity) {
      ProjectileConfig projectileConfig = mock(ProjectileConfig.class);
      when(projectileConfig.getVelocity()).thenReturn(velocity);
      when(projectileConfig.getDimensionInfo()).thenReturn(DimensionInfoBuilder.getDefaultDimensionInfo(5));
      return projectileConfig;
   }

   private static class TestCaseBuilder {

      private GridElement detectedTarget;
      private Position targetPos;
      private IDetector detector;
      private Turret turret;
      private GridElementEvaluator gridElementEvaluator;
      private TurretScanner turretScanner;
      private Grid grid;

      public TestCaseBuilder withDetectedGridElement() {
         detectedTarget = mockGridElement(turret, targetPos, ObstacleImpl.class, true, 1);
         return this;
      }

      public TestCaseBuilder withGrid() {
         this.grid = mock(Grid.class);
         List<GridElement> gridElemWithinDistance = Collections.singletonList(detectedTarget);
         doReturn(gridElemWithinDistance).when(grid).getAllGridElementsWithinDistance(any(), Mockito.anyInt());
         return this;
      }

      public TestCaseBuilder withPlacedDetector(Position pos) {
         this.detector = mockDetector();
         return this;
      }

      public TestCaseBuilder withTurret(Position turretPos) {
         this.turret = mockTurret(turretPos);
         return this;
      }

      public TestCaseBuilder withTurretScanner() {
         this.turretScanner = TurretScannerBuilder.builder()
               .withTurret(turret)
               .withGridElementEvaluator(gridElementEvaluator)
               .withDetector(detector)
               .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(mockProjectileConfig(1)))
               .build();
         return this;
      }

      public TestCaseBuilder withGridElementEvaluator() {
         this.gridElementEvaluator = mockGridElementEvaluator(turret, detector, detectedTarget);
         return this;
      }

      private TestCaseBuilder withTargetPos(Position targetPos) {
         this.targetPos = targetPos;
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }

      private GridElementEvaluator mockGridElementEvaluator(Turret turret, Detector detector, GridElement gridElement) {
         return (position, distance) -> grid.getAllGridElementsWithinDistance(gridElement.getPosition(),
               detector.getDetectorRange());
      }
   }

   private static IDetector mockDetector() {
      IDetector detector = mock(IDetector.class);
      when(detector.getDetectorRange()).thenReturn(100);
      return detector;
   }

   private static Turret mockTurret(Position turretPos) {
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getForemostPosition()).thenReturn(turretPos);
      when(turret.getBelligerentParty()).thenReturn(BelligerentPartyConst.GALACTIC_EMPIRE);
      return turret;
   }

   private static <T extends GridElement> T mockGridElement(Turret turret, Position gridElemPos, Class<T> gridElementClass, boolean isDetected,
         int velocity) {
      T gridElement = mock(gridElementClass);
      when(gridElement.getPosition()).thenReturn(gridElemPos);
      when(gridElement.isDetectedBy(any(), any())).thenReturn(isDetected);
      when(gridElement.getVelocity()).thenReturn(velocity);
      when(gridElement.getVelocity()).thenReturn(velocity);
      if (gridElement instanceof Belligerent) {
         when(((Belligerent) gridElement).getBelligerentParty()).thenReturn(BelligerentPartyConst.REBEL_ALLIANCE);
      }
      return gridElement;
   }

}
