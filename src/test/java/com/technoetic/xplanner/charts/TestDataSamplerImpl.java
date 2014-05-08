
/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import static org.easymock.EasyMock.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springframework.orm.hibernate.HibernateOperations;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

public class TestDataSamplerImpl extends AbstractUnitTestCase {
   DataSamplerImpl dataSampler;
   Iteration iteration;
   TimeGenerator timeGenerator;
   HibernateOperations mockHibernateOperations;
   Iteration activeIteration;
   private Date todayMidnight;
   private Date tomorrowMidnight;
   private Properties properties;
   private TimeGenerator mockTimeGenerator;
   private List estimatedHoursDataSamples = new ArrayList();
   private List actualHoursDataSamples = new ArrayList();
   private List remainingHoursDataSample = new ArrayList();

   public void tearDown() throws Exception {
      super.tearDown();
   }

   protected void setUp() throws Exception {
      super.setUp();
      properties = new Properties();
      setAutomaticallyExtendIterationEndDate(false);
      iteration = new Iteration();
      iteration.setId(99);
      timeGenerator = new TimeGenerator();
      mockHibernateOperations = createLocalMock(HibernateOperations.class);
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      dataSampler = new DataSamplerImpl();
      dataSampler.setProperties(properties);
      dataSampler.setTimeGenerator(timeGenerator);
      dataSampler.setHibernateOperations(mockHibernateOperations);
      todayMidnight = TimeGenerator.getMidnight(new Date());
      tomorrowMidnight = TimeGenerator.shiftDate(todayMidnight,
                                                 Calendar.DAY_OF_MONTH,
                                                 1);
      DataSample dataSampleToDel1 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "remainingHours",
                                        1.0);
      DataSample dataSampleToDel2 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "actualHours",
                                        1.0);
      DataSample dataSampleToDel3 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "estimatedHours",
                                        1.0);

      estimatedHoursDataSamples.add(dataSampleToDel3);
      actualHoursDataSamples.add(dataSampleToDel2);
      remainingHoursDataSample.add(dataSampleToDel1);

      activeIteration = new Iteration();
      activeIteration.setId(99);
      activeIteration.setStatus(IterationStatus.ACTIVE);
   }

   public void testGenerateOpeningDatasample() throws HibernateException {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateOpeningDataSamples(iteration);
      verify();
   }

   public void testGenerateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }

   public void testUpdateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }


   public void testGenerateClosingDatasampleOnIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 10));

      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();
   }

   public void testGenerateClosingDatasampleAfterIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 -10));
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();


   }

   public void testExtendIterationEndDateIfNeeded_TurnedOff() throws Exception {
      iteration.setEndDate(todayMidnight);

      setAutomaticallyExtendIterationEndDate(false);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsActive() throws Exception {
      iteration.setEndDate(todayMidnight);
      iteration.setStatus(IterationStatus.ACTIVE);
      setAutomaticallyExtendIterationEndDate(true);
      expect(mockHibernateOperations.save(iteration)).andReturn(null);
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(tomorrowMidnight, iteration.getEndDate());
   }

   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsInactive() throws Exception {
      iteration.setEndDate(todayMidnight);
      setAutomaticallyExtendIterationEndDate(true);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   private void checkIfDataSamplesHaveNotBeenAlreadyGenerated(Date samplingDate, List estimatedHoursDataSamples,
                                                              List actualHoursDataSamples,
                                                              List remainingHoursDataSample) {
      expect(mockHibernateOperations.findByNamedQuery(eq(DataSamplerImpl.GET_DATASAMPLE_QUERY),
                                               aryEq(new Object[]{samplingDate,
                                                            new Integer(iteration.getId()),
                                                            "estimatedHours"}),
                                                            aryEq(new Type[]{Hibernate.DATE, Hibernate.INTEGER, Hibernate.STRING}))).andReturn(estimatedHoursDataSamples);

      expect(mockHibernateOperations.findByNamedQuery(eq(DataSamplerImpl.GET_DATASAMPLE_QUERY),
                                               aryEq(new Object[]{samplingDate,
                                                            new Integer(iteration.getId()),
                                                            "actualHours"}),
                                               aryEq(new Type[]{Hibernate.DATE, Hibernate.INTEGER, Hibernate.STRING}))).andReturn(actualHoursDataSamples);

      expect(mockHibernateOperations.findByNamedQuery(eq(DataSamplerImpl.GET_DATASAMPLE_QUERY),
                                               aryEq(new Object[]{samplingDate,
                                                            new Integer(iteration.getId()),
                                                            "remainingHours"}),
                                               aryEq(new Type[]{Hibernate.DATE, Hibernate.INTEGER, Hibernate.STRING}))).andReturn(remainingHoursDataSample);
   }

   private void saveDataSamples() {
      expect(mockHibernateOperations.save(anyObject())).andReturn(null).times(3);
   }

   private void updateDataSamples() {
      mockHibernateOperations.update(anyObject());
      mockHibernateOperations.update(anyObject());
      mockHibernateOperations.update(anyObject());
   }

   private void setAutomaticallyExtendIterationEndDate(boolean automaticallyExtend) {
      properties.setProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, Boolean.toString(automaticallyExtend));
   }
}