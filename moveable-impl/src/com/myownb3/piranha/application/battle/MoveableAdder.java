package com.myownb3.piranha.application.battle;

import java.util.List;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.application.battle.impl.MoveableAdderImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

/**
 * The {@link MoveableAdder} is responsible for adding {@link Moveable} on an {@link Application}
 * 
 * @author Dominic
 *
 */
public interface MoveableAdder {

   /**
    * Increments the cycle counter of this {@link MoveableAdder}
    */
   void incrementCounter();

   /**
    * Handles a new cycle on a {@link Application}
    * Return <code>true</code> if the current cycle is over, which means that the internal <code>cycleCounter</code> is greater or equal
    * then the counter size defined by this {@link MoveableAdderImpl} or <code>false</code> if not
    * This internal <code>cycleCounter</code> is resetet to <code>0</code> if this method returns <code>true</code>
    * 
    * 
    * @return <code>true</code> if the current cycle is over and <code>false</code> if not
    */
   boolean isCycleDone();

   /**
    * Adds new {@link GridElement} if necessary
    * 
    * @param grid
    *        the {@link Grid}
    * @param evasionStateMachineConfig
    *        the {@link EvasionStateMachineConfig} for added {@link Moveable}s
    * @param padding
    *        the padding
    * @return all new created {@link GridElement}
    */
   List<GridElement> check4NewMoveables2Add(Grid grid, EvasionStateMachineConfig evasionStateMachineConfig);

}
