package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Polygon;
import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class ShapePaintUtil {

   /**
    * Returns a {@link Polygon} representing the given {@link Shape}
    * 
    * @param shape
    *        the shape
    * @return the Polygon
    */
   public static Polygon getPoligon4Path(Shape shape) {
      List<PathSegment> path = Collections.unmodifiableList(shape.getPath());
      int[] yPoints = path.stream()
            .map(PathSegment::getBegin)
            .map(Position::getY)
            .map(Double::intValue)
            .mapToInt(i -> i).toArray();
      int[] xPoints = path.stream()
            .map(PathSegment::getBegin)
            .map(Position::getX)
            .map(Double::intValue)
            .mapToInt(i -> i).toArray();
      return new Polygon(xPoints, yPoints, path.size());
   }
}
