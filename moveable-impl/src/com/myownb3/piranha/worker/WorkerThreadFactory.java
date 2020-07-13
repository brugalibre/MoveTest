package com.myownb3.piranha.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WorkerThreadFactory {

   public static final WorkerThreadFactory INSTANCE = new WorkerThreadFactory();
   private ExecutorService executorService;

   private WorkerThreadFactory() {
      restart();
   }

   /**
    * Executes the given {@link Runnable} with the given amount of {@link TimeUnit#MILLISECONDS}
    * 
    * @param <V>
    * 
    * @param callable
    *        the {@link Callable} which will be executed
    * @return a {@link ScheduledFuture}
    */
   public <V> Future<V> executeAsync(Callable<V> callable) {
      return executorService.submit(callable);
   }

   /**
    * Re-Starts this {@link WorkerThreadFactory} after it was shutdown earlier
    */
   public void restart() {
      executorService = Executors.newCachedThreadPool();
   }

   /**
    * Shuts this {@link WorkerThreadFactory} down
    */
   public void shutdown() {
      executorService.shutdown();
   }
}
