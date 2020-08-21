package com.myownb3.piranha.application.battle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.myownb3.piranha.application.battle.MoveableAdder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

public class MultiMoveableAdderImpl implements MoveableAdder {
   private List<MoveableAdder> moveableAdders;
   private boolean isAsync;

   private MultiMoveableAdderImpl(List<MoveableAdder> moveableAdders, boolean isAsync) {
      this.moveableAdders = moveableAdders;
      this.isAsync = isAsync;
   }

   @Override
   public void incrementCounter() {
      moveableAdders
            .forEach(MoveableAdder::incrementCounter);
   }

   @Override
   public boolean isCycleDone() {
      return getMoveableAdderStream()
            .anyMatch(MoveableAdder::isCycleDone);
   }

   @Override
   public List<GridElement> check4NewMoveables2Add(Grid grid, EvasionStateMachineConfig evasionStateMachineConfig) {
      return getMoveableAdderStream()
            .filter(MoveableAdder::isCycleDone)
            .map(moveableAdder -> moveableAdder.check4NewMoveables2Add(grid, evasionStateMachineConfig))
            .flatMap(List::stream)
            .collect(Collectors.toList());
   }

   private Stream<MoveableAdder> getMoveableAdderStream() {
      return isAsync ? moveableAdders.parallelStream() : moveableAdders.stream();
   }

   public static class MultiMovealbeAdderBuilder {
      private List<MoveableAdder> moveableAdders;
      private boolean isAsync;

      private MultiMovealbeAdderBuilder() {
         moveableAdders = new ArrayList<>();
      }

      public MultiMovealbeAdderBuilder addMoveableAdder(MoveableAdder moveableAdder) {
         moveableAdders.add(moveableAdder);
         return this;
      }

      public MultiMovealbeAdderBuilder setAsync() {
         this.isAsync = true;
         return this;
      }

      public MultiMoveableAdderImpl build() {
         return new MultiMoveableAdderImpl(moveableAdders, isAsync);
      }

      public static MultiMovealbeAdderBuilder builder() {
         return new MultiMovealbeAdderBuilder();
      }
   }
}
