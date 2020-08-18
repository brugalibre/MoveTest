package com.myownb3.piranha.ui.render.impl;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.imageio.ImageIO;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.image.ImageReader;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;

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
public abstract class Drawable<E> implements Renderer<E> {

   protected E value; // the value this Drawable represents on a panel
   protected int x; // the x-coordinate in relation to it's position on the TimeLine (used for
   // painting)
   protected int y; // the y-coordinate in relation to it's position on the TimeLine (used for
   // painting)
   protected int height;
   protected int width;
   protected ColorSetMode colorSetMode;

   /**
    * Creates a new {@link Drawable} with the given parent and value
    * 
    * @param parent,
    *        the parent of this component
    * @param value
    */
   public Drawable(E value) {
      this.value = value;
      this.colorSetMode = ColorSetMode.DEFAULT;
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

   @Override
   public final ColorSetMode getColorSetMode() {
      return this.colorSetMode;
   }

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      this.colorSetMode = drawMode;
   }

   public void setBounds(int x, int y, int height, int width) {
      Rectangle rec = new Rectangle(x, y, width, height);
      setBounds(rec);
   }

   protected BufferedImage readAndScaleImage4RectangleShape(com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle shape,
         String imageLocation) {
      try {
         BufferedImage bufferedImage = ImageIO.read(new File(imageLocation));
         double scaleHeight = shape.getHeight();
         double scaleWidth = shape.getWidth();
         return ImageReader.resizeImg(bufferedImage, scaleHeight, scaleWidth);
      } catch (IOException e) {
         throw new IllegalStateException("Image '" + imageLocation + "' not loadable");
      }
   }

   @SuppressWarnings("unchecked")
   protected <T extends Shape> T requireAs(Shape shape, Class<T> shapeClass) {
      if (shapeClass.isAssignableFrom(shape.getClass())) {
         return (T) shape;
      }
      throw new IllegalStateException("Shape '" + shape + "' must be a '" + shapeClass + "'!");
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
               throw new IllegalStateException(e);
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
}
