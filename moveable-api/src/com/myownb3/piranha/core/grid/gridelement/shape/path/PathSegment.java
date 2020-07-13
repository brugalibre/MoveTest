package com.myownb3.piranha.core.grid.gridelement.shape.path;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link PathSegment} is a line and also part of the path of a {@link Shape}. By adding all {@link PathSegment} together you'll get the
 * entire path.
 * The smaller this segment is the more accurate the {@link Shape}s path is described
 * 
 * @author Dominic
 *
 */
public interface PathSegment {

   /**
    * 
    * @return the {@link Position} at the begin of this {@link PathSegment}
    */
   Position getBegin();

   /**
    * 
    * @return the {@link Position} at the end of this {@link PathSegment}
    */
   Position getEnd();

   /**
    * @return the length of this {@link PathSegment}
    */
   double getLenght();

   /**
    * @return the {@link Float64Vector} describing the direction from the begin-Position to the end-Position
    */
   Float64Vector getVector();

   /**
    * @return the 'normal' vector of this {@link PathSegment} which crosses it's begin - {@link Position}
    */
   Float64Vector getNormalVectorAtBeginPos();

   /**
    * @return the 'normal' vector of this {@link PathSegment} which crosses it's end - {@link Position}
    */
   Float64Vector getNormalVectorAtEndPos();
}
