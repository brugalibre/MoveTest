package com.myownb3.piranha.application.battle.util;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.battle.util.MoveableAdder.MoveableAdderBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

class MoveableAdderTest {

   @Test
   void testIsCycleDone_NotDone() {

      // Given
      int counter = 50;
      MoveableAdder moveableAdder = MoveableAdderBuilder.builder()
            .withCounter(counter)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .build();

      // When
      boolean isActualCycleOver = moveableAdder.isCycleOver(counter - 1);

      // Then
      assertThat(isActualCycleOver, is(false));
   }

   @Test
   void testIsCycleDone_Done() {

      // Given
      int counter = 50;
      MoveableAdder moveableAdder = MoveableAdderBuilder.builder()
            .withCounter(counter)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withPadding(10)
            .build();

      // When
      boolean isActualCycleOver = moveableAdder.isCycleOver(counter + 1);

      // Then
      assertThat(isActualCycleOver, is(true));
   }

   @Test
   void testCheck4NewMoveables2Add_NoneToAdd() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 1;
      int amountOfMoveables = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .addExistingObstacle()
            .addExistingMoveable()
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(0));
   }

   @Test
   void testCheck4NewMoveables2Add_AddOneMoveable_IgnoreProjectile() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 0;
      int amountOfMoveables = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .withGridElement(ProjectileGridElement.class, PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(1));
   }

   @Test
   void testCheck4NewMoveables2Add_AddOneMoveable_IgnoreTank() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 0;
      int amountOfMoveables = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .withGridElement(TankGridElement.class, mock(TankShape.class))
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(1));
   }

   @Test
   void testCheck4NewMoveables2Add_IgnoreDestroyedOne() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 0;
      int amountOfMoveables = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .addExistingMoveable(-5)
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(1));
   }

   @Test
   void testCheck4NewObstacle_IgnoreMoveable() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 1;
      int amountOfMoveables = 0;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .addExistingMoveable(5)
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(1));
      assertThat(Obstacle.class.isAssignableFrom(actualNewAddeMoveables.get(0).getClass()), is(true));
   }

   @Test
   void testCheck4NewMoveables2Add_AddOneMoveable() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 1;
      int amountOfMoveables = 1;
      int moveableVelocity = 5;
      int gridElementRadius = 8;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withMoveableVelocity(moveableVelocity)
                  .withGridElementRadius(gridElementRadius)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .addExistingObstacle()
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeMoveables.size(), is(1));
      GridElement gridElement = actualNewAddeMoveables.get(0);
      assertThat(AutoMoveableController.class.isAssignableFrom(gridElement.getClass()), is(true));
      assertThat(gridElement.getVelocity(), is(moveableVelocity));
      assertThat(gridElement.getDimensionInfo().getDimensionRadius(), is((double) gridElementRadius));
   }

   @Test
   void testCheck4NewMoveables2Add_AddOneNonMoveable() {

      // Given
      int counter = 50;
      int amountOfNonMoveables = 1;
      int amountOfMoveables = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .addExistingMoveable()
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeGridElements = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);

      // Then
      assertThat(actualNewAddeGridElements.size(), is(1));
      GridElement gridElement = actualNewAddeGridElements.get(0);
      assertThat(Obstacle.class.isAssignableFrom(gridElement.getClass()), is(true));
   }

   private static class TestCaseBuilder {
      public EvasionStateMachineConfig evasionStateMachineConfig;
      private MoveableAdder moveableAdder;
      private Grid grid;

      private TestCaseBuilder() {
         // private 
      }

      public TestCaseBuilder withGridElement(Class<? extends GridElement> gridElemClass, Shape shape) {
         requireNonNull(grid);
         GridElement gridElement = mock(gridElemClass);
         Position pos = Positions.of(5, 5);
         when(gridElement.getShape()).thenReturn(shape);
         when(gridElement.getPosition()).thenReturn(pos);
         grid.addElement(gridElement);
         return this;
      }

      public TestCaseBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig evasionStateMachineConfig) {
         this.evasionStateMachineConfig = evasionStateMachineConfig;
         return this;
      }

      public TestCaseBuilder addExistingMoveable() {
         requireNonNull(grid);
         MoveableBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(Positions.of(5, 5))
                     .build())
               .withHandler(moveable -> true)
               .build();
         return this;
      }

      public TestCaseBuilder addExistingMoveable(double health) {
         requireNonNull(grid);
         MoveableObstacleBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(Positions.of(5, 5))
                     .build())
               .withHealth(health)
               .withVelocity(5)
               .build();
         return this;
      }

      private TestCaseBuilder withMoveableAdder(MoveableAdder moveableAdder) {
         this.moveableAdder = moveableAdder;
         return this;
      }

      private TestCaseBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      private TestCaseBuilder addExistingObstacle() {
         requireNonNull(grid);
         ObstacleBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(Positions.of(5, 5))
                     .build())
               .build();
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }
   }
}
