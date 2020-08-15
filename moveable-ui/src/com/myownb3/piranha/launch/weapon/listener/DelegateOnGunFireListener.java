package com.myownb3.piranha.launch.weapon.listener;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.gun.OnGunFireListener;
import com.myownb3.piranha.core.grid.position.Position;

public class DelegateOnGunFireListener implements OnGunFireListener {

   private OnGunFireListener onGunFireListener;

   @Override
   public void onFire(Position position) {
      requireNonNull(onGunFireListener, "Call 'setOnGunFireListener() first!");
      onGunFireListener.onFire(position);
   }

   public void setOnGunFireListener(OnGunFireListener onGunFireListener) {
      requireNonNull(onGunFireListener);
      this.onGunFireListener = onGunFireListener;
   }
}
