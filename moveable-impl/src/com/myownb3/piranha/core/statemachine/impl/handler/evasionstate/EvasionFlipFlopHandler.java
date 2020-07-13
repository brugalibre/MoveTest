package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;
import com.myownb3.piranha.init.Initializable;

/**
 * The {@link EvasionFlipFlopHandler} recognize and resolve a flip-flop during the {@link EvasionStates#EVASION}
 * 
 * @author Dominic
 *
 */
public class EvasionFlipFlopHandler implements Initializable {

   private Double firstAvoidAngle;
   @Visible4Testing
   List<Double> avoidAngleHistory;
   @Visible4Testing
   int flipFlopCounter;

   public EvasionFlipFlopHandler() {
      init();
   }

   /**
    * Resets this {@link EvasionFlipFlopHandler}
    */
   @Override
   public void init() {
      flipFlopCounter = 0;
      firstAvoidAngle = null;
      avoidAngleHistory = new ArrayList<>();
   }

   /**
    * Register the current avoid angle. This ensures that we recognize a flip flop state
    * 
    * @param currentAvoidAngle
    *        the current evaluated angle
    */
   public void registerAvoidAngle(double currentAvoidAngle) {
      avoidAngleHistory.add(Double.valueOf(currentAvoidAngle));
      initFirstAvoidAngleIfNecessary(currentAvoidAngle);
      checkAvoidAngleHistoy();
   }

   /**
    * Returns either the current avoid-angle or, if there is a flip flop, the first one which was initially registered
    * 
    * @param currentAvoidAngle
    *        the current avoid-angle
    * @return either the current avoid-angle or, if there is a flip flop, the first one which was initially registered
    */
   public double getAvoidAngle(double currentAvoidAngle) {
      return isInFlipFlop() ? firstAvoidAngle : currentAvoidAngle;
   }

   private void initFirstAvoidAngleIfNecessary(double currentAvoidAngle) {
      if (isNull(firstAvoidAngle)) {
         firstAvoidAngle = currentAvoidAngle;
      }
   }

   /*
    * Verifies if there is a flip flop or if the avoid-angle-history needs to be cleaned up
    */
   private void checkAvoidAngleHistoy() {
      if (avoidAngleHistory.size() >= 3) {
         checkIfFlipFlop();
      } else if (avoidAngleHistory.size() == 2) {
         cleanUpHistoryIfNecessary();
      }
   }

   private void checkIfFlipFlop() {
      int size = avoidAngleHistory.size();
      double first = avoidAngleHistory.get(size - 3);
      double second = avoidAngleHistory.get(size - 2);
      double third = avoidAngleHistory.get(size - 1);
      // Since we never have so same values in a row, value 1 can not be equals to value 2
      if (getSignum(third) == getSignum(first)) {
         flipFlopCounter++;
         avoidAngleHistory.clear();
      } else if (getSignum(second) == getSignum(third)) {
         cleanUp();
      }
   }

   /*
    * returns the signum of this value
    */
   private int getSignum(double value) {
      return value < 0 ? -1 : 1;
   }

   /*
    * Avoid that we collect unnecessary data. As long as we collect the same data, there can't be a flip/flop -> remove the previous-value
    */
   private void cleanUpHistoryIfNecessary() {
      double first = avoidAngleHistory.get(0);
      double second = avoidAngleHistory.get(1);
      if (getSignum(first) == getSignum(second)) {
         cleanUp();
      }
   }

   private boolean isInFlipFlop() {
      return flipFlopCounter >= 2;
   }

   private void cleanUp() {
      avoidAngleHistory.remove(0);
      flipFlopCounter = 0;
   }

}
