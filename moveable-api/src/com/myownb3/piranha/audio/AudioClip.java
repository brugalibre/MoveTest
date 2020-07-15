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

}
