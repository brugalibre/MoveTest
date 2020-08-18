package com.myownb3.piranha.core.moveables.types;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.moveables.AbstractMoveable;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;

/**
 * The {@link AutoMoveableTypes} defines the Type of a {@link AbstractMoveable} or a {@link AutoMoveableController}
 * 
 * @author DStalder
 *
 */
public enum AutoMoveableTypes {

   /** The {@link AbstractMoveable} or {@link AutoMoveableController} represents a 'default' obstacle on the {@link Grid} */
   OBSTACLE,

   /** The {@link AbstractMoveable} or {@link AutoMoveableController} is a decoy flare */
   DECOY_FLARE

}
