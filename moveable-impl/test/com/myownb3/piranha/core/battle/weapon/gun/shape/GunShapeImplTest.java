package com.myownb3.piranha.core.battle.weapon.gun.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class GunShapeImplTest {

   @Test
   void testTransformWithoutMazzleBreak() {

      // Given
      Position newPosition = Positions.of(10, 10);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .build();

      // When
      gunShapeImpl.transform(newPosition);

      // Then
      assertThat(gunShapeImpl.getBarrel().getCenter(), is(newPosition));
   }

   @Test
   void testTransformWithMazzleBreak() {

      // Given
      Position newPosition = Positions.of(10, 10);
      Position newBarrelPosition = Positions.of(10, 7.5);
      Position newMuzzleBreakPosition = Positions.of(10, 12.5);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .withMuzzleBreak(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 15))
                  .withHeight(5)
                  .withWidth(5)
                  .build())
            .build();

      // When
      gunShapeImpl.transform(newPosition);

      // Then
      assertThat(gunShapeImpl.getBarrel().getCenter(), is(newBarrelPosition));
      assertThat(gunShapeImpl.getMuzzleBreak().get().getCenter(), is(newMuzzleBreakPosition));
   }

   @Test
   void testGetLengthWithMazzleBreak() {

      // Given
      int barrelHeight = 10;
      int muzzleBreakHeight = 5;
      double expectedLenght = barrelHeight + muzzleBreakHeight;
      Position newPosition = Positions.of(barrelHeight, barrelHeight);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .withMuzzleBreak(RectangleBuilder.builder()
                  .withCenter(Positions.of(barrelHeight, 15))
                  .withHeight(muzzleBreakHeight)
                  .withWidth(muzzleBreakHeight)
                  .build())
            .build();

      // When
      gunShapeImpl.transform(newPosition);

      // Then
      assertThat(gunShapeImpl.getLength(), is(expectedLenght));
   }

   @Test
   void testGetLengthWithoutMazzleBreak() {

      // Given
      int barrelHeight = 10;
      double expectedLenght = barrelHeight;
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .build();

      // When
      double actualLength = gunShapeImpl.getLength();

      // Then
      assertThat(actualLength, is(expectedLenght));
   }

   @Test
   void testGetDimensionRadiusWithMazzleBreak() {

      // Given
      int barrelHeight = 10;
      double muzzleBreakHeight = 5;
      double expectedDimensionRadius = barrelHeight;
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .withMuzzleBreak(RectangleBuilder.builder()
                  .withCenter(Positions.of(barrelHeight, 15))
                  .withHeight(muzzleBreakHeight)
                  .withWidth(muzzleBreakHeight)
                  .build())
            .build();

      // When
      double actualDimensionRadius = gunShapeImpl.getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(expectedDimensionRadius));
   }

   @Test
   void testGetDimensionRadius_WithoutMazzleBreak() {

      // Given
      int barrelHeight = 10;
      double expectedDimensionRadius = barrelHeight;
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .build();

      // When
      double actualDimensionRadius = gunShapeImpl.getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(expectedDimensionRadius));
   }

   @Test
   void testCheck4Collision_WithBarrelCollision() {
      // Given
      Rectangle muzzleBreak = mock(Rectangle.class);
      Rectangle barrel = mock(Rectangle.class);
      when(barrel.getCenter()).thenReturn(Positions.of(5, 5));

      when(barrel.check4Collision(any(), any(), any())).thenReturn(new CollisionDetectionResultImpl(true, Positions.of(5, 5)));
      when(muzzleBreak.check4Collision(any(), any(), any())).thenReturn(new CollisionDetectionResultImpl(true, Positions.of(15, 15)));
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(barrel)
            .withMuzzleBreak(muzzleBreak)
            .build();

      // When
      CollisionDetectionResult actualColRes =
            gunShapeImpl.check4Collision(mock(CollisionDetectionHandler.class), Positions.of(5, 5), Collections.emptyList());

      // Then
      assertThat(actualColRes.getMovedPosition(), is(Positions.of(5, 5)));
   }

   @Test
   void testCheck4Collision_WithMazzleBreakCollision() {
      // Given
      Rectangle muzzleBreak = mock(Rectangle.class);
      Rectangle barrel = mock(Rectangle.class);
      when(barrel.getCenter()).thenReturn(Positions.of(5, 5));

      when(barrel.check4Collision(any(), any(), any())).thenReturn(new CollisionDetectionResultImpl(false, Positions.of(5, 5)));
      when(muzzleBreak.check4Collision(any(), any(), any())).thenReturn(new CollisionDetectionResultImpl(true, Positions.of(15, 15)));
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(barrel)
            .withMuzzleBreak(muzzleBreak)
            .build();

      // When
      CollisionDetectionResult actualColRes =
            gunShapeImpl.check4Collision(mock(CollisionDetectionHandler.class), Positions.of(6, 6), Collections.emptyList());

      // Then
      assertThat(actualColRes.getMovedPosition(), is(Positions.of(15, 15)));
   }

   @Test
   void testCheck4Collision_WithoutMazzleBreak_WithoutCollision() {
      // Given
      Rectangle barrel = mock(Rectangle.class);
      when(barrel.getCenter()).thenReturn(Positions.of(5, 5));

      when(barrel.check4Collision(any(), any(), any())).thenReturn(new CollisionDetectionResultImpl(false, Positions.of(15, 15)));
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(barrel)
            .build();

      // When
      CollisionDetectionResult actualColRes =
            gunShapeImpl.check4Collision(mock(CollisionDetectionHandler.class), Positions.of(6, 6), Collections.emptyList());

      // Then
      assertThat(actualColRes.getMovedPosition(), is(Positions.of(6, 6)));
   }

   @Test
   void testGetForemostPosition_WithMuzzleBreak() {
      // Given
      int barrelHeight = 10;
      double muzzleBreakHeight = 5;
      Position expectedForemostPosition = Positions.of(10, 17.5);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .withMuzzleBreak(RectangleBuilder.builder()
                  .withCenter(Positions.of(barrelHeight, 15))
                  .withHeight(muzzleBreakHeight)
                  .withWidth(muzzleBreakHeight)
                  .build())
            .build();

      // When
      Position actualForemostPosition = gunShapeImpl.getForemostPosition();

      // Then
      assertThat(actualForemostPosition, is(expectedForemostPosition));
   }

   @Test
   void testGetForemostPosition_WithoutMuzzleBreak() {
      // Given
      int barrelHeight = 10;
      Position expectedForemostPosition = Positions.of(10, 15);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(barrelHeight)
                  .withWidth(barrelHeight)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .build();

      // When
      Position actualForemostPosition = gunShapeImpl.getForemostPosition();

      // Then
      assertThat(actualForemostPosition, is(expectedForemostPosition));
   }

   @Test
   void testGetRearmostPosition() {
      // Given
      Position expectedForemostPosition = Positions.of(10, 5);
      GunShapeImpl gunShapeImpl = GunShapeBuilder.builder()
            .withBarrel(RectangleBuilder.builder()
                  .withCenter(Positions.of(10, 10))
                  .withHeight(10)
                  .withWidth(10)
                  .withOrientation(Orientation.VERTICAL)
                  .build())
            .build();

      // When
      Position actualRearmostPosition = gunShapeImpl.getRearmostPosition();

      // Then
      assertThat(actualRearmostPosition, is(expectedForemostPosition));
   }
}
