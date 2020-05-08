/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.moveable;

import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.awt.Color;
import java.awt.Graphics;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
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

   public MoveablePainter(Moveable moveable, Color color, int height, int width, MoveablePainterConfig config) {
      super(moveable, color, height, width);
      this.detectorColor = Color.MAGENTA.brighter();
      this.drawMoveableDirection = config.isDrawMoveableDirection();
      detectorPainter = new DetectorPainter(moveable, detectorColor, height, width,
            DetectorPainterConfig.of(config.getDetectorCluster(), DetectorConfigBuilder.builder()
                  .withDetectorAngle(config.getDetectorAngle())
                  .withDetectorReach(config.getDetectorReach())
                  .withEvasionAngle(config.getEvasionAngle())
                  .withEvasionDistance(config.getEvasionReach())
                  .build()));
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();

      drawMoveableDirection(graphics, getValue().getPosition());
      detectorPainter.render(graphicsCtx);
   }

   private void drawMoveableDirection(Graphics graphics, Position gridElemPos) {
      if (drawMoveableDirection) {
         graphics.setColor(Color.BLACK);
         drawDirectionFromPosition(graphics, gridElemPos, getVector(gridElemPos.getDirection()), 30);
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
