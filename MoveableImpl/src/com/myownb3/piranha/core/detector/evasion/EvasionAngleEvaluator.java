package com.myownb3.piranha.core.detector.evasion;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link EvasionAngleEvaluator} is used directly by an implementation of a {@link IDetector} and is used for evaluating the angle to
 * avoid an evasion
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
    * @param position
    *        the {@link Position} from which we want to know the evasion angle
    * @returns the evasion angle increment
    */
   double getEvasionAngleRelative2(Position position);

   /**
    * Sets the {@link DetectionAware} of this {@link EvasionAngleEvaluator}
    * 
    * @param detectionAware
    */
   void setDetectionAware(DetectionAware detectionAware);
}
