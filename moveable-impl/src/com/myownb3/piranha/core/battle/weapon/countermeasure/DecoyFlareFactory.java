package com.myownb3.piranha.core.battle.weapon.countermeasure;

import static com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareAutoDetectable.getDecoyFlareAutoDetectable;

import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.gridelement.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.factory.AbstractGridElementFactory;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AutoMoveable;
import com.myownb3.piranha.core.moveables.AutoMoveable.AutoMoveableBuilder;

/**
 * Creates new decoy flares
 * 
 * @author DStalder
 *
 */
public class DecoyFlareFactory extends AbstractGridElementFactory {

   public static final DecoyFlareFactory INSTANCE = new DecoyFlareFactory();
   private static final int AMOINT_OF_DECOY_FLARE_POINTS = 10;
   private static final int DECOY_FLARE_TTL_OFFSET = 15;

   private DecoyFlareFactory() {
      // private
   }

   /**
    * Creates a new decoy flare
    * 
    * @param decoyFlarePosition
    * @param decoyFlareConfig
    * @return a new {@link AutoMoveable} representing a decoy flare
    */
   public AutoMoveable createDecoyFlare(Position decoyFlarePosition, DecoyFlareConfig decoyFlareConfig) {
      Shape decoyShape = buildDecoyFlareShape(decoyFlarePosition, decoyFlareConfig);
      return createNewDecoyFlare(decoyShape, decoyFlareConfig);
   }

   private AutoMoveable createNewDecoyFlare(Shape decoyShape, DecoyFlareConfig decoyFlareConfig) {
      LazyGridElement lazyGridElement = new LazyGridElement();
      HealthImpl health = HealthImpl.of(1);
      DimensionInfo dimensionInfo = decoyFlareConfig.getDimensionInfo();
      int ttl = getDecoyFlareTTL(decoyFlareConfig);
      AutoMoveable autoMoveable = AutoMoveableBuilder.builder()
            .withDimensionInfo(dimensionInfo)
            .withBelligerentParty(decoyFlareConfig.getBelligerentParty())
            .withAutoDetectable(getDecoyFlareAutoDetectable(decoyShape, health, dimensionInfo.getHeightFromBottom(), ttl))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(DamageImpl.of(decoyFlareConfig.getProjectileDamage()))
                  .withHealth(health)
                  .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0))
                  .withOnDestroyedCallbackHandler(getDefaultOnDestroyedCallbackHandler(lazyGridElement))
                  .build())
            .withVelocity(decoyFlareConfig.getVelocity())
            .withGrid(grid)
            .withShape(decoyShape)
            .build();
      lazyGridElement.setGridElement(autoMoveable);
      return autoMoveable;
   }

   private int getDecoyFlareTTL(DecoyFlareConfig decoyFlareConfig) {
      return decoyFlareConfig.getDecoyFlareTimeToLife(DECOY_FLARE_TTL_OFFSET);
   }

   private static CircleImpl buildDecoyFlareShape(Position decoyFlarePos, DecoyFlareConfig decoyFlareConfig) {
      return CircleBuilder.builder()
            .withAmountOfPoints(AMOINT_OF_DECOY_FLARE_POINTS)
            .withRadius((int) decoyFlareConfig.getDimensionInfo().getDimensionRadius())
            .withCenter(decoyFlarePos)
            .build();
   }
}
