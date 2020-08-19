package com.myownb3.piranha.core.battle.weapon.target;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Positions;

class TargetGridElementEvaluatorImplTest {

   @Test
   void testIsGridElementEnemy_NotBelligerentAtAll() {

      // Given
      GridElement gridElement = mock(GridElement.class);
      TargetGridElementEvaluatorImpl targetGridElementEvaluator = (TargetGridElementEvaluatorImpl) TargetGridElementEvaluatorBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDetector(mock(IDetector.class))
            .withGridElementEvaluator(mock(GridElementEvaluator.class))
            .build();
      // When
      boolean isActualGridElementEnemy = targetGridElementEvaluator.isGridElementEnemy(gridElement);

      // Then
      assertThat(isActualGridElementEnemy, is(false));
   }

   @Test
   void testIsGridElementEnemy_Enemy() {

      // Given
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withDestructionHelper(mock(DestructionHelper.class))
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(1))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withGrid(mock(Grid.class))
            .build();
      TargetGridElementEvaluatorImpl targetGridElementEvaluator = (TargetGridElementEvaluatorImpl) TargetGridElementEvaluatorBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDetector(mock(IDetector.class))
            .withGridElementEvaluator(mock(GridElementEvaluator.class))
            .build();
      // When
      boolean isActualGridElementEnemy = targetGridElementEvaluator.isGridElementEnemy(obstacleImpl);

      // Then
      assertThat(isActualGridElementEnemy, is(true));
   }

   @Test
   void testIsGridElementEnemy_Friend() {

      // Given
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDestructionHelper(mock(DestructionHelper.class))
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(1))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withGrid(mock(Grid.class))
            .build();
      TargetGridElementEvaluatorImpl targetGridElementEvaluator = (TargetGridElementEvaluatorImpl) TargetGridElementEvaluatorBuilder.builder()
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withDetector(mock(IDetector.class))
            .withGridElementEvaluator(mock(GridElementEvaluator.class))
            .build();
      // When
      boolean isActualGridElementEnemy = targetGridElementEvaluator.isGridElementEnemy(obstacleImpl);

      // Then
      assertThat(isActualGridElementEnemy, is(false));
   }

}
