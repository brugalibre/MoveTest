package com.myownb3.piranha.core.grid.gridelement.shape;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;

public class ShapeUtil {

   private ShapeUtil() {
      // private
   }

   /**
    * Combines the Path of all given shapes
    * 
    * @param gunShapes
    *        all given {@link Shape}s
    * @return the combined path
    */
   public static List<PathSegment> combinePath(Shape... gunShapes) {
      return combinePath(Arrays.asList(gunShapes));
   }

   /**
    * Combines the Path of all given shapes
    * 
    * @param gunShapes
    *        all given {@link Shape}s
    * @return the combined path
    */
   public static List<PathSegment> combinePath(List<? extends Shape> gunShapes) {
      return gunShapes.stream()
            .map(Shape::getPath)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
   }
}
