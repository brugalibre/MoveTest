package com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.TIEFighterShape;
import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape.TIEFighterShapeImpl.TIEFighterShapeBuilder;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class TIEFighterShapeImplTest {

   @Test
   void testTransform() {

      // Given
      Position gridElementPos = Positions.of(5, 5, 0);
      Position newGridElementPos = Positions.of(15, 15, 0);
      Position expectedRightWingCenter = Positions.of(26.7, 15, 0);
      Position expectedLeftWingCenter = Positions.of(3.3, 15, 0).rotate(90);
      int gridElementRadius = 10;
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();

      // When
      tieFighterShape.transform(newGridElementPos);

      // Then
      assertThat(tieFighterShape.getCenter(), is(newGridElementPos));
      assertThat(tieFighterShape.getRightWing().getCenter(), is(expectedRightWingCenter));
      assertThat(tieFighterShape.getLeftWing().getCenter(), is(expectedLeftWingCenter));
   }

   @Test
   void testSetGridElement() {
      GridElement gridElement = Mockito.mock(GridElement.class);
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(spy(CircleBuilder.builder()
                  .withRadius(10)
                  .withAmountOfPoints(20)
                  .withCenter(Positions.of(5, 5, 0))
                  .build()))
            .build();
      // When
      ((AbstractShape) tieFighterShape).setGridElement(gridElement);

      // Then
      verify(((AbstractShape) tieFighterShape.getBallCockpit())).setGridElement(eq(gridElement));

   }

   @Test
   void testCheck4Collision() {
      // Given
      Position gridElementPos = Positions.of(5, 5, 0);
      int gridElementRadius = 10;
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();

      // When
      CollisionDetectionHandler collisionDetectionHandler = mock(CollisionDetectionHandler.class);
      tieFighterShape.check4Collision(collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(collisionDetectionHandler, never()).handleCollision(any(), any(), any());
   }


   @Test
   void testGetForemostPosition() {
      Position gridElementPos = Positions.of(5, 5, 0);
      int gridElementRadius = 10;
      CircleImpl circle = CircleBuilder.builder()
            .withRadius(gridElementRadius)
            .withAmountOfPoints(20)
            .withCenter(gridElementPos)
            .build();
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(circle)
            .build();

      // When
      Position actualForemostPosition = tieFighterShape.getForemostPosition();

      // Then
      assertThat(actualForemostPosition, is(circle.getForemostPosition()));
   }

   @Test
   void testGetRearmostPosition() {
      Position gridElementPos = Positions.of(5, 5, 0);
      int gridElementRadius = 10;
      CircleImpl circle = CircleBuilder.builder()
            .withRadius(gridElementRadius)
            .withAmountOfPoints(20)
            .withCenter(gridElementPos)
            .build();
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(circle)
            .build();

      // When
      Position actualRearmostPosition = tieFighterShape.getRearmostPosition();

      // Then
      assertThat(actualRearmostPosition, is(circle.getRearmostPosition()));
   }

   @Test
   void testGetDimensionRadius() {
      Position gridElementPos = Positions.of(5, 5, 0);
      int gridElementRadius = 10;
      double expectedDimensionRadius = 16.6666666666666666666666666;
      TIEFighterShape tieFighterShape = TIEFighterShapeBuilder.builder()
            .withBallCockpit(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();

      // When
      double actualDimensionRadius = tieFighterShape.getDimensionRadius();

      // Then
      assertThat(actualDimensionRadius, is(expectedDimensionRadius));
   }
}
