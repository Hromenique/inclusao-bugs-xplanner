package com.technoetic.xplanner.actions;

import java.util.Date;
import static org.easymock.EasyMock.*;
import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.util.TimeGenerator;

public class TestStartIterationAction extends AbstractIterationStatusTestCase {
   TimeGenerator mockTimeGenerator;
   private Date samplingDate;

   public void setUp() throws Exception {
      action = new StartIterationAction();
      super.setUp();
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      ((StartIterationAction) action).setTimeGenerator(mockTimeGenerator);
      samplingDate = new Date();
      event.setWhen(samplingDate);
   }

   public void testExecuteWith1StartedIterationAsksForConfirmation() throws Exception {
      Iteration startedIteration = new Iteration();
      startedIteration.start();
      project.getIterations().add(startedIteration);
      editorForm.setIterationStartConfirmed(false);
      iteration.setStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);

      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.INACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);

   }

   public void testExecuteWith0StartedIterationStartsIteration() throws Exception
   {
      editorForm.setIterationStartConfirmed(false);
      iteration.setStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);
      event.setAction(HistoricalEvent.ITERATION_STARTED);
      expect(mockSession.save(event)).andReturn(null);
      expect(mockTimeGenerator.getCurrentTime()).andReturn(samplingDate);
      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.ACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);

   }

   public void testExecuteAfterConfirmationStartsIteration() throws Exception
   {

      editorForm.setIterationStartConfirmed(true);
      iteration.setStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);

      event.setAction(HistoricalEvent.ITERATION_STARTED);
      expect(mockTimeGenerator.getCurrentTime()).andReturn(samplingDate);
      expect(mockSession.save(event)).andReturn(null);

      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.ACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);
   }

}

