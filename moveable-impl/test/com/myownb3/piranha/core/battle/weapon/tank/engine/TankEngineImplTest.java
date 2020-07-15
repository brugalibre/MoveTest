package com.myownb3.piranha.core.battle.weapon.tank.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;

class TankEngineImplTest {

   @Test
   void testMoveForward() {

      MoveableController moveableController = mock(MoveableController.class);

      // Given
      AudioClip audioClip = mock(AudioClip.class);
      TankEngineImpl tankEngineImpl = TankEngineBuilder.builder()
            .withMoveableController(moveableController)
            .withAudioClip(audioClip)
            .build();

      // When
      tankEngineImpl.moveForward();

      // Then
      verify(moveableController).leadMoveable();
      verify(audioClip).play();
   }

   @Test
   void testGetMoveable() {

      MoveableController moveableController = mock(MoveableController.class);
      EndPointMoveable expectedMoveable = mock(EndPointMoveable.class);
      Mockito.when(moveableController.getMoveable()).thenReturn(expectedMoveable);

      // Given
      TankEngineImpl tankEngineImpl = TankEngineBuilder.builder()
            .withMoveableController(moveableController)
            .build();

      // When
      Moveable actualMoveable = tankEngineImpl.getMoveable();

      // Then
      assertThat(actualMoveable, is(expectedMoveable));
   }
}
