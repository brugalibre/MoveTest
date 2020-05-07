/**
 * 
 */
package com.myownb3.piranha.ui.render.impl.detector;

import static com.myownb3.piranha.util.MathUtil.rotateVector;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Optional;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.detector.cluster.tripple.IDetectorInfo;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.config.DetectorConfig;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.AbstractGridElementPainter;
import com.myownb3.piranha.ui.render.impl.GraphicsContext;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class DetectorPainter extends AbstractGridElementPainter<GridElement> {

   private int evasionRange;
   private double evasionAngle;
   private int detectorRange;
   private double detectorAngle;
   private Color detectorColor;
   private Optional<TrippleDetectorCluster> detectorClusterOpt;

   public DetectorPainter(GridElement detectorGridElem, Color color, int height, int width, DetectorPainterConfig painterConfig) {
      super(detectorGridElem, color, height, width);
      detectorColor = Color.MAGENTA.brighter();

      DetectorConfig config = painterConfig.getDetectorConfig();
      this.detectorRange = config.getDetectorReach();
      this.detectorAngle = config.getDetectorAngle();
      this.evasionAngle = config.getEvasionAngle();
      this.detectorClusterOpt = painterConfig.getDetectorCluster();
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      super.render(graphicsCtx);
      GraphicsContext context = (GraphicsContext) graphicsCtx;
      Graphics graphics = context.getGraphics();

      drawDetectorOrDetectorCluster(graphics);
   }

   private void drawDetectorOrDetectorCluster(Graphics graphics) {
      Position furthermostFrontPosition = getValue().getFurthermostFrontPosition();
      if (detectorClusterOpt.isPresent()) {
         drawDetectorCluster(graphics, detectorClusterOpt.get(), furthermostFrontPosition, detectorColor);
      } else {
         drawDetector(graphics, detectorColor, detectorAngle, detectorRange, evasionAngle, evasionRange, furthermostFrontPosition);
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
      Position detectorPos = furthermostFrontPosition.rotate(iDetectorInfo.getOffsetAngle());
      drawDetector(graphics, detectorColor, (int) detector.getDetectorAngle(), detector.getDetectorRange(),
            (int) detector.getEvasionAngle(),
            detector.getEvasionRange(), detectorPos);
   }

   private static void drawDetector(Graphics graphics, Color detectorColor, double detectorAngle, int detectorRange, double evasionAngle,
         int evasionRange, Position detectorPos) {
      Color evasionColor = new Color(detectorColor.getRed(), detectorColor.getGreen(), detectorColor.getBlue()).darker();
      drawDetectorArc(graphics, detectorPos, detectorAngle, detectorRange, detectorColor);
      drawDetectorArc(graphics, detectorPos, evasionAngle, evasionRange, evasionColor);
   }

   private static void drawDetectorArc(Graphics graphics, Position detectorPos, double arcAngle, int arcRadius, Color color) {
      graphics.setColor(color);
      Direction detectorDirection = detectorPos.getDirection();
      int startAngle = (int) arcAngle / 2;
      for (int curAngle = startAngle; curAngle >= -startAngle; curAngle--) {
         Float64Vector detectorDirectionVector = rotateVector(getVector(detectorDirection), curAngle);
         drawDirectionFromPosition(graphics, detectorPos, detectorDirectionVector, arcRadius);
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
