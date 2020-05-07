package com.myownb3.piranha.detector.lightbarrier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.detector.PlacedDetectorImpl.PlacedDetectorBuilder;
import com.myownb3.piranha.detector.lightbarrier.LightBarrierImpl.LightBarrierBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

class LightBarrierTest {

   @Test
   void testUnsupportedStrategy() {

      // Given

      // When
      //      Ex
      LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(gridEle -> {
            })
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(Positions.of(4, 4))
                  .withIDetector(mock(IDetector.class))
                  .build())
            .build();

      // Then
   }

   @Test
   void testNoTriggerCallbackHandlerDetection() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
         hasVisitedList.add(true);
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = new SimpleGridElement(mock(Grid.class), position);
      IDetector detector = mock(IDetector.class);
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When

      // First-time, 
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(true));
   }

   @Test
   void testHappyCase_TriggerCallbackHandlerDetection() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
         hasVisitedList.add(true);
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = mock(GridElement.class);
      Position frontPosition = Positions.of(4, 4);
      Position backPosition = Positions.of(5, 5);
      when(gridElement.getFurthermostBackPosition()).thenReturn(backPosition);
      when(gridElement.getFurthermostFrontPosition()).thenReturn(frontPosition);

      IDetector detector = spy(IDetector.class);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When
      // First-time (detecting front pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Second time (detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Third time (not detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(false);
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(false));
      verify(detector).detectObject(eq(gridElement), eq(frontPosition), eq(position));
      verify(detector, times(2)).detectObject(eq(gridElement), eq(backPosition), eq(position));
   }

   @Test
   void testFullyDetected_ButStillDetecting() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = mock(GridElement.class);
      Position frontPosition = Positions.of(4, 4);
      Position backPosition = Positions.of(5, 5);
      when(gridElement.getFurthermostBackPosition()).thenReturn(backPosition);
      when(gridElement.getFurthermostFrontPosition()).thenReturn(frontPosition);

      IDetector detector = spy(IDetector.class);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When
      // First-time (detecting front pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Second time (detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Third time (still detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(true));
      verify(detector).detectObject(eq(gridElement), eq(frontPosition), eq(position));
      verify(detector, times(2)).detectObject(eq(gridElement), eq(backPosition), eq(position));
   }

   @Test
   void testEnteredButNotDetectingAnyMore() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = mock(GridElement.class);
      Position frontPosition = Positions.of(4, 4);
      Position backPosition = Positions.of(5, 5);
      when(gridElement.getFurthermostBackPosition()).thenReturn(backPosition);
      when(gridElement.getFurthermostFrontPosition()).thenReturn(frontPosition);

      IDetector detector = spy(IDetector.class);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When
      // First-time (detecting front pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);
      lightBarrier.checkGridElement(gridElement);

      // Second time (not detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(false);
      lightBarrier.checkGridElement(gridElement);

      // Third time (not detecting back pos)
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(false);
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(true));
      verify(detector).detectObject(eq(gridElement), eq(frontPosition), eq(position));
      verify(detector, times(2)).detectObject(eq(gridElement), eq(backPosition), eq(position));
   }

   @Test
   void testSecondTimeDetectingDoesNotTriggerCallbackHandler() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
         hasVisitedList.add(true);
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = new SimpleGridElement(mock(Grid.class), position);
      IDetector detector = mock(IDetector.class);
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(true);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When

      // First-time, 
      lightBarrier.checkGridElement(gridElement);
      // Second time
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(true));
   }

   @Test
   void testFirstTimeVisitButNotDetecting() {

      // Given
      List<Boolean> hasVisitedList = new ArrayList<>();
      LightBarrierPassedCallbackHandler callbackHandler = gridEle -> {
         hasVisitedList.add(true);
      };
      Position position = Positions.of(4, 4);
      GridElement gridElement = new SimpleGridElement(mock(Grid.class), position);
      IDetector detector = mock(IDetector.class);
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(false);

      LightBarrier lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(callbackHandler)
            .withPlacedDetector(PlacedDetectorBuilder.builder()
                  .withPosition(position)
                  .withIDetector(detector)
                  .build())
            .build();

      // When

      // First-time, 
      lightBarrier.checkGridElement(gridElement);

      // Then
      assertThat(hasVisitedList.isEmpty(), is(true));
   }

}
