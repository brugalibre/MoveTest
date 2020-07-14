package com.myownb3.piranha.core.grid.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

class ProjectileGridElementsFilterTest {

   @Test
   void testNegativeSinceNoProjectile() {
      // Given
      GridElement gridElement = mock(GridElement.class);
      ProjectileGridElementsFilter filter = new ProjectileGridElementsFilter();

      // When
      boolean actualTest = filter.test(gridElement);

      // Then
      assertThat(actualTest, is(false));
   }

   @Test
   void testPositiveSinceProjectile() {
      // Given
      ProjectileGridElement gridElement = mock(ProjectileGridElement.class);
      ProjectileGridElementsFilter filter = new ProjectileGridElementsFilter();

      // When
      boolean actualTest = filter.test(gridElement);

      // Then
      assertThat(actualTest, is(true));
   }
}
