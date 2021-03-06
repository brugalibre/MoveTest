package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;

public class EvasionEventStateInput extends CommonEvasionStateInput {

   private Detector detector;
   private Position posBeforeEvasion;

   public EvasionEventStateInput(Moveable moveable, Detector detector, DetectableMoveableHelper helper, Position posBeforeEvasion) {
      super(moveable, helper);
      this.detector = requireNonNull(detector);
      this.posBeforeEvasion = requireNonNull(posBeforeEvasion);
   }

   public Detector getDetector() {
      return detector;
   }

   /**
    * @return the Position before the evasion
    */
   public final Position getPosBeforeEvasion() {
      return this.posBeforeEvasion;
   }

   /**
    * Creates a new {@link EvasionEventStateInput}
    * @param moveable
    *        the {@link Moveable}
    * @param detector
    *        the {@link Detector} which helps detecting other
    *        {@link GridElement}s
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param posBeforeEvasion
    *        the {@link Position} of the {@link Moveable} before the evasion
    * 
    * @return a new {@link EvasionEventStateInput}
    */
   public static EvasionEventStateInput of(Moveable moveable, Detector detector, DetectableMoveableHelper helper,
         Position posBeforeEvasion) {
      return new EvasionEventStateInput(moveable, detector, helper, posBeforeEvasion);
   }
}
