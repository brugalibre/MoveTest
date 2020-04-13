/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.moveable;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;

/**
 * 
 * The {@link MoveablePainterConfig} contain the configuration for the {@link MoveablePainter}
 * 
 * @author Dominic
 *
 */
public class MoveablePainterConfig {

   private int evasionReach;
   private int evasionAngle;
   private int detectorAngle;
   private int detectorReach;
   private boolean drawDetector;
   private boolean drawMoveableDirection;

   public MoveablePainterConfig(EvasionStateMachineConfig evasionStateMachineConfig, boolean drawDetector, boolean drawMoveableDirection) {

      this.detectorReach = evasionStateMachineConfig.getDetectorReach();
      this.detectorAngle = evasionStateMachineConfig.getDetectorAngle();
      this.evasionAngle = evasionStateMachineConfig.getEvasionAngle();
      this.evasionReach = 2 * evasionStateMachineConfig.getDetectorReach() / 3;
      this.drawDetector = drawDetector;
      this.drawMoveableDirection = drawMoveableDirection;
   }

   public static MoveablePainterConfig of(EvasionStateMachineConfig evasionStateMachineConfig, boolean drawDetector, boolean drawMoveableDirection) {
      return new MoveablePainterConfig(evasionStateMachineConfig, drawDetector, drawMoveableDirection);
   }

   /**
    * 
    * @return the reach of the {@link Detector} until it can detect a {@link GridElement} and avoid it
    */
   public int getEvasionReach() {
      return evasionReach;
   }

   /**
    * @return the angle to detect an evasion with a {@link GridElement}
    */
   public int getEvasionAngle() {
      return evasionAngle;
   }

   /**
    * @return the angle to detect {@link GridElement} of the detector
    */
   public int getDetectorAngle() {
      return detectorAngle;
   }

   /**
    * @return the reach of the {@link Detector}
    */
   public int getDetectorReach() {
      return detectorReach;
   }

   /**
    * 
    * @return <code>true</code> if the detector should be drawn or <code>false</code>
    *         if not
    */
   public boolean isDrawDetector() {
      return drawDetector;
   }

   /**
    * @return <code>true</code> if the direction of the {@link Moveable} should be drawn or <code>false</code>
    *         if not
    */
   public boolean isDrawMoveableDirection() {
      return drawMoveableDirection;
   }
}
