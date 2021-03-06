/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.moveable;

import java.awt.Color;
import java.awt.Graphics;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.Graphics2DContext;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainter;
import com.myownb3.piranha.ui.render.impl.detector.DetectorPainterConfig;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class MoveablePainter extends AbstractGridElementPainter<Moveable> {

   private Color detectorColor;
   private boolean drawMoveableDirection;
   private DetectorPainter detectorPainter;

   public MoveablePainter(Moveable moveable, Color color, MoveablePainterConfig config) {
      super(moveable, color);
      this.detectorColor = Color.MAGENTA.brighter();
      this.drawMoveableDirection = config.isDrawMoveableDirection();
      if (config.isDrawDetector()) {
         detectorPainter = new DetectorPainter(moveable, detectorColor,
               DetectorPainterConfig.of(config.getDetectorCluster(), DetectorConfigBuilder.builder()
                     .withDetectorAngle(config.getDetectorAngle())
                     .withDetectorReach(config.getDetectorReach())
                     .withEvasionAngle(config.getEvasionAngle())
                     .withEvasionDistance(config.getEvasionReach())
                     .build()));
      }
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
      Graphics2DContext context = (Graphics2DContext) graphicsCtx;
      Graphics graphics = context.getGraphics2d();

      drawMoveableDirection(graphics, value.getPosition());
      if (detectorPainter != null) {
         detectorPainter.render(graphicsCtx);
      }
   }

   private void drawMoveableDirection(Graphics graphics, Position gridElemPos) {
      if (drawMoveableDirection) {
         graphics.setColor(Color.BLACK);
         drawDirectionFromPosition(graphics, gridElemPos, gridElemPos.getDirection().getVector(), 30);
      }
   }

   private static void drawDirectionFromPosition(Graphics graphics, Position position, Float64Vector directionVectorIn, int length) {
      Float64Vector directionVector = directionVectorIn.times(length * 10);
      int gridElemX1 = round(position.getX());
      int gridElemY1 = round(position.getY());
      int gridElemX2 = gridElemX1 + round(directionVector.getValue(0));
      int gridElemY2 = gridElemY1 + round(directionVector.getValue(1));
      graphics.drawLine(gridElemX1, gridElemY1, gridElemX2, gridElemY2);
   }

   private static int round(double x) {
      return (int) MathUtil.round(x, 0);
   }

}
