package com.myownb3.piranha.core.grid.filter;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.MathUtil;

/**
 * The {@link GridElementFilter} is used by the {@link Grid} in order to filter relevant {@link GridElement}s for collision detecting
 * or for {@link Detector}s
 * 
 * @author Dominic
 *
 */
public class GridElementFilter {
   private GridElement movedGridElement;
   private double distance;
   private Position gridElemPos;

   private GridElementFilter(GridElement movedGridElement, Position gridElemPos, double distance) {
      this.movedGridElement = movedGridElement;
      this.gridElemPos = gridElemPos;
      this.distance = distance;
   }

   /**
    * Verifies if the moved {@link GridElement} of this helper is behind an other {@link GridElement} to check
    * 
    * @return <code>true</code> if the {@link GridElement} to check if in front of the given one or <code>false</code> if not
    */
   public boolean isGridElementInfrontOf(GridElement gridElement2Check) {
      requireNonNull(movedGridElement, "For using this filter we need a GridElement to compare to!");
      double angleBetweenPositions = MathUtil.calcAngleBetweenPositions(movedGridElement.getPosition(), gridElement2Check.getPosition());
      return angleBetweenPositions < 180.0;
   }

   /**
    * Verifies if the moved {@link GridElement} of this helper is within the given distance to another {@link GridElement}
    * 
    * @return <code>true</code> if the {@link GridElement} to check if within the given distance of the other one or <code>false</code> if
    *         not
    */
   public boolean isGridElementWithinDistance(GridElement gridElement) {
      double movedGridElement2GridElemDistance = calcDistanceFromGridElement2Pos(gridElemPos, gridElement);
      return movedGridElement2GridElemDistance <= distance;
   }

   /*
    * We have to subtract the 'Dimension-Radius' from the calculated distance. 
    */
   private static double calcDistanceFromGridElement2Pos(Position gridElemPos, GridElement otherGridElement) {
      return gridElemPos.calcDistanceTo(otherGridElement.getPosition()) - otherGridElement.getDimensionInfo().getDimensionRadius();
   }

   /**
    * Creates a new {@link GridElementFilter} with the given filter criterias
    * 
    * @param movedGridElement
    *        the {@link GridElement} which was moved on the {@link Grid} and for which there are other {@link GridElement}s
    *        to filter
    * @param gridElemPos
    *        the {@link Position} of the {@link GridElement} above. This {@link Position} may differ from the
    *        {@link GridElement#getPosition()}
    * @param distance
    *        the distance to filter other {@link GridElement}s
    * @return a new {@link GridElementFilter}
    */
   public static GridElementFilter of(GridElement movedGridElement, Position gridElemPos, Double distance) {
      return new GridElementFilter(movedGridElement, gridElemPos, distance);
   }

   /**
    * Creates a new {@link GridElementFilter} with the given {@link Position} of a {@link GridElement} and filter distance
    * 
    * <b> Note </b> That with this constructor calling the methods {@link GridElementFilter#isGridElementInfrontOf(GridElement)}
    * or {@link GridElementFilter#isGridElementNotMovingFurtherAway(GridElement)} will cause an {@link NullPointerException} to be thrown
    * 
    * @param gridElemPos
    *        the {@link Position} of the {@link GridElement} above. This {@link Position} may differ from the
    *        {@link GridElement#getPosition()}
    * @param distance
    *        the distance to filter other {@link GridElement}s
    * @return a new {@link GridElementFilter}
    */
   public static GridElementFilter of(Position gridElemPos, double distance) {
      return new GridElementFilter(null, gridElemPos, distance);
   }
}
