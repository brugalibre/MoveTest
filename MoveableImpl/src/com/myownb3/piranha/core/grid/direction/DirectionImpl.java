/**
 * 
 */
package com.myownb3.piranha.core.grid.direction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.gridelement.position.Positions.PositionImpl;
import com.myownb3.piranha.core.moveables.MoveableConst;
import com.myownb3.piranha.util.attribute.LazyAttribute;

/**
 * @author Dominic
 */
public class DirectionImpl implements Direction {

   private static final Map<Integer, String> degree2DirectionMap;
   static {
      degree2DirectionMap = getDegree2DirectionMap();
   }

   private double rotation;
   private double forwardX;
   private double forwardY;
   private String cardinalDirection;
   private LazyAttribute<Float64Vector> vector;

   /**
    * @param rotation
    * 
    */
   DirectionImpl(double rotation, String cardinalDirection) {
      this.rotation = rotation;
      this.forwardX = Math.cos(Math.toRadians(rotation)) / MoveableConst.STEP_WITDH;
      this.forwardY = Math.sin(Math.toRadians(rotation)) / MoveableConst.STEP_WITDH;
      this.cardinalDirection = cardinalDirection;
      this.vector = new LazyAttribute<>(() -> Float64Vector.valueOf(this.getForwardX(), this.getForwardY(), 0));
   }

   /**
    * @param rotation
    * 
    */
   DirectionImpl(double rotation) {
      this(rotation, evalCardinalDirection(rotation));
   }

   /**
    * @param rotation
    * 
    */
   DirectionImpl(double forwardX, double forwardY) {
      this.forwardX = forwardX;
      this.forwardY = forwardY;
      double angle = Math.toDegrees(Math.atan(forwardY / forwardX));
      this.rotation = PositionImpl.getAbsolutAngle(angle, forwardX, forwardY);
      this.vector = new LazyAttribute<>(() -> Float64Vector.valueOf(this.getForwardX(), this.getForwardY(), 0));
   }

   @Override
   public Direction rotate(double degree) {
      double rotationTmp = (this.rotation + degree) % 360;
      if (rotationTmp < 0) {
         rotationTmp = 360 + rotationTmp;
      }
      return new DirectionImpl(rotationTmp);
   }

   @Override
   public double getForwardX() {
      return forwardX;
   }

   @Override
   public double getForwardY() {
      return forwardY;
   }

   @Override
   public double getBackwardX() {
      return -getForwardX();
   }

   @Override
   public double getBackwardY() {
      return -getForwardY();
   }

   @Override
   public double getAngle() {
      return this.rotation;
   }

   @Override
   public Float64Vector getVector() {
      return vector.get();
   }

   @Override
   public final String getCardinalDirection() {
      return this.cardinalDirection;
   }

   private static String evalCardinalDirection(double rotation) {
      return degree2DirectionMap.get((int) rotation);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.cardinalDirection == null) ? 0 : this.cardinalDirection.hashCode());
      result = (prime * result + (int) this.rotation);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof DirectionImpl)) {
         return false;
      }
      DirectionImpl other = (DirectionImpl) obj;
      if (this.rotation != other.rotation) {
         return false;
      }
      if (this.cardinalDirection == null) {
         return other.cardinalDirection == null;
      }
      return this.cardinalDirection.equals(other.cardinalDirection);
   }

   @Override
   public String toString() {
      return "Cardinal-Direction:" + cardinalDirection + ", Rotation: " + rotation;
   }

   private static Map<Integer, String> getDegree2DirectionMap() {
      Map<Integer, String> degree2DirectionMap = new HashMap<>();

      degree2DirectionMap.put(90, "N");
      degree2DirectionMap.put(0, "O");
      degree2DirectionMap.put(270, "S");
      degree2DirectionMap.put(180, "W");

      return Collections.unmodifiableMap(degree2DirectionMap);
   }
}
