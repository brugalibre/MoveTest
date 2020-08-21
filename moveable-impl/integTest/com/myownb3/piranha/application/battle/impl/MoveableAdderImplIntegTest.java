package com.myownb3.piranha.application.battle.impl;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.battle.MoveableAdder;
import com.myownb3.piranha.application.battle.impl.MoveableAdderImpl.MoveableAdderBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

class MoveableAdderImplIntegTest {

   @Test
   void testCheck4NewMoveables2Add_AddAndMoveOneMoveable() {

      // Given
      int counter = 50;
      int expectedAmountGridElements = 1;
      int amountOfNonMoveables = 0;
      int amountOfMoveables = expectedAmountGridElements;
      int moveableVelocity = 1;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withMoveableVelocity(moveableVelocity)
                  .withPadding(5)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .withProjectile()
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);
      assertThat(actualNewAddeMoveables.size(), is(expectedAmountGridElements));
      GridElement gridElement = actualNewAddeMoveables.get(0);
      Position expectedGridElemPos = evaluateMovedPos(gridElement.getPosition());
      assertThat(AutoMoveableController.class.isAssignableFrom(gridElement.getClass()), is(true));
      ((AutoMoveableController) gridElement).autodetect();

      // Then
      assertThat(gridElement.getPosition(), is(expectedGridElemPos));
   }

   private Position evaluateMovedPos(Position currentGridElemPos) {
      Direction direction = currentGridElemPos.getDirection();
      double forwardX = direction.getForwardX();
      double forwardY = direction.getForwardY();
      return Positions.of(currentGridElemPos.getX() + forwardX, currentGridElemPos.getY() + forwardY, currentGridElemPos.getZ());
   }

   @Test
   void testCheck4NewMoveables2Add_AddAndDestroyOneMoveable() {

      // Given
      int counter = 50;
      int expectedAmountGridElements = 1;
      int amountOfNonMoveables = 0;
      int amountOfMoveables = expectedAmountGridElements;
      int moveableVelocity = 5;

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withMoveableAdder(spy(MoveableAdderBuilder.builder()
                  .withCounter(counter)
                  .withAmountOfMoveables(amountOfMoveables)
                  .withAmountOfNonMoveables(amountOfNonMoveables)
                  .withMoveableVelocity(moveableVelocity)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withPadding(5)
                  .build()))
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinY(50)
                  .build())
            .withProjectile()
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build();

      // When
      List<GridElement> actualNewAddeMoveables = tcb.moveableAdder.check4NewMoveables2Add(tcb.grid, tcb.evasionStateMachineConfig);
      assertThat(actualNewAddeMoveables.size(), is(expectedAmountGridElements));
      GridElement gridElement = actualNewAddeMoveables.get(0);
      gridElement.onCollision(Collections.singletonList(tcb.projectile));

      // Then
      assertThat(tcb.grid.getAllGridElements(null).size(), is(expectedAmountGridElements));
   }

   private static class TestCaseBuilder {
      private ProjectileGridElement projectile;
      private EvasionStateMachineConfig evasionStateMachineConfig;
      private MoveableAdder moveableAdder;
      private Grid grid;

      private TestCaseBuilder() {
         // private 
      }

      public TestCaseBuilder withProjectile() {
         requireNonNull(grid);
         this.projectile = ProjectileGridElementBuilder.builder()
               .withGrid(grid)
               .withProjectile(ProjectileBuilder.builder()
                     .withDamage(Integer.MAX_VALUE)
                     .withShape(PositionShapeBuilder.builder()
                           .withPosition(Positions.of(5, 5))
                           .build())
                     .withProjectileConfig(mock(ProjectileConfig.class))
                     .withProjectileTypes(ProjectileTypes.BULLET)
                     .build())
               .withVelocity(5)
               .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
               .build();
         return this;
      }

      public TestCaseBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig evasionStateMachineConfig) {
         this.evasionStateMachineConfig = evasionStateMachineConfig;
         return this;
      }

      private TestCaseBuilder withMoveableAdder(MoveableAdderImpl moveableAdder) {
         this.moveableAdder = moveableAdder;
         return this;
      }

      private TestCaseBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }
   }
}
