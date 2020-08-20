package com.myownb3.piranha.ui.image;

/**
 * The {@link ImageResource} defines a resource to an image as well as a definition of the images shape (e.g. rectangle or circle and so
 * on)
 * 
 * @author DStalder
 *
 */
public class ImageResource {

   private String resource;
   private ImageShape imageShape;

   public ImageResource(String resource, ImageShape imageShape) {
      this.resource = resource;
      this.imageShape = imageShape;
   }

   public String getResource() {
      return resource;
   }

   public ImageShape getImageShape() {
      return imageShape;
   }
}
