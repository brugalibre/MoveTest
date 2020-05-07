package com.myownb3.piranha.launch;

import java.util.List;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.maze.corridor.CorridorDetector;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.ui.render.Renderer;

public class MazePostMoveForwardHandler extends DefaultPostMoveForwardHandler {

   private boolean hasMoveableDetected;
   private DetectorHolder detectorHolder;

   public MazePostMoveForwardHandler(DetectorHolder detectorHolder, MainWindowHolder windowHolder, MoveableControllerHolder moveableControllerHolder,
         List<GridElement> gridElements, List<Renderer> renderers) {
      super(windowHolder, moveableControllerHolder, gridElements, renderers);
      this.detectorHolder = detectorHolder;
   }

   @Override
   public void handlePostMoveForward(MoveResult moveResult) {

      Detector detector = detectorHolder.corridorDetector.getDetector();
      Moveable moveable = getMoveableController().getMoveable();
      if (!hasMoveableDetected) {
         detectMoveable(detector, moveable);
         hasMoveableDetected = detector.hasObjectDetected(moveable);
      } else {
         detectMoveable(detector, moveable);
         boolean isNotDetectedNow = detector.hasObjectDetected(moveable);
         if (!isNotDetectedNow) {
            getMoveableController().stop();
         }
      }
   }

   private void detectMoveable(Detector detector, Moveable moveable) {
      detector.detectObject(moveable, moveable.getFurthermostBackPosition(), detectorHolder.corridorDetector.getPosition());
   }

   public static class DetectorHolder {
      private CorridorDetector corridorDetector;

      public void setDetector(CorridorDetector corridorDetector) {
         this.corridorDetector = corridorDetector;
      }
   }

}
