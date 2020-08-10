package com.myownb3.piranha.core.grid.gridelement.shape.rectangle;

import static com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleUtil.getNextPosition;
import static com.myownb3.piranha.core.grid.position.Positions.buildPositionsBetweenTwoPositions;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.RectanglePathBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.DefaultRectanglePathBuilderImpl;
import com.myownb3.piranha.core.grid.position.Position;

public class RectangleImpl extends AbstractShape implements Rectangle {

   private static final long serialVersionUID = 7252451429027726834L;
   private double height;
   private double width;
   private Orientation orientation;
   private double distanceBetweenPosOnColDetectionPath;
   private List<Position> path4Detection;
   private RectanglePathBuilder rectanglePathBuilder;

   private RectangleImpl(Position center, double width, double height, double distanceBetweenPosOnColDetectionPath,
         RectanglePathBuilder rectanglePathBuilder) {
      this(center, width, height, distanceBetweenPosOnColDetectionPath, Orientation.HORIZONTAL, rectanglePathBuilder);
   }

   private RectangleImpl(Position center, double width, double height, double distanceBetweenPosOnColDetectionPath, Orientation orientation,
         RectanglePathBuilder rectanglePathBuilder) {
      super(rectanglePathBuilder.buildRectanglePath(center, orientation, width, height), center);
      this.height = height;
      this.width = width;
      this.orientation = orientation;
      this.rectanglePathBuilder = rectanglePathBuilder;
      this.distanceBetweenPosOnColDetectionPath = distanceBetweenPosOnColDetectionPath;
      this.collisionDetector = buildCollisionDetector();
      this.path4Detection = buildPath4DetectionPrivate();
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return collisionDetector.checkCollision(collisionDetectionHandler, gridElement, center, newPosition, gridElements2Check);
   }

   @Override
   public Position getForemostPosition() {
      return shiftPositionForInternal(center);
   }

   private Position shiftPositionForInternal(Position furthermostFrontPos) {
      switch (orientation) {
         case HORIZONTAL:
            return furthermostFrontPos.movePositionForward4Distance(height / 2);
         case VERTICAL:
            return furthermostFrontPos.movePositionForward4Distance(width / 2);
         default:
            throw new IllegalStateException("Unknown Orientation '" + orientation + "'!");
      }
   }

   @Override
   public Position getRearmostPosition() {
      Position furthermostFrontPos = center.rotate(180);
      return shiftPositionForInternal(furthermostFrontPos);
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(width, height);
   }

   @Override
   public void transform(Position position) {
      super.transform(position);
      this.path = rectanglePathBuilder.buildRectanglePath(position, orientation, width, height);
      this.path4Detection = buildPath4DetectionPrivate();
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return path4Detection;
   }

   private List<Position> buildPath4DetectionPrivate() {
      Set<Position> path4DetectionSet = new LinkedHashSet<>();
      Position pathPos1 = path.get(0).getBegin();
      for (int i = 0; i < path.size(); i++) {
         Position pathPos2 = getNextPosition(path, i);
         path4DetectionSet.addAll(buildPositionsBetweenTwoPositions(pathPos1, pathPos2, distanceBetweenPosOnColDetectionPath));
         pathPos1 = pathPos2;
      }
      return new ArrayList<>(path4DetectionSet);
   }

   @Override
   public double getHeight() {
      return height;
   }

   @Override
   public double getWidth() {
      return width;
   }

   @Override
   public Position getUpperLeftPosition() {
      return path.get(1).getBegin();
   }

   @Override
   public Orientation getOrientation() {
      return orientation;
   }

   public static class RectangleBuilder {
      private Position center;
      private double height;
      private double width;
      private Orientation orientation;
      private double distanceBetweenPosOnColDetectionPath;
      private RectanglePathBuilder rectanglePathBuilder;

      private RectangleBuilder() {
         distanceBetweenPosOnColDetectionPath = 1;
         rectanglePathBuilder = new DefaultRectanglePathBuilderImpl();
      }

      public RectangleBuilder withCenter(Position center) {
         this.center = center;
         return this;
      }

      public RectangleBuilder withOrientation(Orientation orientation) {
         this.orientation = orientation;
         return this;
      }

      public RectangleBuilder withWidth(double width) {
         this.width = width;
         return this;
      }

      public RectangleBuilder withHeight(double height) {
         this.height = height;
         return this;
      }

      public RectangleBuilder withRectanglePathBuilder(RectanglePathBuilder rectanglePathBuilder) {
         this.rectanglePathBuilder = rectanglePathBuilder;
         return this;
      }

      public RectangleBuilder withDistanceBetweenPosOnColDetectionPath(double distanceBetweenPosOnColDetectionPath) {
         this.distanceBetweenPosOnColDetectionPath = distanceBetweenPosOnColDetectionPath;
         return this;
      }

      public Rectangle build() {
         if (nonNull(orientation)) {
            return new RectangleImpl(center, width, height, distanceBetweenPosOnColDetectionPath, orientation, rectanglePathBuilder);
         }
         return new RectangleImpl(center, width, height, distanceBetweenPosOnColDetectionPath, rectanglePathBuilder);
      }

      public static RectangleBuilder builder() {
         return new RectangleBuilder();
      }
   }
}
