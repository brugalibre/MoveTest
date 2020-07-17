/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static java.util.Objects.isNull;

import java.util.Objects;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.lineshape.ImmutableLineShape.ImmutableLineShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement.WallGridElementBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link MirrorGrid} is a {@link Grid} which mirrors any {@link Moveable}
 * as soon as this moveable is crossing the grids borders
 * 
 * @author Dominic
 *
 */
public class MirrorGrid extends DefaultGrid {

   /**
    * 
    * @param maxX
    * @param maxY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private MirrorGrid(int maxX, int maxY, CollisionDetectionHandler collisionDetectionHandler) {
      this(maxX, maxY, 0, 0, collisionDetectionHandler);
   }

   /**
    * 
    * @param maxX
    * @param maxY
    * @param minX
    * @param minY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private MirrorGrid(int maxX, int maxY, int minX, int minY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, minX, minY, collisionDetectionHandler);
      buildLowerWallGridElement();
      buildUpperWallGridElement();
      buildLeftWallGridElement();
      buildRightWallGridElement();
   }

   private void buildRightWallGridElement() {
      Position beginPos = Positions.of(Directions.O, maxX, minY, 0);
      Position endPos = Positions.of(Directions.O, maxX, maxY, 0);
      buildWallGridElement(beginPos, endPos);
   }

   private void buildLeftWallGridElement() {
      Position beginPos = Positions.of(Directions.O, minX, minY, 0);
      Position endPos = Positions.of(Directions.O, minX, maxY, 0);
      buildWallGridElement(beginPos, endPos);
   }

   private void buildUpperWallGridElement() {
      Position beginPos = Positions.of(Directions.O, minX, maxY, 0);
      Position endPos = Positions.of(Directions.O, maxX, maxY, 0);
      buildWallGridElement(beginPos, endPos);
   }

   private void buildLowerWallGridElement() {
      Position beginPos = Positions.of(Directions.O, minX, minY, 0);
      Position endPos = Positions.of(Directions.O, maxX, minY, 0);
      buildWallGridElement(beginPos, endPos);
   }

   private void buildWallGridElement(Position beginPos, Position endPos) {
      WallGridElementBuilder.builder()
            .withGrid(this)
            .withShape(ImmutableLineShapeBuilder.builder()
                  .withBeginPosition(beginPos)
                  .withEndPosition(endPos)
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(1))
            .build();
   }

   /**
    * The {@link MirrorGridBuilder} helps to build a {@link MirrorGrid}
    * 
    * @author Dominic
    *
    */
   public static class MirrorGridBuilder extends AbstractGridBuilder<MirrorGrid> {

      public static MirrorGridBuilder builder() {
         return new MirrorGridBuilder()
               .withMaxX(10)
               .withMaxY(10);
      }

      /**
       * Creates a new {@link MirrorGrid}
       * 
       * @return a new {@link MirrorGrid}
       */
      @Override
      public MirrorGrid build() {
         Objects.requireNonNull(maxX, "We need a max x value!");
         Objects.requireNonNull(maxY, "We need a max y value!");
         MirrorGrid mirrorGrid;
         setDefaultCollisionDetectionHandlerIfNull();
         if (isNull(minX) || isNull(minY)) {
            mirrorGrid = new MirrorGrid(maxX, maxY, collisionDetectionHandler);
         } else {
            mirrorGrid = new MirrorGrid(maxX, maxY, minX, minY, collisionDetectionHandler);
         }
         return mirrorGrid;
      }
   }
}
