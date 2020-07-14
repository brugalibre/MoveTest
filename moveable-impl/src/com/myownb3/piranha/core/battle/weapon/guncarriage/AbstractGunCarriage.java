package com.myownb3.piranha.core.battle.weapon.guncarriage;

import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.Orientation2PositionHelper;

public class AbstractGunCarriage implements GunCarriage {

   private static final long serialVersionUID = -1724494190061272992L;
   private Shape shape;
   private Gun gun;
   private double rotationSpeed;
   private transient Orientation2PositionHelper helper;

   protected AbstractGunCarriage(Shape shape, Gun gun, double rotationSpeed) {
      this.shape = requireNonNull(shape);
      this.gun = requireNonNull(gun);
      this.rotationSpeed = abs(rotationSpeed);
      this.gun.evalAndSetGunPosition(shape.getForemostPosition());
      this.helper = new Orientation2PositionHelper();
   }

   @Override
   public void fire() {
      gun.fire();
   }

   @Override
   public void aimTargetPos(Position targetPos) {
      double angleDiff = getPosition().calcAngleRelativeTo(targetPos);
      double angle2Turn = helper.getAngle2Turn(angleDiff, rotationSpeed);
      rotate(angle2Turn);
   }

   @Override
   public void turn2ParkPosition(double parkingAngle) {
      double angleDiff = parkingAngle - getPosition().getDirection().getAngle();
      double angle2Turn = helper.getAngle2Turn(angleDiff, rotationSpeed);
      rotate(angle2Turn);
   }

   private void rotate(double angle2Turn) {
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

   public abstract static class AbstractGunCarriageBuilder<T extends AbstractGunCarriage> {

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
