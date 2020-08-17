package com.myownb3.piranha.audio;

import java.io.InputStream;

/**
 * The {@link AudioClip} is a simple wrapper for what ever sound implementation is going to be used later.
 * It wrapps all the {@link InputStream} stuff and only let us start a new clip
 * 
 * @author Dominic
 *
 */
public interface AudioClip {

   /**
    * Plays this {@link AudioClip}
    */
   public void play();

   /**
    * Stops playing this {@link AudioClip}
    */
   void stop();

   /**
    * @return <code>true</code> if this {@link AudioClip} can be closed or <code>false</code> if not
    */
   boolean isCloseable();

   /**
    * Closes this {@link AudioClip}
    * <b>Note:</b> A closed {@link AudioClip} can not be reused
    */
   void close();

}
