package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;

import com.myownb3.piranha.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;

public class PostEvasionStateHandler4Maze extends CommonEvasionStateHandlerImpl<PostEvasionEventStateInput, CommonEvasionStateResult>
      implements PostEvasionStateHandler {

   @Override
   public CommonEvasionStateResult handle(PostEvasionEventStateInput evenStateInput) {
      // We don't need to do anything here. A moveable with a TrippleDetectorCluster within a maze can correct it's path on its own  
      return evalNextStateAndBuildResult(evenStateInput, POST_EVASION, POST_EVASION.nextState());
   }
}
