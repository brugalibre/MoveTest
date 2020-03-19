package com.myownb3.piranha.statemachine.impl.handler.evasionstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;

public class EvasionEventStateInput extends CommonEventStateInput {

   private Detector detector;

   public EvasionEventStateInput(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {
      super(grid, moveable, helper);
      this.detector = requireNonNull(detector);
   }

   public Detector getDetector() {
      return detector;
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
    * @return a new {@link EvasionEventStateInput}
    */
   public static EvasionEventStateInput of(Grid grid, Moveable moveable, Detector detector,
         DetectableMoveableHelper helper) {
      return new EvasionEventStateInput(grid, moveable, detector, helper);
   }
}
