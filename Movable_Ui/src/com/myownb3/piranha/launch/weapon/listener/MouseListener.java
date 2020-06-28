package com.myownb3.piranha.launch.weapon.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.human.HumanToTurretInteractionCallbackHandler;

public class MouseListener implements MouseMotionListener, java.awt.event.MouseListener {

   private int offset;
   private HumanToTurretInteractionCallbackHandler callbackHandler;

   public MouseListener(int offset, HumanToTurretInteractionCallbackHandler callbackHandler) {
      this.offset = offset;
      this.callbackHandler = callbackHandler;
   }

   @Override
   public void mouseDragged(MouseEvent mouseEvent) {
      onTurretTurn(mouseEvent);
   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {
      onTurretTurn(mouseEvent);
   }

   private void onTurretTurn(MouseEvent mouseEvent) {
      Position turretPos = Positions.of(offset + mouseEvent.getX(), offset + mouseEvent.getY());
      callbackHandler.onTurretTurned(turretPos);
   }

   @Override
   public void mouseClicked(MouseEvent arg0) {
      callbackHandler.onSingleShotFired();
   }

   @Override
   public void mouseEntered(MouseEvent arg0) {
      // nothing to do
   }

   @Override
   public void mouseExited(MouseEvent arg0) {
      // nothing to do
   }

   @Override
   public void mousePressed(MouseEvent arg0) {
      callbackHandler.onStartFire(true);
   }

   @Override
   public void mouseReleased(MouseEvent arg0) {
      callbackHandler.onStartFire(false);
   }
}
