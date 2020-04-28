package com.myownb3.piranha.detector.evasion;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link EvasionAngleEvaluator} is used directly by an implementation of a {@link IDetector} and is used for evaluating the angle to avoid
 * an evasion
 * 
 * @author Dominic
 *
 */
public interface EvasionAngleEvaluator {

   /**
    * Returns the angle increment for which a {@link Moveable} can make a turn in
    * order to avoid a {@link Avoidable} which is is on a collision path. This
    * method will return <code>0</code> if this {@link Detector} is currently not
    * evasion any {@link Avoidable}
    * 
    * @param avoidable
    *        the detected {@link Avoidable} which we want to avoid
    * @param position
    *        the {@link Position} from which we want to know the evasion angle
    * @returns the evasion angle increment
    */
   double getEvasionAngleRelative2(Avoidable avoidable, Position position);
}
