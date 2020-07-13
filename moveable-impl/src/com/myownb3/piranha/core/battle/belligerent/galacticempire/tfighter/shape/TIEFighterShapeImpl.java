package com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape;

import static com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil.combinePath;

import java.util.Arrays;
import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.TIEFighterShape;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

public class TIEFighterShapeImpl extends AbstractShape implements TIEFighterShape {

   private Circle ballCockpit;
   private Rectangle rightWing;
   private Rectangle leftWing;

   private TIEFighterShapeImpl(Circle ballCockpit, Rectangle rightWing, Rectangle leftWing) {
      super(combinePath(ballCockpit, rightWing, leftWing), ballCockpit.getCenter());
      this.ballCockpit = ballCockpit;
      this.rightWing = rightWing;
      this.leftWing = leftWing;
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ShapeUtil.setGridElement(gridElement, ballCockpit, leftWing, rightWing);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return ShapeUtil.check4Collision(Arrays.asList(ballCockpit, leftWing, rightWing), collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      ballCockpit.transform(position);
      rightWing.transform(getWingCenter(ballCockpit, -90));
      leftWing.transform(getWingCenter(ballCockpit, 90));
      this.path = combinePath(ballCockpit, rightWing, leftWing);
   }

   @Override
   public Position getForemostPosition() {
      return ballCockpit.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return ballCockpit.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return ballCockpit.getDimensionRadius() + leftWing.getWidth() + rightWing.getWidth();
   }

   @Override
   public Circle getBallCockpit() {
      return ballCockpit;
   }

   @Override
   public Rectangle getRightWing() {
      return rightWing;
   }

   @Override
   public Rectangle getLeftWing() {
      return leftWing;
   }

   private static Position getWingCenter(Circle ballCockpit, double angle) {
      double dimensionRadius = ballCockpit.getDimensionRadius();
      double width = dimensionRadius / 3d;
      return ballCockpit.getCenter()
            .rotate(angle)
            .movePositionForward4Distance(dimensionRadius + (width / 2d));
   }

   public static class TIEFighterShapeBuilder {
      private Circle ballCockpit;

      private TIEFighterShapeBuilder() {
         // private
      }

      public TIEFighterShapeBuilder withBallCockpit(Circle ballCockpit) {
         this.ballCockpit = ballCockpit;
         return this;
      }

      public TIEFighterShape build() {
         Rectangle rightWing = buildRightWing(ballCockpit);
         Rectangle leftWing = buildLeftWing(ballCockpit);
         return new TIEFighterShapeImpl(ballCockpit, rightWing, leftWing);
      }

      private static Rectangle buildLeftWing(Circle ballCockpit) {
         Position wingShapeCenter = getWingCenter(ballCockpit, 90);
         return buildWing(ballCockpit, wingShapeCenter);
      }

      private static Rectangle buildRightWing(Circle ballCockpit) {
         Position wingShapeCenter = getWingCenter(ballCockpit, -90);
         return buildWing(ballCockpit, wingShapeCenter);
      }

      private static Rectangle buildWing(Circle ballCockpit, Position wingShapeCenter) {
         double dimensionRadius = ballCockpit.getDimensionRadius();
         return RectangleBuilder.builder()
               .withCenter(wingShapeCenter)
               .withWidth(dimensionRadius / 3d)
               .withHeight(dimensionRadius * 3d)
               .withOrientation(Orientation.VERTICAL)
               .build();
      }

      public static TIEFighterShapeBuilder builder() {
         return new TIEFighterShapeBuilder();
      }
   }

}
