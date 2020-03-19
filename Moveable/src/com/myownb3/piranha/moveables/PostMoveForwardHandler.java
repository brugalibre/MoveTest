package com.myownb3.piranha.moveables;

@FunctionalInterface
public interface PostMoveForwardHandler {

   /**
    * Does specific actions after a {@link EndPointMoveable} was moved forward one
    * time
    * 
    * @param moveResult
    *        the result of the forward moving
    */
   void handlePostMoveForward(MoveResult moveResult);

}
