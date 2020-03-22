package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Polygon;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

public class ShapePaintUtil {

   /**
    * Returns a {@link Polygon} representing the given {@link Shape}
    * 
    * @param shape
    *        the shape
    * @return the Polygon
    */
   public static Polygon getPoligon4Path(Shape shape) {
      int[] yPoints = shape.getPath()
            .stream()
            .map(Position::getY)
            .map(Double::intValue)
            .mapToInt(i -> i).toArray();
      int[] xPoints = shape.getPath()
            .stream()
            .map(Position::getX)
            .map(Double::intValue)
            .mapToInt(i -> i).toArray();
      return new Polygon(xPoints, yPoints, shape.getPath().size());
   }
}
