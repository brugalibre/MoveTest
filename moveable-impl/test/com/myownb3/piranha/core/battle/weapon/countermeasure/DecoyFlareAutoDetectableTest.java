package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.destruction.Health;
import com.myownb3.piranha.core.battle.destruction.HealthImpl;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;

class DecoyFlareAutoDetectableTest {

   @Test
   void testGetDecoyFlareAutoDetectable() {

      // Given
      double targetHeight = 0.0;
      int timeToLife = 100;
      Health health = HealthImpl.of(1);
      Shape decoyShape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5, 5))
            .build();
      AutoDetectable decoyFlareAutoDetectable = DecoyFlareAutoDetectable.getDecoyFlareAutoDetectable(decoyShape, health, targetHeight, timeToLife);

      // When
      for (int i = 0; i <= timeToLife; i++) {
         decoyFlareAutoDetectable.autodetect();
      }

      // Then
      assertThat(health.isHealthy(), is(false));
   }
}
