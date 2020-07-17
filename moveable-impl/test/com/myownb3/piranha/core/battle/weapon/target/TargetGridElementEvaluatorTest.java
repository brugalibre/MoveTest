package com.myownb3.piranha.core.battle.weapon.target;
//package com.myownb3.piranha.core.battle.weapon.target;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//
//import com.myownb3.piranha.core.grid.gridelement.GridElement;
//import com.myownb3.piranha.core.grid.position.Positions;
//import com.myownb3.piranha.core.grid.position.Position;
//import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
//import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScannerTest.TestCaseBuilder;
//
//class TargetGridElementEvaluatorTest {
//
//   @Test
//   void testScan_GridElementIsNotEnemy() {
//
//      // Given
//      Position pos = Positions.of(0, 0);
//      Position gridElemPos = Positions.of(1, 1);
//      TestCaseBuilder tcb = new TestCaseBuilder()
//            .withTurret(pos)
//            .withTargetPos(gridElemPos)
//            .withDetectedGridElement()
//            .withPlacedDetector(pos)
//            .withGrid()
//            .withGridElementEvaluator()
//            .withTurretScanner()
//            .build();
//      GridElement gridElement = mock(GridElement.class);
//
//      // When
//      boolean actualIsEnemy = tcb.turretScanner.isGridElementEnemy(gridElement);
//
//      // Then
//      assertThat(actualIsEnemy, is(false));
//   }
//
//   @Test
//   void testScan_TankGridElementIsEnemy() {
//
//      // Given
//      Position pos = Positions.of(0, 0);
//      Position gridElemPos = Positions.of(1, 1);
//      TestCaseBuilder tcb = new TestCaseBuilder()
//            .withTurret(pos)
//            .withTargetPos(gridElemPos)
//            .withDetectedGridElement()
//            .withPlacedDetector(pos)
//            .withGrid()
//            .withGridElementEvaluator()
//            .withTurretScanner()
//            .build();
//
//      TankGridElement gridElement = mock(TankGridElement.class);
//      when(tcb.turret.isEnemy(eq(gridElement))).thenReturn(true);
//
//      // When
//      boolean actualIsEnemy = tcb.turretScanner.isGridElementEnemy(gridElement);
//
//      // Then
//      assertThat(actualIsEnemy, is(true));
//   }
//
//   @Test
//   void testScan_TankGridElementIsNotEnemy() {
//
//      // Given
//      Position pos = Positions.of(0, 0);
//      Position gridElemPos = Positions.of(1, 1);
//      TestCaseBuilder tcb = new TestCaseBuilder()
//            .withTurret(pos)
//            .withTargetPos(gridElemPos)
//            .withDetectedGridElement()
//            .withPlacedDetector(pos)
//            .withGrid()
//            .withGridElementEvaluator()
//            .withTurretScanner()
//            .build();
//
//      TankGridElement gridElement = mock(TankGridElement.class);
//      when(tcb.turret.isEnemy(eq(gridElement))).thenReturn(false);
//
//      // When
//      boolean actualIsEnemy = tcb.turretScanner.isGridElementEnemy(gridElement);
//
//      // Then
//      assertThat(actualIsEnemy, is(false));
//   }
//
//}
