package com.myownb3.piranha.core.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape.TIEFighterShapeImpl.TIEFighterShapeBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyEndPoinMoveable;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController.AutoMoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.types.AutoMoveableTypes;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;

class AutoMoveableControllerTest {
   @Test
   void testGetAutoMoveableTypes() {

      // Given
      AutoMoveableTypes autoMoveableTypes = AutoMoveableTypes.DECOY_FLARE;
      Grid grid = mock(Grid.class);
      Position gridElementPos = Positions.of(5, 5);
      MoveableController moveableController = mock(MoveableController.class);
      DestructionHelper destructionHelper = mock(DestructionHelper.class);

      // When
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withMoveableController(moveableController)
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(1)
                  .withOnDestroyedCallbackHandler(() -> {
                  })
                  .build())
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(1)
                  .build())
            .withDestructionHelper(destructionHelper)
            .withGrid(grid)
            .withAutoMoveableTypes(autoMoveableTypes)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .build())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(1)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(1)
            .build();

      // Then
      assertThat(autoMoveableController.getAutoMoveableTypes(), is(autoMoveableTypes));
   }

   @Test
   void testDefaultAutoDetectable() {

      // Given
      Grid grid = mock(Grid.class);
      Position gridElementPos = Positions.of(5, 5);
      MoveableController moveableController = mock(MoveableController.class);
      DestructionHelper destructionHelper = mock(DestructionHelper.class);
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withMoveableController(moveableController)
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(1)
                  .withOnDestroyedCallbackHandler(() -> {
                  })
                  .build())
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(1)
                  .build())
            .withDestructionHelper(destructionHelper)
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .build())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(1)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(1)
            .build();

      // When
      autoMoveableController.autodetect();

      // Then
      verify(moveableController).leadMoveable();
   }

   @Test
   void testDestructionHelper() {

      // Given
      Grid grid = mock(Grid.class);
      Position gridElementPos = Positions.of(5, 5);
      MoveableController moveableController = mock(MoveableController.class);
      DestructionHelper destructionHelper = mock(DestructionHelper.class);
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(1)
                  .withOnDestroyedCallbackHandler(() -> {
                  })
                  .build())
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(1)
                  .build())
            .withDestructionHelper(destructionHelper)
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .build())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(1)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(1)
            .withMoveableController(moveableController)
            .build();

      // When
      autoMoveableController.isDestroyed();
      autoMoveableController.getDamage();
      autoMoveableController.onCollision(Collections.emptyList());

      // Then
      verify(destructionHelper).isDestroyed();
      verify(destructionHelper).getDamage();
      verify(destructionHelper).onCollision(eq(Collections.emptyList()));
   }

   @Test
   void testAutodetect() {

      // Given
      Grid grid = mock(Grid.class);
      Position gridElementPos = Positions.of(5, 5);
      MoveableController moveableController = mock(MoveableController.class);
      AutoDetectable autoDetectableDelegate = mock(AutoDetectable.class);
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(1)
                  .withOnDestroyedCallbackHandler(() -> {
                  })
                  .build())
            .withAutoDetectable(autoDetectableDelegate)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(1)
                  .build())
            .withMoveableController(moveableController)
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .build())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(1)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(1)
            .build();

      // When
      autoMoveableController.autodetect();

      // Then
      verify(moveableController).leadMoveable();
      verify(autoDetectableDelegate).autodetect();
   }

   @Test
   void testAutoMoveableController() {

      // Given
      int moveableVelocity = 10;
      int gridElementRadius = 10;
      double expectedGridElementRadius = 16.666666666666668;
      Grid grid = mock(Grid.class);
      List<EndPosition> endPosList = Collections.singletonList(EndPositions.of(5, 5));
      Position gridElementPos = Positions.of(5, 5);
      LazyEndPoinMoveable lazyEndPoinMoveable = new LazyEndPoinMoveable();

      // When
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(moveableVelocity)
                  .withDestroyedAudioClip(AudioClipBuilder.builder()
                        .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                        .build())
                  .withOnDestroyedCallbackHandler(() -> {
                     grid.remove(lazyEndPoinMoveable.getGridElement());
                  })
                  .build())
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(gridElementRadius)
                  .build())
            .withMoveableController(MoveableControllerBuilder.builder()
                  .withEndPositions(endPosList)
                  .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                  .withLazyMoveable(() -> lazyEndPoinMoveable.getGridElement())
                  .build())
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .build())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(gridElementRadius)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(moveableVelocity)
            .build();
      lazyEndPoinMoveable.setGridElement(autoMoveableController);

      // Then
      assertThat(lazyEndPoinMoveable.getGridElement().getPosition(), is(gridElementPos));
      assertThat(lazyEndPoinMoveable.getGridElement().getVelocity(), is(moveableVelocity));
      assertThat(lazyEndPoinMoveable.getGridElement().getDimensionInfo().getDimensionRadius(), is(expectedGridElementRadius));
   }

}
