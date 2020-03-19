package com.myownb3.piranha.ui.render.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.myownb3.piranha.ui.render.Renderer;

/**
 * The {@link Drawable} class can be used to draw a representation of any
 * instantiated object on the {@link MainUserInterface} component
 * 
 * @author Dominic
 * @param <E>
 *        the value this Drawable represents on the
 *        {@link MainUserInterface}
 * 
 */
public abstract class Drawable<E> implements Renderer {

   protected E value; // the value this Drawable represents on a panel
   protected int x; // the x-coordinate in relation to it's position on the TimeLine (used for
   // painting)
   protected int y; // the y-coordinate in relation to it's position on the TimeLine (used for
   // painting)
   protected int height;
   protected int width;

   /**
    * Creates a new {@link Drawable} with the given parent and value
    * 
    * @param parent,
    *        the parent of this component
    * @param value
    */
   public Drawable(E value) {
      this.value = value;
   }

   /**
    * Sets the bounds the layout manager has assigned to this {@link Drawable}.
    * Those, of course, have to be considered in the rendering process.
    * 
    * @param bounds
    *        the new bounds for the Drawable.
    */
   @Override
   public void setBounds(Rectangle bounds) {
      this.x = bounds.x;
      this.y = bounds.y;
      this.height = bounds.height;
      this.width = bounds.width;
   }

   public void setBounds(int x, int y, int height, int width) {
      Rectangle rec = new Rectangle(x, y, width, height);
      setBounds(rec);
   }

   /*
    * Returns true if the given point is within the area of this Drawable-object
    */
   protected boolean hasIntersect(Point e) {
      return (e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height);
   }

   @Override
   public String toString() {
      Field[] fields = getClass().getDeclaredFields();
      StringBuilder builder = new StringBuilder(
            "\n/**************/ " + getClass().getSimpleName() + " START /**************/");
      for (Field field : fields) {
         if (!Modifier.isStatic(field.getModifiers())) {
            try {
               builder.append("\n" + field.getName() + ": " + field.get(this));
            } catch (IllegalArgumentException | IllegalAccessException e) {
               throw new RuntimeException(e);
            }
         }
      }
      builder.append("\n/**************/ " + getClass().getSimpleName() + " ENDE /**************/ \n");
      return builder.toString();
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getX() {
      return x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getY() {
      return y;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   /**
    * Returns the {@link #value} field
    * 
    * @return the {@link #value} field
    */
   public void setValue(E value) {
      this.value = value;
   }

   /**
    * Returns the {@link #value} field
    * 
    * @return the {@link #value} field
    */
   public E getValue() {
      return value;
   }
}
