package com.myownb3.piranha.ui.render.impl.shape;

import java.awt.Color;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.TIEFighterShape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.shape.circle.CirclePainter;
import com.myownb3.piranha.ui.render.impl.shape.rectangle.RectanglePainter;

public class TIEFighterShapePainter extends Drawable<TIEFighterShape> {

   private CirclePainter ballCockpitPainter;
   private RectanglePainter rightWingPainter;
   private RectanglePainter leftWingPainter;

   public TIEFighterShapePainter(TIEFighterShape value, Color color) {
      super(value);
      ballCockpitPainter = new CirclePainter(value.getBallCockpit(), PaintMode.SHAPE, color, 5, 5);
      rightWingPainter = new RectanglePainter(value.getRightWing(), color);
      leftWingPainter = new RectanglePainter(value.getLeftWing(), color);
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      ballCockpitPainter.render(graphicsCtx);
      rightWingPainter.render(graphicsCtx);
      leftWingPainter.render(graphicsCtx);
   }
}
