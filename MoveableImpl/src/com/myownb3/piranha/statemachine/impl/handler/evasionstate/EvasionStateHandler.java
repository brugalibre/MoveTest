package com.myownb3.piranha.statemachine.impl.handler.evasionstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;
import static java.util.Objects.nonNull;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.output.EvasionStateResult;

public class EvasionStateHandler extends CommonEvasionStateHandlerImpl<EvasionEventStateInput, EvasionStateResult> {

   private boolean hadEvasionBefore;// defines if there was any evasion at all
   private int postEvasionDelayDistance;

   public EvasionStateHandler(int postEvasionDelayDistance) {
      this.postEvasionDelayDistance = postEvasionDelayDistance;
   }

   @Override
   public void init() {
      super.init();
      hadEvasionBefore = false;
   }

   @Override
   public EvasionStateResult handle(EvasionEventStateInput evenStateInput) {
      return handleEvasionState(evenStateInput.getGrid(), evenStateInput.getMoveable(), evenStateInput.getDetector(),
            evenStateInput.getHelper(), evenStateInput.getPosBeforeEvasion());
   }

   private EvasionStateResult handleEvasionState(Grid grid, Moveable moveable, Detector detector,
         DetectableMoveableHelper helper, Position posBeforeEvasion) {
      if (helper.check4Evasion(grid, moveable)) {
         hadEvasionBefore = true;
         EvasionStateResult evasionStateResult = handleEvasionManeuvre(grid, moveable, detector, helper);
         if (nonNull(evasionStateResult)) {
            return evasionStateResult;
         }
      }
      return evalAndReturnNextState(moveable, posBeforeEvasion);
   }

   private EvasionStateResult evalAndReturnNextState(Moveable moveable, Position posBeforeEvasion) {
      if (has2DelayPostEvasion(moveable, posBeforeEvasion)) {
         return EvasionStateResult.of(EVASION, EVASION, false);
      }
      return EvasionStateResult.of(EVASION, EVASION.nextState(), false);
   }

   private EvasionStateResult handleEvasionManeuvre(Grid grid, Moveable moveable, Detector detector, DetectableMoveableHelper helper) {
      double avoidAngle = detector.getEvasionAngleRelative2(moveable.getPosition());
      moveable.makeTurnWithoutPostConditions(avoidAngle);
      helper.checkSurrounding(grid, moveable);
      if (helper.check4Evasion(grid, moveable)) {
         return EvasionStateResult.of(EVASION, EVASION, true);
      }
      return null;
   }

   /*
    * true if we have to wait until we switch to state 'POST_EVASION' and false if we can move on to the state 'POST_EVASION' since we have successfully avoided an avoidable and are in safe distance to it
    */
   private boolean has2DelayPostEvasion(Moveable moveable, Position posBeforeEvasion) {
      return hadEvasionBefore
            && isNotInSaveDistance(moveable.getPosition(), posBeforeEvasion);
   }

   private boolean isNotInSaveDistance(Position moveablePos, Position posBeforeEvasion) {
      double distancePosBeforeEvasion2CurrentPos = posBeforeEvasion.calcDistanceTo(moveablePos);
      return distancePosBeforeEvasion2CurrentPos <= postEvasionDelayDistance;
   }
}
