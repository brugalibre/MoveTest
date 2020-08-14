package com.myownb3.piranha.core.battle.weapon.gun.shape;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;

public class GunShapeImpl extends AbstractShape implements GunShape {

   private static final long serialVersionUID = -5552409576828255258L;
   private Rectangle barrel;
   private Rectangle muzzleBrakeShape;

   protected GunShapeImpl(Rectangle barrel, Rectangle muzzleBrakeShape, List<PathSegment> path, Position center) {
      super(path, center);
      this.barrel = barrel;
      this.muzzleBrakeShape = muzzleBrakeShape;
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ((AbstractShape) barrel).setGridElement(gridElement);
      getMuzzleBrake().ifPresent(muzzleBrake -> ((AbstractShape) muzzleBrake).setGridElement(gridElement));
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      CollisionDetectionResult hasCollisionWithBarrel = barrel.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
      if (!hasCollisionWithBarrel.isCollision()) {
         return getMuzzleBrake().map(muzzleBrake -> muzzleBrake.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check))
               .orElse(new CollisionDetectionResultImpl(newPosition));
      }
      return hasCollisionWithBarrel;
   }

   @Override
   public Position getForemostPosition() {
      return getMuzzleBrake().map(Shape::getForemostPosition)
            .orElse(barrel.getForemostPosition());
   }

   @Override
   public Position getRearmostPosition() {
      return barrel.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return getMuzzleBrake().map(Shape::getDimensionRadius)
            .map(muzzleBrakeDimension -> Math.max(muzzleBrakeDimension, barrel.getDimensionRadius()))
            .orElse(barrel.getDimensionRadius());
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      if (getMuzzleBrake().isPresent()) {
         Rectangle muzzleBrake = getMuzzleBrake().get();
         Position barrelPos = position.movePositionBackward4Distance(muzzleBrake.getHeight() / 2);
         barrel.transform(barrelPos);

         Position muzzleBrakePos = position.movePositionForward4Distance((muzzleBrake.getHeight() / 2));
         muzzleBrake.transform(muzzleBrakePos);
         this.path = ShapeUtil.combinePath(barrel, muzzleBrake);
      } else {
         barrel.transform(position);
         this.path = barrel.getPath();
      }
   }

   @Override
   public double getLength() {
      return getMuzzleBrake().map(Shape::getDimensionRadius)
            .map(muzzleBrakeDimension -> muzzleBrakeDimension + barrel.getDimensionRadius())
            .orElse(barrel.getDimensionRadius());
   }

   @Override
   public Rectangle getBarrel() {
      return barrel;
   }

   @Override
   public Optional<Rectangle> getMuzzleBrake() {
      return Optional.ofNullable(muzzleBrakeShape);
   }

   public static final class GunShapeBuilder {

      private Rectangle barrel;
      private Rectangle muzzleBrakeShape;

      private GunShapeBuilder() {
         // private
      }

      public GunShapeBuilder withBarrel(Rectangle barrel) {
         this.barrel = barrel;
         return this;
      }

      public GunShapeBuilder withMuzzleBrake(Rectangle muzzleBrakeShape) {
         this.muzzleBrakeShape = muzzleBrakeShape;
         return this;
      }

      public GunShapeImpl build() {
         return new GunShapeImpl(barrel, muzzleBrakeShape, Collections.emptyList(), barrel.getCenter());
      }

      public static GunShapeBuilder builder() {
         return new GunShapeBuilder();
      }
   }
}
