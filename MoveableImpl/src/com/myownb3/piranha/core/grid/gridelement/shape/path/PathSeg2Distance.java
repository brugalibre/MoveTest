package com.myownb3.piranha.core.grid.gridelement.shape.path;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class PathSeg2Distance {
   private PathSegment pathSegment;
   private double distance;

   private PathSeg2Distance(PathSegment pathSegment, double distance) {
      this.pathSegment = pathSegment;
      this.distance = distance;
   }

   /**
    * Converts the given list of {@link PathSegment} into a list of {@link PathSeg2Distance} which is a mapping of each {@link PathSegment}
    * to it's distance to the new {@link Position}
    * 
    * @param newPosition
    *        the new {@link Position}
    * @param path
    *        the path of a {@link Shape}
    * @return a list of {@link PathSeg2Distance}
    */
   public static List<PathSeg2Distance> fillupPathSegment2DistanceMap(Position newPosition, List<PathSegment> path) {
      return path.stream()
            .map(toPathSegment(newPosition))
            .collect(Collectors.toList());
   }

   private static Function<? super PathSegment, ? extends PathSeg2Distance> toPathSegment(Position newPosition) {
      return pathSegment -> new PathSeg2Distance(pathSegment, newPosition.calcDistanceToLine(pathSegment.getBegin(), pathSegment.getVector()));
   }

   public PathSegment getPathSegment() {
      return pathSegment;
   }

   public double getDistance() {
      return distance;
   }
}
