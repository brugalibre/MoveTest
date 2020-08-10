package com.myownb3.piranha.ui.render.impl.shape.rectangle;

import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;

public class RectanglePaintUtil {

   private RectanglePaintUtil() {
      // private 
   }

   public static double getRectangleRotationAngle(Rectangle rectangle) {
      double angle2Rotate = 0.0;
      if (rectangle.getOrientation() == Orientation.HORIZONTAL) {
         angle2Rotate = Math.toRadians(rectangle.getCenter().getDirection().getAngle());
      } else if (rectangle.getOrientation() == Orientation.VERTICAL) {
         angle2Rotate = Math.toRadians(rectangle.getCenter().getDirection().getAngle() + 90);
      }
      return angle2Rotate;
   }
}
