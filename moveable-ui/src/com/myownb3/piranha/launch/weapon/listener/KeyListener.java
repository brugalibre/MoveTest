package com.myownb3.piranha.launch.weapon.listener;

import java.awt.event.KeyEvent;

import com.myownb3.piranha.core.moveables.engine.human.HumanToEngineInteractionCallbackHandler;

public class KeyListener implements java.awt.event.KeyListener {
   private HumanToEngineInteractionCallbackHandler callbackHandler;

   public KeyListener(HumanToEngineInteractionCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   @Override
   public void keyPressed(KeyEvent keyEvent) {
      handleKeyInput(keyEvent, true);
   }

   @Override
   public void keyReleased(KeyEvent keyEvent) {
      handleKeyInput(keyEvent, false);
   }

   @Override
   public void keyTyped(KeyEvent keyEvent) {
      handleKeyInput(keyEvent, true);
   }

   private void handleKeyInput(KeyEvent keyEvent, boolean isPressed) {
      if (keyEvent.getKeyCode() == KeyEvent.VK_UP || keyEvent.getKeyCode() == KeyEvent.VK_W) {
         callbackHandler.onForwardPressed(isPressed);
      } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN || keyEvent.getKeyCode() == KeyEvent.VK_S) {
         callbackHandler.onBackwardPressed(isPressed);
      }

      if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT || keyEvent.getKeyCode() == KeyEvent.VK_A) {
         callbackHandler.onTurnLeftPressed(isPressed);
      } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT || keyEvent.getKeyCode() == KeyEvent.VK_D) {
         callbackHandler.onTurnRightPressed(isPressed);
      }
   }
}
