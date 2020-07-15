package com.myownb3.piranha.core.battle.weapon.guncarriage;

import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.battle.weapon.guncarriage.sound.GunCarriageAudio;
import com.myownb3.piranha.core.battle.weapon.guncarriage.sound.GunCarriageAudio.GunCarriageAudioBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.state.GunCarriageStates;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.Orientation2PositionHelper;

public class AbstractGunCarriage implements GunCarriage {

   private static final long serialVersionUID = -1724494190061272992L;
   private GunCarriageStates gunCarriageState;
   private Shape shape;
   private Gun gun;
   private double rotationSpeed;
   private transient Orientation2PositionHelper helper;
   private transient GunCarriageAudio gunCarriageAudio;

   protected AbstractGunCarriage(Shape shape, Gun gun, double rotationSpeed) {
      this.shape = requireNonNull(shape);
      this.gun = requireNonNull(gun);
      this.rotationSpeed = abs(rotationSpeed);
      this.gun.evalAndSetGunPosition(shape.getForemostPosition());
      this.helper = new Orientation2PositionHelper();
      this.gunCarriageAudio = GunCarriageAudioBuilder.builder()
            .withAudio()
            .build();
      this.gunCarriageState = GunCarriageStates.IDLE;
   }

   @Override
   public void fire() {
      gun.fire();
   }

   @Override
   public void aimTargetPos(Position targetPos) {
      double angleDiff = getPosition().calcAngleRelativeTo(targetPos);
      double angle2Turn = helper.getAngle2Turn(angleDiff, rotationSpeed);
      evalAndSetCurrentGunCarriageState(angle2Turn);
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);
      rotate(angle2Turn);
   }

   @Override
   public void turn2ParkPosition(double parkingAngle) {
      double angleDiff = parkingAngle - getPosition().getDirection().getAngle();
      double angle2Turn = helper.getAngle2Turn(angleDiff, rotationSpeed);
      evalAndSetCurrentGunCarriageState(angle2Turn);
      gunCarriageAudio.playGunCarriageAudio(gunCarriageState);
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

   private void evalAndSetCurrentGunCarriageState(double angle2Turn) {
      this.gunCarriageState = evaluateCurrentGunCarriageState(angle2Turn, gunCarriageState);
   }

   @Visible4Testing
   static GunCarriageStates evaluateCurrentGunCarriageState(double angle2Turn, GunCarriageStates gunCarriageState) {
      boolean has2Turn = has2Turn(angle2Turn);
      switch (gunCarriageState) {
         case IDLE:
            return has2Turn ? GunCarriageStates.START_TURNING : GunCarriageStates.IDLE;
         case START_TURNING:
            return has2Turn ? GunCarriageStates.TURNING : GunCarriageStates.IDLE;
         case TURNING:
            return !has2Turn ? GunCarriageStates.END_TURNING : GunCarriageStates.TURNING;
         case END_TURNING:
            return !has2Turn ? GunCarriageStates.IDLE : GunCarriageStates.START_TURNING;
         default:
            throw new IllegalStateException("Unknown State '" + gunCarriageState + "'");
      }
   }

   private static boolean has2Turn(double angle2Turn) {
      return angle2Turn != 0.0;
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
