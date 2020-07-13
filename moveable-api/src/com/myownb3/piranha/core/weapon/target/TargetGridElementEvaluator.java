package com.myownb3.piranha.core.weapon.target;

import java.util.Optional;

import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.position.Position;

public interface TargetGridElementEvaluator {

   /**
    * Evaluates the nearest {@link TargetGridElement} detected from the given {@link Position}
    * 
    * @param detectorPos
    *        the {@link Position} from which the {@link IDetector} will detect
    * @return the nearest {@link TargetGridElement}
    */
   Optional<TargetGridElement> getNearestTargetGridElement(Position detectorPos);

}
