package com.myownb3.piranha.audio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.worker.WorkerThreadFactory;

/**
 * The {@link AudioClipCloser} watches all created {@link AudioClip}s and closes them as soon as they are done playing
 * 
 * @author Dominic
 *
 */
public class AudioClipCloser implements Callable<Optional<Void>> {

   /** The singleton instance of a {@link AudioClipCloser} */
   public static final AudioClipCloser INSTANCE = new AudioClipCloser();
   //   private static final Logger
   @Visible4Testing
   static final int CLEANUP_INTERVALL = 700;

   private List<AudioClip> audioClips;
   private Object lock;
   private boolean isRunning;

   private AudioClipCloser() {
      this.audioClips = new ArrayList<>();
      this.lock = new Object();
      this.isRunning = true;
   }

   public void registerAudioClip(AudioClip audioClip) {
      synchronized (lock) {
         this.audioClips.add(audioClip);
      }
   }

   @Override
   public Optional<Void> call() throws Exception {
      try {
         checkAllAudioClips4Closure();
      } catch (Exception e) {
         log(e);
         throw e;
      }
      return Optional.empty();
   }

   private void checkAllAudioClips4Closure() throws InterruptedException {
      while (isRunning) {
         List<AudioClip> audioClipsCpy = createAudioClipCopy();
         checkAudioClips4Closure(audioClipsCpy);
         Thread.sleep(CLEANUP_INTERVALL);
      }
   }

   private void checkAudioClips4Closure(List<AudioClip> audioClipsCpy) {
      audioClipsCpy.stream()
            .filter(AudioClip::isCloseable)
            .forEach(AudioClip::close);
   }

   private List<AudioClip> createAudioClipCopy() {
      List<AudioClip> audioClipsCopy;
      synchronized (lock) {
         audioClipsCopy = new ArrayList<>(audioClips);
      }
      return audioClipsCopy;
   }

   /**
    * Stops this {@link AudioClipCloser}
    */
   public void stop() {
      this.isRunning = false;
      audioClips.clear();
   }

   /**
    * This will lead this {@link AudioClipCloser} to be registeret at the {@link WorkerThreadFactory}
    */
   public void start() {
      isRunning = true;
      WorkerThreadFactory.INSTANCE.executeAsync(this);
   }

   private static void log(Exception e) {
      e.printStackTrace();
   }

}
