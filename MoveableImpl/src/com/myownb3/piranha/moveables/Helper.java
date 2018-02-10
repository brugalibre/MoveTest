/**
 * 
 */
package com.myownb3.piranha.moveables;

/**
 * @author Dominic
 *
 */
public class Helper {

    protected Helper next;

    public void moveForward(AbstractMovable abstractMovable) {
	abstractMovable.position = abstractMovable.grid.moveForward(abstractMovable.position);
	checkPostConditions(abstractMovable);
    }

    public void makeTurn(AbstractMovable abstractMovable, double degree) {
	abstractMovable.position.rotate(degree);
	checkPostConditions(abstractMovable);
    }

    public void moveBackward(AbstractMovable abstractMovable) {
	abstractMovable.position = abstractMovable.grid.moveBackward(abstractMovable.position);
	checkPostConditions(abstractMovable);
    }

    public void moveBackward(AbstractMovable abstractMovable, int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveBackward(abstractMovable);
	}
    }

    public void moveForward(AbstractMovable abstractMovable, int amount) {
	verifyAmount(amount);
	for (int i = 0; i < amount; i++) {
	    moveForward(abstractMovable);
	}
    }

    void verifyAmount(int amount) {
	if (amount <= 0) {
	    throw new IllegalArgumentException("The value 'amount' must not be zero or below!");
	}
    }

    public void checkPostConditions(AbstractMovable abstractMovable) {

    }
}
