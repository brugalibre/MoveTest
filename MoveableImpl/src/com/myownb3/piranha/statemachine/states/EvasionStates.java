/**
 * 
 */
package com.myownb3.piranha.statemachine.states;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;

/**
 * Describes the different states when a {@link Moveable} is envasion any
 * {@link GridElement}
 * 
 * @author Dominic
 *
 */
public enum EvasionStates {

    NONE {
	@Override
	public EvasionStates nextState() {
	    return NONE;
	}
    },
    
    /**
     * Describes the default or initial state of any {@link Moveable} which are
     * currently not evasion any {@link GridElement}
     */
    DEFAULT {
	@Override
	public EvasionStates nextState() {
	    return EVASION;
	}
    },

    /**
     * Describes the effectively state of evasion the detected {@link GridElement}
     */
    EVASION {
	@Override
	public EvasionStates nextState() {
	    return POST_EVASION;
	}
    },

    /**
     * Describes the state after a {@link GridElement} was evaded and the
     * {@link Moveable} is correcting it's direction bevore the {@link GridElement}
     * can be safely passed
     */
    POST_EVASION {
	@Override
	public EvasionStates nextState() {
	    return PASSING;
	}
    },

    /**
     * Describes the state after a {@link Moveable} has evaded a {@link GridElement}
     */
    PASSING {
	@Override
	public EvasionStates nextState() {
	    return RETURNING;
	}
    },

    /**
     * Describes the state after a {@link Moveable} has passed a {@link GridElement}
     * and is now returning to its former direction
     */
    RETURNING {
	@Override
	public EvasionStates nextState() {
	    return EvasionStates.RE_INIT;
	}
    },

    /**
     * Describes the state after the {@link EvasionStateMachine} returns from the state {@link EvasionStates#RETURNING}
     * This inidacates that the {@link EvasionStateMachine} has to re-initializes itself
     */
    RE_INIT {
	@Override
	public EvasionStates nextState() {
	    return EvasionStates.DEFAULT;
	}
    };
    
    public abstract EvasionStates nextState();
}
