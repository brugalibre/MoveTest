package com.myownb3.piranha.core.weapon.turret;

import static com.myownb3.piranha.core.weapon.turret.states.TurretState.SCANNING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.PlacedDetector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.weapon.turret.TurretScanner.TurretScannerBuilder;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

class TurretScannerTest {

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

      Position expectedAcquiredTargetPos = Positions.of(1, 2.5);

      // When
      TurretState newState = tcb.turretScanner.scan(SCANNING); // 1.
      newState = tcb.turretScanner.scan(newState); // 2.

      // Then
      Position actualAcquiredTargetPos = tcb.turretScanner.getNearestDetectedTargetPos().get();
      assertThat(actualAcquiredTargetPos, is(expectedAcquiredTargetPos));
      assertThat(newState, is(TurretState.ACQUIRING));
   }

   private static class TestCaseBuilder {

      private GridElement detectedTarget;
      private Position targetPos;
      private PlacedDetector placedDetector;
      private Turret turret;
      private GridElementEvaluator gridElementEvaluator;
      private TurretScanner turretScanner;
      private Grid grid;

      public TestCaseBuilder withDetectedGridElement() {
         detectedTarget = mockGridElement(targetPos);
         return this;
      }

      public TestCaseBuilder withGrid() {
         this.grid = mock(Grid.class);
         List<GridElement> gridElemWithinDistance = Collections.singletonList(detectedTarget);
         doReturn(gridElemWithinDistance).when(grid).getAllGridElementsWithinDistance(any(), Mockito.anyInt());
         return this;
      }

      public TestCaseBuilder withPlacedDetector(Position pos) {
         this.placedDetector = mockPlacedDetector(pos);
         return this;
      }

      public TestCaseBuilder withTurret(Position turretPos) {
         this.turret = mock(Turret.class);
         when(turret.getForemostPosition()).thenReturn(turretPos);
         return this;
      }

      public TestCaseBuilder withTurretScanner() {
         this.turretScanner = TurretScannerBuilder.builder()
               .withTurret(turret)
               .withGridElementEvaluator(gridElementEvaluator)
               .withPlacedDetector(placedDetector)
               .withTargetPositionLeadEvaluator(new TargetPositionLeadEvaluatorImpl(1))
               .build();
         return this;
      }

      public TestCaseBuilder withGridElementEvaluator() {
         this.gridElementEvaluator = mockGridElementEvaluator(turret, placedDetector, detectedTarget);
         return this;
      }

      private TestCaseBuilder withTargetPos(Position targetPos) {
         this.targetPos = targetPos;
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }

      private GridElementEvaluator mockGridElementEvaluator(Turret turret, PlacedDetector placedDetector, GridElement gridElement) {
         return (position, distance) -> grid.getAllGridElementsWithinDistance(gridElement.getPosition(),
               placedDetector.getDetector().getDetectorRange());
      }

      private GridElement mockGridElement(Position gridElemPos) {
         GridElement gridElement = mock(GridElement.class);
         when(gridElement.getPosition()).thenReturn(gridElemPos);
         when(gridElement.isDetectedBy(any(), any())).thenReturn(true);
         when(gridElement.isAimable()).thenReturn(true);
         return gridElement;
      }

      private PlacedDetector mockPlacedDetector(Position pos) {
         PlacedDetector placedDetector = mock(PlacedDetector.class);
         IDetector detector = mock(IDetector.class);
         when(placedDetector.getDetector()).thenReturn(detector);
         when(detector.getDetectorRange()).thenReturn(100);
         when(placedDetector.getPosition()).thenReturn(pos);
         return placedDetector;
      }
   }
}
