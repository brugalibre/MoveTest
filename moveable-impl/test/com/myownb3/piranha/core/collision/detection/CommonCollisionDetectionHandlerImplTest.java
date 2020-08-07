package com.myownb3.piranha.core.collision.detection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.detection.handler.CommonCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class CommonCollisionDetectionHandlerImplTest {

   @Test
   void testHandleCollisionVerifyReturnValue() {

      // Given
      CommonCollisionDetectionHandlerImpl collisionDetectionHandlerImpl = new CommonCollisionDetectionHandlerImpl();
      Position newPosition = Positions.of(5, 5);
      Position currentObstaclePos = Positions.of(0, 0);
      Obstacle obstacle = mock(Obstacle.class);
      ObstacleImpl movedObstacle = mock(ObstacleImpl.class);
      when(movedObstacle.getPosition()).thenReturn(currentObstaclePos);

      CollisionGridElement collisionGridElement = CollisionGridElementImpl.of(mock(Intersection.class), obstacle);

      // When
      CollisionDetectionResult collisionDetectionResult =
            collisionDetectionHandlerImpl.handleCollision(Collections.singletonList(collisionGridElement), movedObstacle, newPosition);

      // Then
      assertThat(collisionDetectionResult.isCollision(), is(true));
      assertThat(collisionDetectionResult.getMovedPosition(), is(currentObstaclePos));
   }

   @Test
   void testHandleCollision() {

      // Given
      CommonCollisionDetectionHandlerImpl collisionDetectionHandlerImpl = new CommonCollisionDetectionHandlerImpl();
      Position newPosition = Positions.of(5, 5);
      Obstacle obstacle = mock(Obstacle.class);
      ObstacleImpl movedObstacle = mock(ObstacleImpl.class);

      CollisionGridElement collisionGridElement = CollisionGridElementImpl.of(mock(Intersection.class), obstacle);

      // When
      collisionDetectionHandlerImpl.handleCollision(Collections.singletonList(collisionGridElement), movedObstacle, newPosition);

      // Then
      verify(obstacle).onCollision(eq(Collections.singletonList(movedObstacle)));
   }

   @Test
   void testHandleNoCollision() {

      // Given
      CommonCollisionDetectionHandlerImpl collisionDetectionHandlerImpl = new CommonCollisionDetectionHandlerImpl();
      Position newPosition = Positions.of(5, 5);
      ObstacleImpl obstacle = mock(ObstacleImpl.class);

      // When
      collisionDetectionHandlerImpl.handleCollision(Collections.emptyList(), obstacle, newPosition);

      // Then
      verify(obstacle).onCollision(eq(Collections.emptyList()));
   }
}
