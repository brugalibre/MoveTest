/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.grid.gridelement.DummyGridWall;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link MirrorGrid} is a {@link Grid} which mirrors any {@link Moveable}
 * as soon as this moveable is crossing the grids borders
 * 
 * @author Dominic
 *
 */
public class MirrorGrid extends DefaultGrid {

   private Wall mirrorGridWall;

   /**
    * 
    * @param maxX
    * @param maxY
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   private MirrorGrid(int maxX, int maxY, CollisionDetectionHandler collisionDetectionHandler) {
      super(maxX, maxY, 0, 0, collisionDetectionHandler);
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
      this.mirrorGridWall = new DummyGridWall(this, Positions.of(minX, minY));
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * com.myownb3.piranha.core.grid.DefaultGrid#moveForward(com.myownb3.piranha.core.grid.
    * Position)
    */
   @Override
   public Position moveForward(GridElement gridElement) {
      return moveForwardOrBackward(gridElement, gridElement.getForemostPosition(), gridElement1 -> super.moveForward(gridElement1));
   }

   @Override
   public Position moveBackward(GridElement gridElement) {
      return moveForwardOrBackward(gridElement, gridElement.getRearmostPosition(), gridElement1 -> super.moveBackward(gridElement1));
   }

   private Position moveForwardOrBackward(GridElement gridElement, Position foremostOrRearmostPos,
         Function<GridElement, Position> moveForwardOrdBackward) {
      MirroringRes mirroringRes = checkIfMirroring(gridElement, foremostOrRearmostPos);
      if (mirroringRes.isMirroring) {
         handleAfterMirroring(gridElement);
         return mirroringRes.mirroredPosition;
      }
      return moveForwardOrdBackward.apply(gridElement);
   }

   private MirroringRes checkIfMirroring(GridElement gridElement, Position foremostOrRearmostPos) {
      boolean isMirrored = false;
      Position mirroredPos = gridElement.getPosition();
      if (foremostOrRearmostPos.getX() <= minX || foremostOrRearmostPos.getX() >= maxX) {
         mirroredPos = mirroredPos.rotate(180 - 2 * mirroredPos.getDirection().getAngle());
         isMirrored = true;
      }
      if (foremostOrRearmostPos.getY() <= minY || foremostOrRearmostPos.getY() >= maxY) {
         mirroredPos = mirroredPos.rotate(360 - 2 * mirroredPos.getDirection().getAngle());
         isMirrored = true;
      }
      return new MirroringRes(mirroredPos, isMirrored);
   }

   private void handleAfterMirroring(GridElement gridElement) {
      if (gridElement instanceof CollisionSensitive) {
         ((CollisionSensitive) gridElement).onCollision(Collections.singletonList(mirrorGridWall));
      }
   }

   private static final class MirroringRes {
      private boolean isMirroring;
      private Position mirroredPosition;

      private MirroringRes(Position mirroredPosition, boolean isMirroring) {
         this.isMirroring = isMirroring;
         this.mirroredPosition = mirroredPosition;
      }
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

      public static MirrorGridBuilder builder(int maxX, int maxY) {
         return new MirrorGridBuilder()
               .withMaxX(maxX)
               .withMaxY(maxY);
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
