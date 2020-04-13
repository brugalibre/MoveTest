/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.moveable;

import static com.myownb3.piranha.util.MathUtil.rotateVector;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.awt.Color;
import java.awt.Graphics;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class MoveablePainter extends AbstractGridElementPainter<Moveable> {

   private int evasionRange;
   private int evasionAngle;
   private int detectorRange;
   private int detectorAngle;
   private Color detectorColor;
   private boolean drawDetector;
   private boolean drawMoveableDirection;

   public MoveablePainter(Moveable moveable, Color color, int height, int width, MoveablePainterConfig config) {
      super(moveable, color, height, width);
      detectorColor = Color.MAGENTA.brighter();
      this.drawDetector = config.isDrawDetector();
      this.drawMoveableDirection = config.isDrawMoveableDirection();
      this.detectorRange = config.getDetectorReach();
      this.detectorAngle = config.getDetectorAngle();
      this.evasionAngle = config.getEvasionAngle();
      this.evasionRange = 2 * config.getDetectorReach() / 3;
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();

      Position furthermostFrontPosition = Positions.of(getValue().getFurthermostFrontPosition());
      Position gridElemPos = Positions.of(getValue().getPosition());

      drawDetector(graphics, furthermostFrontPosition);
      drawMoveableDirection(graphics, gridElemPos);
   }

   private void drawDetector(Graphics graphics, Position detectorPos) {
      if (drawDetector) {
         Color evasionColor = new Color(detectorColor.getRed(), detectorColor.getGreen(), detectorColor.getBlue()).darker();
         drawDetectorArc(graphics, detectorPos, detectorAngle, detectorRange, detectorColor);
         drawDetectorArc(graphics, detectorPos, evasionAngle, evasionRange, evasionColor);
      }
   }

   private void drawDetectorArc(Graphics graphics, Position detectorPos, int arcAngle, int arcRadius, Color color) {
      graphics.setColor(color);
      Direction detectorDirection = detectorPos.getDirection();
      int startAngle = arcAngle / 2;
      for (int curAngle = startAngle; curAngle >= -startAngle; curAngle--) {
         Float64Vector detectorDirectionVector = rotateVector(getVector(detectorDirection), curAngle);
         drawDirectionFromPosition(graphics, detectorPos, detectorDirectionVector, arcRadius);
      }
   }

   private void drawMoveableDirection(Graphics graphics, Position gridElemPos) {
      if (drawMoveableDirection) {
         graphics.setColor(Color.BLACK);
         drawDirectionFromPosition(graphics, gridElemPos, getVector(gridElemPos.getDirection()), 30);
      }
   }

   private void drawDirectionFromPosition(Graphics graphics, Position position, Float64Vector directionVectorIn, int length) {
      Float64Vector directionVector = directionVectorIn.times(length * 10);
      int gridElemX1 = round(position.getX());
      int gridElemY1 = round(position.getY());
      int gridElemX2 = gridElemX1 + round(directionVector.getValue(0));
      int gridElemY2 = gridElemY1 + round(directionVector.getValue(1));
      graphics.drawLine(gridElemX1, gridElemY1, gridElemX2, gridElemY2);
   }

   private int round(double x) {
      return (int) MathUtil.round(x, 0);
   }

}
