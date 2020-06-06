package com.myownb3.piranha.core.weapon.guncarriage;

import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.util.MathUtil;

public class AbstractGunCarriage implements GunCarriage {

   private Shape shape;
   private Gun gun;
   private double rotationSpeed;

   protected AbstractGunCarriage(Shape shape, Gun gun, double rotationSpeed) {
      this.shape = requireNonNull(shape);
      this.gun = requireNonNull(gun);
      this.rotationSpeed = abs(rotationSpeed);
      this.gun.evalAndSetGunPosition(shape.getForemostPosition());
   }

   @Override
   public void fire() {
      gun.fire();
   }

   @Override
   public void aimTargetPos(Position targetPos) {
      double angleDiff = getPosition().calcAngleRelativeTo(targetPos);
      rotate(angleDiff);
   }

   @Override
   public void turn2ParkPosition(double parkingAngle) {
      double angleDiff = parkingAngle - getPosition().getDirection().getAngle();
      rotate(angleDiff);
   }

   private void rotate(double angleDiff) {
      double angle2Turn;
      int signum = MathUtil.getSignum(angleDiff);
      if (abs(angleDiff) > rotationSpeed) {
         angle2Turn = signum * rotationSpeed;
      } else /*if (abs(angleDiff) > 0)*/ {
         angle2Turn = angleDiff;
      }
      Position position = getPosition().rotate(angle2Turn);
      evalAndSetPosition(position);
   }

   @Override
   public boolean isInParkingPosition(double parkingAngle) {
      return getPosition().getDirection().getAngle() == parkingAngle;
   }

   @Override
   public boolean hasTargetLocked(Position acquiredTargetPos) {
      return getPosition().calcAngleRelativeTo(acquiredTargetPos) == 0;
   }

   @Override
   public void evalAndSetPosition(Position position) {
      shape.transform(position);
      gun.evalAndSetGunPosition(shape.getForemostPosition());
   }

   private Position getPosition() {
      return shape.getCenter();
   }

   @Override
   public Shape getShape() {
      return shape;
   }

   @Override
   public Gun getGun() {
      return gun;
   }

   public static abstract class AbstractGunCarriageBuilder<T extends AbstractGunCarriage> {

      protected Shape shape;
      protected Gun gun;
      protected double rotationSpeed;

      protected AbstractGunCarriageBuilder() {
         // private
      }

      public AbstractGunCarriageBuilder<T> withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public AbstractGunCarriageBuilder<T> withGun(Gun gun) {
         this.gun = gun;
         return this;
      }

      public AbstractGunCarriageBuilder<T> withRotationSpeed(double rotationSpeed) {
         this.rotationSpeed = rotationSpeed;
         return this;
      }

      public abstract T build();
   }
}
