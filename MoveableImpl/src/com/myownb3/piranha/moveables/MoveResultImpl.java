package com.myownb3.piranha.moveables;

public class MoveResultImpl implements MoveResult{

    double distance2EndPos;
    double prevDistance2EndPos;
    boolean isDone;

    
    public MoveResultImpl(double distance2EndPos, double prevDistance2EndPos) {
	this(distance2EndPos, prevDistance2EndPos, false);
    }
    
    public MoveResultImpl(double distance2EndPos, double prevDistance2EndPos, boolean isDone) {
	this.distance2EndPos = distance2EndPos;
	this.prevDistance2EndPos = prevDistance2EndPos;
	this.isDone = isDone;
    }

    @Override
    public double getEndPosDistance() {
	return distance2EndPos;
    }

    @Override
    public boolean isDone() {
	return isDone;
    }
}
