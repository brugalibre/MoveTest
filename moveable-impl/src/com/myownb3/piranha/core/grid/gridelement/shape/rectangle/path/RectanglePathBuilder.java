package com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path;

import java.io.Serializable;
import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link RectanglePathBuilder} is responsible for building all necessary {@link PathSegment}s for a {@link Rectangle}
 * in order to define it's shape
 * 
 * @author DStalder
 *
 */
public interface RectanglePathBuilder extends Serializable {

   /**
    * Builds a {@link List} with {@link PathSegment} that defines the path of a {@link Rectangle} with the given {@link Position} and size
    * 
    * @param center
    *        the center {@link Position} of the {@link Rectangle}
    * @param orientation
    *        the {@link Orientation} of the {@link Rectangle}
    * @param width
    *        the width
    * @param height
    *        the height
    * @return a {@link List} with {@link PathSegment}
    */
   public List<PathSegment> buildRectanglePath(Position center, Orientation orientation, double width, double height);

}
