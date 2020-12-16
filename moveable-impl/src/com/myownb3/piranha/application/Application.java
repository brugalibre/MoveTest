package com.myownb3.piranha.application;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.worker.WorkerThreadFactory;

/**
 * Defines the default Application
 * 
 * @author Dominic
 *
 */
public interface Application {
   /**
    * Starts this {@link Application} application
    */
   public void run();

   /**
    * Prepares this {@link Application}
    * This includes e.g. preparing the {@link Grid} and or the {@link WorkerThreadFactory}
    */
   void prepare();
}
