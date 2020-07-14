package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AutoMoveable;

class DecoyFlareFactoryTest {

   private Grid grid = mock(Grid.class);

   @BeforeEach
   public void tearDown() {
      ProjectileFactory.INSTANCE.deregisterGrid();
   }

   @Test
   void testCreateAndAssertDecoyFlare() {
      // Given
      DecoyFlareFactory.INSTANCE.registerGrid(grid);
      double dimensionRadius = 5;
      int velocity = 4;
      Position decoyFlarePosition = Positions.of(5, 5);
      DecoyFlareConfig decoyFlareConfig = DecoyFlareConfigBuilder.builder()
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(dimensionRadius))
            .withVelocity(4)
            .build();

      // When
      AutoMoveable autoMoveable = DecoyFlareFactory.INSTANCE.createDecoyFlare(decoyFlarePosition, decoyFlareConfig);

      // Then
      assertThat(autoMoveable.getVelocity(), is(velocity));
      assertThat(autoMoveable.getPosition(), is(decoyFlarePosition));
      assertThat(autoMoveable.getDimensionInfo().getDimensionRadius(), is(dimensionRadius));
   }

}
