package com.myownb3.piranha.core.battle.weapon.tank.countermeasure;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.countermeasure.MissileCounterMeasureSystemImpl.MissileCounterMeasureSystemBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShapeImpl;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.lineshape.ImmutableLineShape.ImmutableLineShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class MissileCounterMeasureSystemImplTest {

   @Test
   void testAutodetect_NothingDetected() {

      // Given
      Position pos = Positions.of(5, 5);
      DecoyFlareDispenser decoyFlareDispenser = mockDecoyFlareDispenser();
      TankGridElement tankGridElement = mockTankGridElement(pos);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDecoyFlareDispenser(decoyFlareDispenser)
            .withTankGridElement(tankGridElement)
            .build();

      // When
      tcb.missileCounterMeasureSystem.autodetect();

      // Then
      //      verify(tcb.detector).checkSurroundingFromPosition(eq(tankGridElement), eq(pos));
      verify(decoyFlareDispenser, never()).dispenseDecoyFlares(any());
   }

   private TankGridElement mockTankGridElement(Position pos) {
      TankGridElement tankGridElement = mock(TankGridElement.class);
      when(tankGridElement.getPosition()).thenReturn(pos);
      TankShape shape = mock(TankShapeImpl.class);
      when(shape.getHull()).thenReturn(ImmutableLineShapeBuilder.builder()
            .withBeginPosition(pos.movePositionForward())
            .withEndPosition(pos)
            .build());
      when(tankGridElement.getShape()).thenReturn(shape);
      return tankGridElement;
   }

   @Test
   void testAutodetect_DetectedButNotProjectile() {

      // Given
      DecoyFlareDispenser decoyFlareDispenser = mockDecoyFlareDispenser();
      TankGridElement tankGridElement = mock(TankGridElement.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDecoyFlareDispenser(decoyFlareDispenser)
            .withTankGridElement(tankGridElement)
            .withDetectedGridElement(mock(GridElement.class), false)
            .build();

      // When
      tcb.missileCounterMeasureSystem.autodetect();

      // Then
      verify(decoyFlareDispenser, never()).dispenseDecoyFlares(any());
   }

   @Test
   void testAutodetect_DetectedProjectileButBullet() {

      // Given
      DecoyFlareDispenser decoyFlareDispenser = mockDecoyFlareDispenser();
      TankGridElement tankGridElement = mock(TankGridElement.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDecoyFlareDispenser(decoyFlareDispenser)
            .withTankGridElement(tankGridElement)
            .withDetectedGridElement(mockMissile(ProjectileTypes.BULLET), false)
            .build();

      // When
      tcb.missileCounterMeasureSystem.autodetect();

      // Then
      verify(decoyFlareDispenser, never()).dispenseDecoyFlares(any());
   }

   private DecoyFlareDispenser mockDecoyFlareDispenser() {
      DecoyFlareDispenser mock = mock(DecoyFlareDispenser.class);
      when(mock.getDecoyFlareConfig()).thenReturn(DecoyFlareConfigBuilder.builder()
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
            .withVelocity(4)
            .build());
      return mock;
   }

   @Test
   void testAutodetect_DetectedMissileButToFarAway() {

      // Given
      DecoyFlareDispenser decoyFlareDispenser = mockDecoyFlareDispenser();
      TankGridElement tankGridElement = mock(TankGridElement.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDecoyFlareDispenser(decoyFlareDispenser)
            .withTankGridElement(tankGridElement)
            .withDetectedGridElement(mockMissile(ProjectileTypes.MISSILE), false)
            .build();

      // When
      tcb.missileCounterMeasureSystem.autodetect();

      // Then
      verify(decoyFlareDispenser, never()).dispenseDecoyFlares(any());
   }

   @Test
   void testAutodetect_DetectedMissile() {

      // Given
      DecoyFlareDispenser decoyFlareDispenser = mockDecoyFlareDispenser();
      TankGridElement tankGridElement = mockTankGridElement(Positions.of(5, 5));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDecoyFlareDispenser(decoyFlareDispenser)
            .withTankGridElement(tankGridElement)
            .withDetectedGridElement(mockMissile(ProjectileTypes.MISSILE, Positions.of(10, 10)), true)
            .build();

      // When
      tcb.missileCounterMeasureSystem.autodetect();

      // Then
      verify(decoyFlareDispenser).dispenseDecoyFlares(any());
   }

   private ProjectileGridElement mockMissile(ProjectileTypes type) {
      ProjectileGridElement projectileGridElement = mock(ProjectileGridElement.class);
      when(projectileGridElement.getProjectileType()).thenReturn(type);
      return projectileGridElement;
   }

   private ProjectileGridElement mockMissile(ProjectileTypes type, Position pos) {
      ProjectileGridElement projectileGridElement = mock(ProjectileGridElement.class);
      when(projectileGridElement.getProjectileType()).thenReturn(type);
      when(projectileGridElement.getPosition()).thenReturn(pos);
      when(projectileGridElement.isAvoidable()).thenReturn(true);
      return projectileGridElement;
   }

   private static class TestCaseBuilder {

      private MissileCounterMeasureSystem missileCounterMeasureSystem;
      private DecoyFlareDispenser decoyFlareDispenser;
      private TankGridElement tankGridElement;
      private List<GridElement> detectedGridElements;
      private Map<GridElement, Boolean> isEvasionGridElements;
      private Detector detector;

      private TestCaseBuilder() {
         detectedGridElements = new ArrayList<>();
         isEvasionGridElements = new HashMap<>();
      }

      private TestCaseBuilder withDetectedGridElement(GridElement detectedGridElement, boolean isEvasion) {
         this.detectedGridElements.add(detectedGridElement);
         this.isEvasionGridElements.put(detectedGridElement, isEvasion);
         return this;
      }

      private TestCaseBuilder withTankGridElement(TankGridElement tankGridElement) {
         this.tankGridElement = tankGridElement;
         return this;
      }

      private TestCaseBuilder withDecoyFlareDispenser(DecoyFlareDispenser decoyFlareDispenser) {
         this.decoyFlareDispenser = decoyFlareDispenser;
         return this;
      }

      private TestCaseBuilder build() {
         Grid grid = mockGrid();
         mockDetector();
         missileCounterMeasureSystem = MissileCounterMeasureSystemBuilder.builder()
               .withDecoyFlareDispenser(decoyFlareDispenser)
               .withGrid(grid)
               .withDetector(detector)
               .withTankGridElementSupplier(() -> tankGridElement)
               .build();
         return this;
      }

      private Detector mockDetector() {
         this.detector = mock(Detector.class);
         for (GridElement gridElement : detectedGridElements) {
            when(gridElement.isDetectedBy(any(), eq(detector))).thenReturn(true);
            when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
         }

         for (Entry<GridElement, Boolean> entry : isEvasionGridElements.entrySet()) {
            when(detector.isEvasion(eq(entry.getKey()))).thenReturn(entry.getValue());
         }
         return detector;
      }

      private Grid mockGrid() {
         Grid grid = mock(Grid.class);
         when(grid.getAllAvoidableGridElementsWithinDistance(eq(tankGridElement), anyInt())).thenReturn(detectedGridElements);
         when(grid.getAllGridElements(eq(tankGridElement))).thenReturn(detectedGridElements);
         return grid;
      }
   }
}
