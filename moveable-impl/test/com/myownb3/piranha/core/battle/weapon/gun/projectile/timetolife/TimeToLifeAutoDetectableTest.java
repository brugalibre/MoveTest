package com.myownb3.piranha.core.battle.weapon.gun.projectile.timetolife;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.destruction.Health;
import com.myownb3.piranha.core.battle.destruction.HealthImpl;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;

class TimeToLifeAutoDetectableTest {

   @Test
   void testGetTimeToLifeAutoDetectable() {

      // Given
      int timeToLife = 100;
      double healthValue = 9990;
      Health health = HealthImpl.of(healthValue);
      AutoDetectable timeToLifeAutoDetectable = TimeToLifeAutoDetectable.getTimeToLifeAutoDetectable(health, timeToLife);

      // When
      for (int i = 0; i <= timeToLife; i++) {
         timeToLifeAutoDetectable.autodetect();
      }

      // Then
      assertThat(health.isHealthy(), is(false));
   }
}
