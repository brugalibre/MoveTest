package com.myownb3.piranha.core.weapon.gun.shape;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;

public class GunShapeImpl extends AbstractShape implements GunShape {

   private Rectangle barrel;
   private Optional<Rectangle> muzzleBreakShapeOpt;

   protected GunShapeImpl(Rectangle barrel, Optional<Rectangle> muzzleBreakShape, List<PathSegment> path, Position center) {
      super(path, center);
      this.barrel = barrel;
      this.muzzleBreakShapeOpt = muzzleBreakShape;
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ((AbstractShape) barrel).setGridElement(gridElement);
      muzzleBreakShapeOpt.ifPresent(muzzleBreak -> ((AbstractShape) muzzleBreak).setGridElement(gridElement));
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      CollisionDetectionResult hasCollisionWithBarrel = barrel.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
      if (!hasCollisionWithBarrel.isCollision()) {
         return muzzleBreakShapeOpt.map(muzzleBreak -> muzzleBreak.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check))
               .orElse(new CollisionDetectionResultImpl(newPosition));
      }
      return hasCollisionWithBarrel;
   }

   @Override
   public Position getForemostPosition() {
      return muzzleBreakShapeOpt.map(muzzleBreak -> muzzleBreak.getForemostPosition())
            .orElse(barrel.getForemostPosition());
   }

   @Override
   public Position getRearmostPosition() {
      return barrel.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return muzzleBreakShapeOpt.map(muzzleBreak -> muzzleBreak.getDimensionRadius())
            .map(muzzleBreakDimension -> Math.max(muzzleBreakDimension, barrel.getDimensionRadius()))
            .orElse(barrel.getDimensionRadius());
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      if (muzzleBreakShapeOpt.isPresent()) {
         Rectangle muzzleBreak = muzzleBreakShapeOpt.get();
         Position barrelPos = Positions.movePositionBackward4Distance(position, muzzleBreak.getHeight() / 2);
         barrel.transform(barrelPos);

         Position muzzleBreakPos = Positions.movePositionForward4Distance(position, (muzzleBreak.getHeight() / 2));
         muzzleBreak.transform(muzzleBreakPos);
         this.path = ShapeUtil.combinePath(barrel, muzzleBreak);
      } else {
         barrel.transform(position);
         this.path = barrel.getPath();
      }
   }

   @Override
   public double getLength() {
      return muzzleBreakShapeOpt.map(muzzleBreak -> muzzleBreak.getDimensionRadius())
            .map(muzzleBreakDimension -> muzzleBreakDimension + barrel.getDimensionRadius())
            .orElse(barrel.getDimensionRadius());
   }

   @Override
   public Rectangle getBarrel() {
      return barrel;
   }

   @Override
   public Optional<Rectangle> getMuzzleBreak() {
      return muzzleBreakShapeOpt;
   }

   public static final class GunShapeBuilder {

      private Rectangle barrel;
      private Rectangle muzzleBreakShape;

      private GunShapeBuilder() {
         // private
      }

      public GunShapeBuilder withBarrel(Rectangle barrel) {
         this.barrel = barrel;
         return this;
      }

      public GunShapeBuilder withMuzzleBreak(Rectangle muzzleBreakShape) {
         this.muzzleBreakShape = muzzleBreakShape;
         return this;
      }

      public GunShapeImpl build() {
         return new GunShapeImpl(barrel, Optional.ofNullable(muzzleBreakShape), Collections.emptyList(), barrel.getCenter());
      }

      public static GunShapeBuilder builder() {
         return new GunShapeBuilder();
      }
   }
}
