/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 12:05:30 PM
 */
package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.charts.DataSampler;

public abstract class AbstractIterationAction extends AbstractAction {
   protected DataSampler dataSampler;
   
   public void setDataSampler(DataSampler dataSampler)
   {
       this.dataSampler = dataSampler;
   }

   public DataSampler getDataSampler()
   {
      return dataSampler;
   }
}