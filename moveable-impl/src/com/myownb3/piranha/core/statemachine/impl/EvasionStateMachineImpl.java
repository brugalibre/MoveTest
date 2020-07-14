/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.DEFAULT;
import static com.myownb3.piranha.core.statemachine.states.EvasionStates.EVASION;
import static com.myownb3.piranha.core.statemachine.states.EvasionStates.ORIENTING;
import static com.myownb3.piranha.core.statemachine.states.EvasionStates.PASSING;
import static com.myownb3.piranha.core.statemachine.states.EvasionStates.POST_EVASION;
import static com.myownb3.piranha.core.statemachine.states.EvasionStates.RETURNING;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.core.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.core.statemachine.handler.returningstate.ReturningStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.defaultstate.DefaultStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.defaultstate.input.DefaultStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.EvasionStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.OrientatingStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.input.OrientatingStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.passingstate.PassingStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.passingstate.input.PassingEventStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.DefaultPostEvasionStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.PostEvasionStateHandlerWithEndPos;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.ReturningStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.ReturningStateHandlerWithoutEndPos;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

/**
 * An {@link EvasionStateMachine} implements the {@link MoveablePostActionHandler} whereas it not only can detect toher
 * {@link GridElement} it also can handle the complete evasion maneuvres in order to avoid an evasion
 * 
 * @author Dominic
 *
 */
public class EvasionStateMachineImpl implements EvasionStateMachine, MoveablePostActionHandler {

   @Visible4Testing
   EvasionStates evasionState;
   private DetectableMoveableHelper detectableMoveableHelper;
   private Map<EvasionStates, EvasionStatesHandler<?, ?>> evasionStatesHandler2StateMap;
   private EvasionStateMachineConfig config;
   private Position positionBeforeEvasion;
   private EndPosition endPosition;

   private EvasionStateMachineImpl(DetectableMoveableHelper detectableMoveableHelper, EndPosition endPos, EvasionStateMachineConfig config) {
      this(detectableMoveableHelper, endPos, config, createAndInitHandlerMap(config, detectableMoveableHelper.getDetector(), endPos));
   }

   private EvasionStateMachineImpl(DetectableMoveableHelper detectableMoveableHelper, EndPosition endPosition, EvasionStateMachineConfig config,
         Map<EvasionStates, EvasionStatesHandler<?, ?>> handlerMap) {
      this.config = config;
      this.endPosition = endPosition;
      this.evasionState = DEFAULT;
      this.positionBeforeEvasion = null;
      this.detectableMoveableHelper = detectableMoveableHelper;
      this.evasionStatesHandler2StateMap = handlerMap;
   }

   private static Map<EvasionStates, EvasionStatesHandler<?, ?>> createAndInitHandlerMap(EvasionStateMachineConfig config, Detector detector,
         EndPosition endPosition) {
      Map<EvasionStates, EvasionStatesHandler<?, ?>> evasionStatesHandler2StateMap = new EnumMap<>(EvasionStates.class);
      evasionStatesHandler2StateMap.put(ORIENTING, new OrientatingStateHandler(config.getOrientationAngle()));
      evasionStatesHandler2StateMap.put(DEFAULT, new DefaultStateHandler());
      evasionStatesHandler2StateMap.put(EVASION, new EvasionStateHandler(detector.getEvasionDelayDistance()));
      evasionStatesHandler2StateMap.put(POST_EVASION, getPostEvasionStateHandler(config, endPosition));
      evasionStatesHandler2StateMap.put(PASSING, new PassingStateHandler(config.getPassingDistance()));
      evasionStatesHandler2StateMap.put(RETURNING, getReturningStateHandler(config, endPosition));

      return evasionStatesHandler2StateMap;
   }

   @Override
   public boolean handlePostConditions(Moveable moveable) {
      beforePostConditions(moveable);
      CommonEvasionStateResult eventStateResult = handlePostConditionsInternal(moveable);
      afterPostConditions(eventStateResult);
      return true;
   }

   private void beforePostConditions(Moveable moveable) {
      detectableMoveableHelper.handlePostConditions(moveable);
   }

   private CommonEvasionStateResult handlePostConditionsInternal(Moveable moveable) {
      CommonEvasionStateResult eventStateResult = null;
      switch (evasionState) {
         case DEFAULT:
            DefaultStateHandler defaultStateHandler = getHandler4State(evasionState);
            DefaultStateInput defaultStateInput = DefaultStateInput.of(moveable, detectableMoveableHelper, endPosition);
            eventStateResult = defaultStateHandler.handle(defaultStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         case ORIENTING:
            OrientatingStateHandler orientatingStateHandler = getHandler4State(evasionState);
            OrientatingStateInput orientatingStateInput = OrientatingStateInput.of(moveable, detectableMoveableHelper, endPosition);
            eventStateResult = orientatingStateHandler.handle(orientatingStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         case EVASION:
            EvasionStateHandler evasionStateHandler = getHandler4State(evasionState);
            EvasionEventStateInput evasionEventStateInput = buildEvasionEventStateInput(moveable);
            eventStateResult = evasionStateHandler.handle(evasionEventStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         case POST_EVASION:
            PostEvasionStateHandler postEvastionStateHandler = getHandler4State(evasionState);
            PostEvasionEventStateInput postEvasionStateInput = buildPostEvasionEventStateInput(moveable);
            eventStateResult = postEvastionStateHandler.handle(postEvasionStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         case PASSING:
            PassingStateHandler passingStateHandler = getHandler4State(evasionState);
            PassingEventStateInput passingEventStateInput = buildPassingEventStateInput(moveable);
            eventStateResult = passingStateHandler.handle(passingEventStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         case RETURNING:
            ReturningStateHandler returningStateHandler = getHandler4State(evasionState);
            ReturningEventStateInput returingStateInput = buildReturningEventStateInput(moveable);
            eventStateResult = returningStateHandler.handle(returingStateInput);
            evasionState = eventStateResult.getNextState();
            break;
         default:
            throw new IllegalStateException("Unknown state'" + evasionState + "'");
      }
      return eventStateResult;
   }

   private void afterPostConditions(CommonEvasionStateResult eventStateResult) {
      boolean isEvadingNow = eventStateResult.getPrevState() != EVASION && evasionState == EVASION;
      if (isEvadingNow) {
         setPositionBeforeEvasion(eventStateResult.getPositionBeforeEvasion());
         initHandlers();
      }
   }

   private void initHandlers() {
      evasionStatesHandler2StateMap.values()
            .stream()
            .forEach(EvasionStatesHandler::init);
   }

   private void setPositionBeforeEvasion(Optional<Position> optionalPos) {
      positionBeforeEvasion = optionalPos.orElse(positionBeforeEvasion);
   }

   private static PostEvasionStateHandler getPostEvasionStateHandler(EvasionStateMachineConfig config, Position endPosition) {
      if (nonNull(endPosition)) {
         return new PostEvasionStateHandlerWithEndPos(config.getPostEvasionReturnAngle());
      }
      return new DefaultPostEvasionStateHandler(config.getPostEvasionReturnAngle());
   }

   private static ReturningStateHandler getReturningStateHandler(EvasionStateMachineConfig config, Position endPosition) {
      if (nonNull(endPosition)) {
         return new ReturningStateHandlerImpl(config);
      }
      return new ReturningStateHandlerWithoutEndPos();
   }

   @SuppressWarnings("unchecked")
   @Visible4Testing
   <T extends EvasionStatesHandler<?, ?>> T getHandler4State(EvasionStates state) {
      if (evasionStatesHandler2StateMap.containsKey(state)) {
         return (T) evasionStatesHandler2StateMap.get(state);
      }
      throw new IllegalStateException("No EvasionStatesHandler registered for state '" + state + "'");
   }

   private PassingEventStateInput buildPassingEventStateInput(Moveable moveable) {
      return PassingEventStateInput.of(detectableMoveableHelper, moveable, positionBeforeEvasion);
   }

   private EvasionEventStateInput buildEvasionEventStateInput(Moveable moveable) {
      return EvasionEventStateInput.of(moveable, detectableMoveableHelper.getDetector(), detectableMoveableHelper, positionBeforeEvasion);
   }

   private PostEvasionEventStateInput buildPostEvasionEventStateInput(Moveable moveable) {
      return PostEvasionEventStateInput.of(detectableMoveableHelper, moveable, positionBeforeEvasion, endPosition);
   }

   private ReturningEventStateInput buildReturningEventStateInput(Moveable moveable) {
      return ReturningEventStateInput.of(detectableMoveableHelper, moveable, positionBeforeEvasion, endPosition);
   }

   @Override
   public void setEndPosition(EndPosition endPos) {
      this.endPosition = requireNonNull(endPos);
      evasionStatesHandler2StateMap.put(POST_EVASION, getPostEvasionStateHandler(config, endPosition));
      evasionStatesHandler2StateMap.put(RETURNING, getReturningStateHandler(config, endPosition));
   }

   public static class EvasionStateMachineBuilder {
      private Grid grid;
      private EvasionStateMachineConfig config;
      private EndPosition endPosition;
      private Detector detector;
      private PostEvasionStateHandler postEvasionStateHandler;

      private EvasionStateMachineBuilder() {
         // private
      }

      public EvasionStateMachineBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public EvasionStateMachineBuilder withDetector(Detector detector) {
         this.detector = detector;
         return this;
      }

      public EvasionStateMachineBuilder withEndPosition(EndPosition endPosition) {
         this.endPosition = endPosition;
         return this;
      }

      public EvasionStateMachineBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig config) {
         this.config = config;
         return this;
      }

      public EvasionStateMachineBuilder withPostEvasionStateHandler(PostEvasionStateHandler postEvasionStateHandler) {
         this.postEvasionStateHandler = postEvasionStateHandler;
         return this;
      }

      public EvasionStateMachineImpl build() {
         if (nonNull(postEvasionStateHandler)) {
            Map<EvasionStates, EvasionStatesHandler<?, ?>> handlerMap = createAndInitHandlerMap(config, detector, endPosition);
            handlerMap.put(EvasionStates.POST_EVASION, postEvasionStateHandler);
            return new EvasionStateMachineImpl(new DetectableMoveableHelper(grid, detector), endPosition, config, handlerMap);
         }
         return new EvasionStateMachineImpl(new DetectableMoveableHelper(grid, detector), endPosition, config);
      }

      public static EvasionStateMachineBuilder builder() {
         return new EvasionStateMachineBuilder();
      }
   }
}
