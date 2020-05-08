package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;

public class EvasionEventStateInput extends CommonEvasionStateInput {

   private Detector detector;
   private Position posBeforeEvasion;

   public EvasionEventStateInput(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper, Position posBeforeEvasion) {
      super(grid, moveable, helper);
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
    * 
    * @param grid
    *        the {@link Grid} on which the moveable moves
    * 
    * @param moveable
    *        the {@link Moveable}
    * @param detector
    *        the {@link Detector} which helps detecting other
    *        {@link GridElement}s
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param posBeforeEvasion
    *        the {@link Position} of the {@link Moveable} before the evasion
    * @return a new {@link EvasionEventStateInput}
    */
   public static EvasionEventStateInput of(Grid grid, Moveable moveable, Detector detector,
         DetectableMoveableHelper helper, Position posBeforeEvasion) {
      return new EvasionEventStateInput(grid, moveable, detector, helper, posBeforeEvasion);
   }
}
