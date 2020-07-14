/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static java.lang.Math.max;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.battle.weapon.tank.countermeasure.DecoyFlareFactory;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.exception.GridElementOutOfBoundsException;
import com.myownb3.piranha.core.grid.filter.GridElementFilter;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.MathUtil;

/**
 * The most simple implementation of a {@link Grid} which simply moves a
 * {@link Position} forward and backward
 * 
 * @author Dominic
 *
 */
public class DefaultGrid implements Grid {

   private List<GridElement> gridElements;
   private boolean checkLowerBoundarys;
   protected int maxX;
   protected int maxY;
   protected int minX;
   protected int minY;
   private CollisionDetectionHandler collisionDetectionHandler;
   private Object lock = new Object();

   /**
    * Creates a new {@link Grid} with the given maximal, minimal-x and maximal,
    * minimal -y values
    * @param maxX
    *        the maximal x-axis value
    * @param maxY
    *        the maximal y-axis value
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   protected DefaultGrid(int maxX, int maxY, CollisionDetectionHandler collisionDetectionHandler) {
      this(maxX, maxY, 0, 0, collisionDetectionHandler);
      this.checkLowerBoundarys = false;
   }

   /**
    * Creates a new {@link Grid} with the given maximal, minimal-x and maximal,
    * minimal -y values
    * @param maxX
    *        the maximal x-axis value
    * @param maxY
    *        the maximal y-axis value
    * @param minX
    *        the minimal x-axis value
    * @param minY
    *        the minimal y-axis value
    * @param collisionDetectionHandler
    *        the {@link CollisionDetectionHandler} which handles a collision
    */
   protected DefaultGrid(int maxX, int maxY, int minX, int minY, CollisionDetectionHandler collisionDetectionHandler) {
      this.maxY = maxY;
      this.maxX = maxX;
      this.minY = minY;
      this.minX = minX;
      this.checkLowerBoundarys = true;
      gridElements = new ArrayList<>();
      this.collisionDetectionHandler = requireNonNull(collisionDetectionHandler);
   }

   @Override
   public void prepare() {
      ProjectileFactory.INSTANCE.registerGrid(this);
      DecoyFlareFactory.INSTANCE.registerGrid(this);
   }

   @Override
   public void remove(GridElement gridElement) {
      synchronized (lock) {
         gridElements.remove(gridElement);
      }
   }

   /**
    * Moves the position of this {@link Position} backward by 1 unit
    * 
    * @param position
    *        the current Position to move backward
    * @return the new moved Position
    */
   @Override
   public Position moveBackward(GridElement gridElement) {

      Position position = gridElement.getPosition();
      Direction direction = position.getDirection();
      double newX = getNewXValue(gridElement, direction.getBackwardX());
      double newY = getNewYValue(gridElement, direction.getBackwardY());
      checkBounds(newX, newY);
      CollisionDetectionResult collisionDetectionResult = checkCollision(gridElement, Positions.of(direction, newX, newY, position.getZ()));
      return collisionDetectionResult.getMovedPosition();
   }

   private CollisionDetectionResult checkCollision(GridElement gridElement, Position newPosition) {
      List<GridElement> gridElements2Check =
            getGridElements4CollisionCheckWithinDistanceInternal(gridElement, getCollisionCheckDistance(gridElement));
      return gridElement.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   private double getCollisionCheckDistance(GridElement gridElement) {
      return gridElement.getDimensionInfo().getDimensionRadius() + 2 * gridElement.getSmallestStepWith();
   }

   /**
    * Moves the position of this {@link Position} forward by 1 unit
    * 
    * @param position
    *        the current Position to move forward
    * @return the new moved Position
    */
   @Override
   public Position moveForward(GridElement gridElement) {

      Position position = gridElement.getPosition();
      Direction direction = position.getDirection();
      double newX = getNewXValue(gridElement, direction.getForwardX());
      double newY = getNewYValue(gridElement, direction.getForwardY());
      checkBounds(newX, newY);
      CollisionDetectionResult collisionDetectionResult = checkCollision(gridElement, Positions.of(direction, newX, newY, position.getZ()));
      return collisionDetectionResult.getMovedPosition();
   }

   @Override
   public void addElement(GridElement gridElement) {
      checkBounds(gridElement.getPosition());
      synchronized (lock) {
         gridElements.add(gridElement);
      }
   }

   /**
    * @param gridElement
    * @param forwardY
    *        the amount of forward units for the y-axis
    * @return
    */
   protected double getNewYValue(GridElement gridElement, double forwardY) {
      Position position = gridElement.getPosition();
      return getNewYValue(position, forwardY);
   }

   protected double getNewYValue(Position position, double forwardY) {
      return position.getY() + forwardY;
   }

   /**
    * @param position
    * @param forwardX
    *        the amount of forward units for the x-axis
    * @return the new x-value
    */
   protected double getNewXValue(GridElement gridElement, double forwardX) {
      Position position = gridElement.getPosition();
      return getNewXValue(position, forwardX);
   }

   protected double getNewXValue(Position position, double forwardX) {
      return position.getX() + forwardX;
   }

   @Override
   public List<GridElement> getAllGridElements(GridElement gridElement) {
      synchronized (lock) {
         return gridElements.stream()
               .filter(DestructionHelper::isNotDestroyed)
               .filter(currenGridEl -> !currenGridEl.equals(gridElement))
               .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
      }
   }

   @Override
   public List<GridElement> getAllGridElementsWithinDistance(Position position, int distance) {
      GridElementFilter gridElementFilter = GridElementFilter.of(position, distance);
      return getAllGridElements(null).stream()
            .filter(gridElementFilter::isGridElementWithinDistance)
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
   }

   @Override
   public List<GridElement> getAllAvoidableGridElementsWithinDistance(GridElement gridElement, int distance) {
      return getGridElements4CollisionCheckWithinDistanceInternal(gridElement, distance);
   }

   private List<GridElement> getGridElements4CollisionCheckWithinDistanceInternal(GridElement gridElement, double distance) {
      Position gridElemPos = gridElement.getForemostPosition();
      GridElementFilter gridElementFilter = GridElementFilter.of(gridElement, gridElemPos, distance);
      return getAllGridElements(gridElement).stream()
            .filter(GridElement::isAvoidable)
            .filter(gridElementFilter::isGridElementWithinDistance)
            .filter(gridElementFilter::isGridElementInfrontOf)
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
   }

   @Override
   public Dimension getDimension() {
      return new DimensionImpl(minX, minY, maxX - minX, maxY - minY);
   }

   private void checkBounds(Position position) {
      checkBounds(position.getX(), position.getY());
   }

   /**
    * Verifies whether or not the given coordinates are within the bounds
    * 
    * @param newX
    * @param newY
    */
   private void checkBounds(double newX, double newY) {
      if (outOfUpperBounds(newY, newX) || outOfLowerBounds(newY, newX)) {
         throw new GridElementOutOfBoundsException(
               "The bounds '" + newX + "', '" + newY + "' are out of bounds for this Grid '\n" + this);
      }
   }

   private boolean outOfLowerBounds(double newY, double newX) {
      return checkLowerBoundarys && (newY < minY || newX < minX);
   }

   private boolean outOfUpperBounds(double newY, double newX) {
      return newY > maxY || newX > maxX;
   }

   /**
    * @return a new {@link Position} within the borders of this {@link Grid}
    */
   @Override
   public Position getRandomPosition(double maxDimensionRadius) {
      double offset = getOffsetFromMinOrMax(maxDimensionRadius);
      double x = max(MathUtil.getRandom(maxX - offset), minX + offset);
      double y = max(MathUtil.getRandom(maxY - offset), minY + offset);
      return Positions.of(x, y);
   }

   private double getOffsetFromMinOrMax(double maxDimensionRadius) {
      return maxDimensionRadius;
   }

   @Override
   public String toString() {
      return "Max x:'" + maxX + ", Min x:'" + minX + "; Max y:'" + maxY + ", Min y:'" + minY;
   }

   public abstract static class AbstractGridBuilder<T> {

      protected Integer maxX;
      protected Integer maxY;
      protected Integer minX;
      protected Integer minY;
      protected CollisionDetectionHandler collisionDetectionHandler;

      protected AbstractGridBuilder() {}

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withMaxX(int maxX) {
         this.maxX = maxX;
         return (B) this;
      }

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withMinX(int minX) {
         this.minX = minX;
         return (B) this;
      }

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withMaxY(int maxY) {
         this.maxY = maxY;
         return (B) this;
      }

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withMinY(int minY) {
         this.minY = minY;
         return (B) this;
      }

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withDefaultCollisionDetectionHandler() {
         this.collisionDetectionHandler = new DefaultCollisionDetectionHandlerImpl();
         return (B) this;
      }

      public abstract T build();

      @SuppressWarnings("unchecked")
      public <B extends AbstractGridBuilder<T>> B withCollisionDetectionHandler(
            CollisionDetectionHandler collisionDetectionHandler) {
         this.collisionDetectionHandler = collisionDetectionHandler;
         return (B) this;
      }

      protected void setDefaultCollisionDetectionHandlerIfNull() {
         if (isNull(collisionDetectionHandler)) {
            // We better have always a collisionDetectionHandler
            collisionDetectionHandler = new DefaultCollisionDetectionHandlerImpl();
         }
      }
   }

   public static class GridBuilder extends AbstractGridBuilder<DefaultGrid> {

      protected GridBuilder() {
         super();
      }

      public static GridBuilder builder() {
         return new GridBuilder()
               .withMaxX(10)
               .withMaxY(10);
      }

      public static GridBuilder builder(int maxX, int maxY) {
         return new GridBuilder()
               .withMaxX(maxX)
               .withMaxY(maxY);
      }

      @Override
      public DefaultGrid build() {
         Objects.requireNonNull(maxX, "We need a max x value!");
         Objects.requireNonNull(maxY, "We need a max y value!");
         DefaultGrid defaultGrid;
         setDefaultCollisionDetectionHandlerIfNull();
         if (isNull(minX) || isNull(minY)) {
            defaultGrid = new DefaultGrid(maxX, maxY, collisionDetectionHandler);
         } else {
            defaultGrid = new DefaultGrid(maxX, maxY, minX, minY, collisionDetectionHandler);
         }
         return defaultGrid;
      }
   }
}
