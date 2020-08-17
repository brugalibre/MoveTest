package com.myownb3.piranha.worker;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class WorkerThreadExecutionExceptionTest {

   @Test
   void testWorkerThreadExecutionException() {

      // Given
      WorkerThreadExecutionException workerThreadExecutionException = new WorkerThreadExecutionException(new RuntimeException());

      // When
      Executable ex = () -> {
         throw workerThreadExecutionException;
      };

      // Then
      assertThrows(WorkerThreadExecutionException.class, ex);
   }

}
