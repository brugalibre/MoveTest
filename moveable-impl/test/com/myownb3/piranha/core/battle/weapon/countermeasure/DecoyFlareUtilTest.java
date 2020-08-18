package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.AutoMoveable;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.moveables.types.AutoMoveableTypes;

class DecoyFlareUtilTest {

   @Test
   void testIsDecoyFlareGridElement_IsAutoMoveableDecoyFlare() {

      // Given
      AutoMoveable autoMoveable = mock(AutoMoveable.class);
      when(autoMoveable.getAutoMoveableTypes()).thenReturn(AutoMoveableTypes.DECOY_FLARE);
      boolean expectedIsDecoyFlareGridElement = true;

      // When
      boolean actualIsDecoyFlareGridElement = DecoyFlareUtil.isDecoyFlareGridElement(autoMoveable);

      // Then
      assertThat(actualIsDecoyFlareGridElement, is(expectedIsDecoyFlareGridElement));
   }

   @Test
   void testIsDecoyFlareGridElement_IsAutoMoveableControllerDecoyFlare() {

      // Given
      AutoMoveableController autoMoveableController = mock(AutoMoveableController.class);
      when(autoMoveableController.getAutoMoveableTypes()).thenReturn(AutoMoveableTypes.DECOY_FLARE);
      boolean expectedIsDecoyFlareGridElement = true;

      // When
      boolean actualIsDecoyFlareGridElement = DecoyFlareUtil.isDecoyFlareGridElement(autoMoveableController);

      // Then
      assertThat(actualIsDecoyFlareGridElement, is(expectedIsDecoyFlareGridElement));
   }

   @Test
   void testIsDecoyFlareGridElement_IsAutoMoveableButNotDecoyFlare() {

      // Given
      AutoMoveable autoMoveable = mock(AutoMoveable.class);
      boolean expectedIsDecoyFlareGridElement = false;

      // When
      boolean actualIsDecoyFlareGridElement = DecoyFlareUtil.isDecoyFlareGridElement(autoMoveable);

      // Then
      assertThat(actualIsDecoyFlareGridElement, is(expectedIsDecoyFlareGridElement));
   }

   @Test
   void testIsDecoyFlareGridElement_IsAutoMoveableControllerButNotDecoyFlare() {

      // Given
      AutoMoveableController autoMoveableController = mock(AutoMoveableController.class);
      boolean expectedIsDecoyFlareGridElement = false;

      // When
      boolean actualIsDecoyFlareGridElement = DecoyFlareUtil.isDecoyFlareGridElement(autoMoveableController);

      // Then
      assertThat(actualIsDecoyFlareGridElement, is(expectedIsDecoyFlareGridElement));
   }

   @Test
   void testIsDecoyFlareGridElement_IsNotDecoyFlare() {

      // Given
      GridElement gridElement = mock(GridElement.class);
      boolean expectedIsDecoyFlareGridElement = false;

      // When
      boolean actualIsDecoyFlareGridElement = DecoyFlareUtil.isDecoyFlareGridElement(gridElement);

      // Then
      assertThat(actualIsDecoyFlareGridElement, is(expectedIsDecoyFlareGridElement));
   }

}
