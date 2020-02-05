package com.myownb3.piranha.moveables;

public interface MoveResult {

    /**
     * @return the distance to the end-position
     */
    double getEndPosDistance();

    /**
     * 
     * @return <code>true</code> if a {@link EndPointMoveable} has reached it's
     *         end-point. Or <code>false</code> if not
     * 
     */
    boolean isDone();

}
