package com.myownb3.piranha.core.grid.gridelement.wall;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement.WallGridElementBuilder;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link WallBuilder} is used to build single {@link WallGridElement} which are combined to a chained {@link Wall}
 * 
 * @author DStalder
 *
 */
public class WallBuilder {

   private LinkedList<WallGridElement> wallSegments;
   private Position wallSegmentPos;
   private Grid grid;
   private Double wallSegmentWidth;
   private double wallHealth;

   private WallBuilder() {
      this.wallSegments = new LinkedList<>();
      this.wallHealth = Integer.MAX_VALUE;
   }

   public WallBuilder withGrid(Grid grid) {
      this.grid = grid;
      return this;
   }

   public WallBuilder withWallStartPos(Position wallStartPos) {
      this.wallSegmentPos = wallStartPos;
      return this;
   }

   public WallBuilder withWallSegmentWidth(double wallSegmentWidth) {
      this.wallSegmentWidth = wallSegmentWidth;
      return this;
   }

   public WallBuilder withWallHealth(double wallHealth) {
      this.wallHealth = wallHealth;
      return this;
   }

   public WallBuilder addWallSegment(double wallSegmentLength) {
      requireNonNull(wallSegmentPos, "Call first 'withWallStartPos()' before adding wall segments!");
      requireNonNull(wallSegmentWidth, "Call first 'withWallSegmentWidth()' bevor adding wall segments!");
      requireNonNull(grid, "Call first 'withGrid()' bevor adding wall segments!");
      wallSegmentPos = wallSegmentPos.movePositionBackward4Distance(wallSegmentLength);
      wallSegments.add(WallGridElementBuilder.builder()
            .withGrid(grid)
            .withHealth(wallHealth)
            .withShape(RectangleBuilder.builder()
                  .withHeight(wallSegmentLength)
                  .withWidth(wallSegmentWidth)
                  .withCenter(wallSegmentPos)
                  .build())
            .build());
      return this;
   }

   public WallBuilder rotate(double angle) {
      requireNonNull(wallSegmentPos, "Call first 'withWallStartPos()' before rotating wall segments!");
      Rectangle lastWallRectangle = getLastWallRectangle();
      double distance2Move = calcDistance2Move(lastWallRectangle);
      wallSegmentPos = wallSegmentPos.movePositionBackward4Distance(lastWallRectangle.getHeight() / 2d)
            .rotate(angle)
            .movePositionForward4Distance(distance2Move);
      return this;
   }

   private double calcDistance2Move(Rectangle lastWallRectangle) {
      double distanceFromRectangle2CurrentPos = lastWallRectangle.getForemostPosition().calcDistanceTo(wallSegmentPos);
      return distanceFromRectangle2CurrentPos + lastWallRectangle.getWidth() / 2d;
   }

   public WallBuilder addGap(double gapWidth) {
      wallSegmentPos = wallSegmentPos.movePositionBackward4Distance(gapWidth);
      return this;
   }

   private Rectangle getLastWallRectangle() {
      WallGridElement lastWall = wallSegments.getLast();
      return (Rectangle) lastWall.getShape();
   }

   public List<WallGridElement> build() {
      return Collections.unmodifiableList(wallSegments);
   }

   public static WallBuilder builder() {
      return new WallBuilder();
   }
}
