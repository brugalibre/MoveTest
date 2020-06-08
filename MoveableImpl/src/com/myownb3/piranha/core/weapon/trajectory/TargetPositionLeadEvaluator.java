package com.myownb3.piranha.core.weapon.trajectory;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TargetGridElement;

/**
 * The {@link TargetPositionLeadEvaluator} is responsible for evaluating the {@link Position} a {@link Gun} should aim in order to hit a
 * moving target.
 * So the {@link TargetPositionLeadEvaluatorImpl} basically calculates a lead for which the {@link Gun} actually aims
 * 
 * @author Dominic
 *
 */
public interface TargetPositionLeadEvaluator {

   /**
    * Depending on the acquired {@link GridElement} this calculates a 'lead' to the evaluated target-Position which consideres the forward
    * movment of this very target-Position as well as the velocity of the gun-projectiles
    * 
    * If the given {@link TargetGridElement} is not moving at all, then it's direct Position is returned
    * 
    * @param targetGridElement
    *        the evaluated target-Position
    * @param turretPos
    *        the Position of the {@link Turret}
    * @return the actual target {@link Position}
    */
   Position calculateTargetConsideringLead(TargetGridElement targetGridElement, Position turretPos);

}
