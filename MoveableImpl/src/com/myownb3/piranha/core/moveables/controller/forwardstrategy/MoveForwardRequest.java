package com.myownb3.piranha.core.moveables.controller.forwardstrategy;

import java.util.List;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link MoveForwardRequest} contains all the necessary data to move an {@link EndPointMoveable} forward
 * 
 * @author Dominic
 *
 */
public class MoveForwardRequest {

   private EndPointMoveable endPointMoveable;
   private List<EndPosition> endPositions;

   private MoveForwardRequest(EndPointMoveable endPointMoveable, List<EndPosition> endPositions) {
      this.endPointMoveable = endPointMoveable;
      this.endPositions = endPositions;
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
    * Creates a new {@link MoveForwardRequest}
    * 
    * @param moveable
    *        the {@link Moveable} to move
    * @param endPositions
    *        the {@link EndPosition}s to reach
    * @return a new {@link MoveForwardRequest}
    */
   public static MoveForwardRequest of(EndPointMoveable moveable, List<EndPosition> endPositions) {
      return new MoveForwardRequest(moveable, endPositions);
   }
}
