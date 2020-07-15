package com.myownb3.piranha.core.battle.destruction;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;

public class DestructionAudio {

   /**
    * Plays the default explosion sound
    * 
    * @see AudioConstants#EXPLOSION_SOUND
    */
   public void playDefaultExplosion() {
      AudioClipBuilder.builder()
            .withAudioResource(AudioConstants.EXPLOSION_SOUND)
            .build()
            .play();
   }
}
