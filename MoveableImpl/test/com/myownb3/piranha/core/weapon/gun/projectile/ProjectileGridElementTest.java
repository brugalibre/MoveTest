package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.StroomTrooper;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;

class ProjectileGridElementTest {

   @Test
   void testAutoDetectable() {

      // Given
      ProjectileGridElement projectileGridElement = Mockito.spy(ProjectileGridElementBuilder.builder()
            .withDamage(10)
            .withGrid(mock(Grid.class))
            .withHealth(5)
            .withPosition(Positions.of(5, 5))
            .withShape(mock(AbstractShape.class))
            .withProjectile(ProjectileBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withVelocity(10)
            .build());

      // When
      projectileGridElement.autodetect();

      // Then
      verify(projectileGridElement).moveForward(10);
   }

   @Test
   void testBelligerentProjectile() {

      // Given
      ProjectileGridElement projectileGridElement = ProjectileGridElementBuilder.builder()
            .withDamage(10)
            .withGrid(mock(Grid.class))
            .withHealth(5)
            .withPosition(Positions.of(5, 5))
            .withShape(mock(AbstractShape.class))
            .withProjectile(ProjectileBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withVelocity(10)
            .build();
      Belligerent belligerent = new StroomTrooper();

      // When
      BelligerentParty actualBelligerentParty = projectileGridElement.getBelligerentParty();
      boolean actualIsEnemy = projectileGridElement.isEnemy(belligerent);

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.REBEL_ALLIANCE));
      assertThat(actualIsEnemy, is(true));
   }

   @Test
   void testIsProjectileDestroyed() {

      // Given

      // When
      boolean actualIsDestroyed = ProjectileGridElement.isDestroyed(null);

      // Then
      assertThat(actualIsDestroyed, is(true));
   }

   @Test
   void testGetDamage() {

      // Given
      double damageValue = 10;
      Projectile projectile = mockProjectile(damageValue);
      ProjectileGridElement projectileGridElement = ProjectileGridElementBuilder.builder()
            .withDamage(damageValue)
            .withGrid(mock(Grid.class))
            .withHealth(5)
            .withPosition(Positions.of(5, 5))
            .withShape(mock(AbstractShape.class))
            .withVelocity(10)
            .withProjectile(projectile)
            .build();

      // When
      Damage actualDamage = projectileGridElement.getDamage();

      // Then
      assertThat(actualDamage.getDamageValue(), is(damageValue));
   }

   private Projectile mockProjectile(double damageValue) {
      Projectile projectile = mock(Projectile.class);
      Damage damage = mockDamage(damageValue);
      when(projectile.getDamage()).thenReturn(damage);
      return projectile;
   }

   private Damage mockDamage(double damageValue) {
      Damage damage = mock(Damage.class);
      when(damage.getDamageValue()).thenReturn(damageValue);
      return damage;
   }
}
