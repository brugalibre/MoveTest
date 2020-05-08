/**
 * 
 */
package com.myownb3.piranha.core.grid;

/**
 * @author Dominic
 *
 */
public class DimensionImpl implements Dimension {

   private int height;
   private int width;
   private int x;
   private int y;

   public DimensionImpl(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   @Override
   public int getWidth() {
      return width;
   }

   @Override
   public int getHeight() {
      return height;
   }

   @Override
   public int getX() {
      return x;
   }

   @Override
   public int getY() {
      return y;
   }
}
