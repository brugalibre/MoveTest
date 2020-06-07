package com.myownb3.piranha.core.moveables.controller.forwardstrategy;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;

/**
 * The {@link MoveForwardRequest} contains all the necessary data to move an {@link EndPointMoveable} forward
 * 
 * @author Dominic
 *
 */
public class MoveForwardRequest {

   private EndPointMoveable endPointMoveable;
   private List<EndPosition> endPositions;
   private PostMoveForwardHandler postMoveForwardHandler;

   private MoveForwardRequest(EndPointMoveable endPointMoveable, List<EndPosition> endPositions, PostMoveForwardHandler postMoveForwardHandler) {
      this.endPointMoveable = endPointMoveable;
      this.endPositions = endPositions;
      this.postMoveForwardHandler = postMoveForwardHandler;
   }

   /**
    * @return the list of {@link EndPosition} the {@link Moveable} of this request is led to
    */
   public List<EndPosition> getEndPositions() {
      return endPositions;
   }

   /**
    * @return The {@link EndPointMoveable} which is going to be led
    */
   public EndPointMoveable getEndPointMoveable() {
      return endPointMoveable;
   }

   /**
    * @return the {@link PostMoveForwardHandler} for every action after moving
    */
   public PostMoveForwardHandler getPostMoveForwardHandler() {
      return requireNonNull(postMoveForwardHandler);
   }

   /**
    * Creates a new {@link MoveForwardRequest}
    * 
    * @param moveable
    *        the {@link Moveable} to move
    * @param endPositions
    *        the {@link EndPosition}s to reach
    * @param postMoveForwardHandler
    *        the {@link PostMoveForwardHandler} for every action after moving
    * @return a new {@link MoveForwardRequest}
    */
   public static MoveForwardRequest of(EndPointMoveable moveable, List<EndPosition> endPositions, PostMoveForwardHandler postMoveForwardHandler) {
      return new MoveForwardRequest(moveable, endPositions, postMoveForwardHandler);
   }
}
