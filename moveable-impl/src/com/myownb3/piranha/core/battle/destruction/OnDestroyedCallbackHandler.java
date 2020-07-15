package com.myownb3.piranha.core.battle.destruction;

/**
 * A callback when ever the {@link DestructionHelper} was destroyed
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface OnDestroyedCallbackHandler {

   /**
    * Call in order to propagate the destruction
    */
   void onDestroy();

   /**
    * Returns a {@link OnDestroyedCallbackHandler} which combines this as well as the other given {@link OnDestroyedCallbackHandler}
    * 
    * @param otherOnDestroyedCallbackHandler
    * @return a combined {@link OnDestroyedCallbackHandler}
    */
   default OnDestroyedCallbackHandler andThen(OnDestroyedCallbackHandler otherOnDestroyedCallbackHandler) {
      return () -> {
         this.onDestroy();
         otherOnDestroyedCallbackHandler.onDestroy();
      };
   }
}
