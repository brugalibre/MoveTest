/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.moveable;

import static com.myownb3.piranha.util.MathUtil.rotateVector;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Optional;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.detector.cluster.tripple.IDetectorInfo;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
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
   private Optional<TrippleDetectorCluster> detectorClusterOpt;

   public MoveablePainter(Moveable moveable, Color color, int height, int width, MoveablePainterConfig config) {
      super(moveable, color, height, width);
      detectorColor = Color.MAGENTA.brighter();

      this.drawDetector = config.isDrawDetector();
      this.drawMoveableDirection = config.isDrawMoveableDirection();
      this.detectorRange = config.getDetectorReach();
      this.detectorAngle = config.getDetectorAngle();
      this.evasionAngle = config.getEvasionAngle();
      this.evasionRange = config.getEvasionReach();
      this.detectorClusterOpt = config.getDetectorCluster();
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();

      Position gridElemPos = Positions.of(getValue().getPosition());

      drawDetectorOrDetectorCluster(graphics);
      drawMoveableDirection(graphics, gridElemPos);
   }

   private void drawDetectorOrDetectorCluster(Graphics graphics) {
      if (drawDetector) {
         Position furthermostFrontPosition = Positions.of(getValue().getFurthermostFrontPosition());
         if (detectorClusterOpt.isPresent()) {
            drawDetectorCluster(graphics, detectorClusterOpt.get(), furthermostFrontPosition, detectorColor);
         } else {
            drawDetector(graphics, detectorColor, detectorAngle, detectorRange, evasionAngle, evasionRange, furthermostFrontPosition);
         }
      }
   }

   private static void drawDetectorCluster(Graphics graphics, TrippleDetectorCluster detectorCluster, Position furthermostFrontPosition,
         Color detectorColor) {
      drawSingleDetector4Cluster(graphics, furthermostFrontPosition, detectorColor, detectorCluster.getCenterDetector());
      drawSingleDetector4Cluster(graphics, furthermostFrontPosition, detectorColor, detectorCluster.getRightSideDetector());
      drawSingleDetector4Cluster(graphics, furthermostFrontPosition, detectorColor, detectorCluster.getLeftSideDetector());
   }

   private static void drawSingleDetector4Cluster(Graphics graphics, Position furthermostFrontPosition, Color detectorColor,
         IDetectorInfo iDetectorInfo) {
      IDetector detector = iDetectorInfo.getDetector();
      Position detectorPos = getCurrentDetectorPos(furthermostFrontPosition, detector, iDetectorInfo.getOffsetAngle());
      drawDetector(graphics, detectorColor, (int) detector.getDetectorAngle(), detector.getDetectorRange(),
            (int) detector.getEvasionAngle(),
            detector.getEvasionRange(), detectorPos);
   }

   private static Position getCurrentDetectorPos(Position furthermostFrontPosition, Detector detector, double degree) {
      Position detectorPos = Positions.of(furthermostFrontPosition);
      detectorPos.rotate(degree);
      return detectorPos;
   }

   private static void drawDetector(Graphics graphics, Color detectorColor, int detectorAngle, int detectorRange, int evasionAngle,
         int evasionRange, Position detectorPos) {
      Color evasionColor = new Color(detectorColor.getRed(), detectorColor.getGreen(), detectorColor.getBlue()).darker();
      drawDetectorArc(graphics, detectorPos, detectorAngle, detectorRange, detectorColor);
      drawDetectorArc(graphics, detectorPos, evasionAngle, evasionRange, evasionColor);
   }

   private static void drawDetectorArc(Graphics graphics, Position detectorPos, int arcAngle, int arcRadius, Color color) {
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
