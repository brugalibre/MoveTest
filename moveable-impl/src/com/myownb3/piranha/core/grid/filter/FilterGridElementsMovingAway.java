package com.myownb3.piranha.core.grid.filter;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

public class FilterGridElementsMovingAway implements Predicate<GridElement> {
   private Supplier<? extends GridElement> movedGridElementSupplier;

   private FilterGridElementsMovingAway(Supplier<? extends GridElement> movedGridElementSupplier) {
      this.movedGridElementSupplier = movedGridElementSupplier;
   }

   /**
    * The detected GridElement can be ignored for collision detecting, if it is is moving faster then the one which is checking for
    * collision
    * 
    * @param avoidableGridElement
    *        the {@link GridElement} under test
    * @return <code>true</code> if the detected {@link GridElement} is <b>not</b> moving faster than the one which is checking for a
    *         collision or it's moving towards the one which is checking for a collision
    */
   @Override
   public boolean test(GridElement avoidableGridElement) {
      if (avoidableGridElement.getVelocity() > 0) {
         Position movedGridElementPos = getMovedGridElement().getPosition();
         return isNotMovingAway(calcDistance2MovedGridElementAfter(avoidableGridElement, movedGridElementPos),
               calcDistance2MovedGridElementBefore(avoidableGridElement, movedGridElementPos));
      }
      return true;// not moving at all
   }

   /*
    * The detected GridElement is moving away when
    *    - the distance between the detected gridelement and the moved GridElement is getting bigger
    */
   private boolean isNotMovingAway(double distanceBetweenTheGridElementsAfterMovement, double distanceBetweenTheGridElementsBeforeMovement) {
      return distanceBetweenTheGridElementsAfterMovement <= distanceBetweenTheGridElementsBeforeMovement;
   }

   private double calcDistance2MovedGridElementAfter(GridElement avoidableGridElement, Position movedGridElementPos) {
      Position movedAvoidableGridElemPos = avoidableGridElement.getPosition().movePositionForward(avoidableGridElement.getVelocity());
      return movedGridElementPos.calcDistanceTo(movedAvoidableGridElemPos);
   }

   private double calcDistance2MovedGridElementBefore(GridElement avoidableGridElement, Position movedGridElementPos) {
      return movedGridElementPos.calcDistanceTo(avoidableGridElement.getPosition());
   }

   private GridElement getMovedGridElement() {
      return movedGridElementSupplier.get();
   }

   /**
    * Creates a new {@link GridElementFilter} with the given filter criterias
    * 
    * @param movedGridElementSupplier
    *        a {@link Supplier} for the {@link GridElement} which was moved on the {@link Grid} and for which there are other
    *        {@link GridElement}s to filter
    * @return a new {@link GridElementFilter}
    */
   public static FilterGridElementsMovingAway of(Supplier<? extends GridElement> movedGridElementSupplier) {
      return new FilterGridElementsMovingAway(movedGridElementSupplier);
   }
}
