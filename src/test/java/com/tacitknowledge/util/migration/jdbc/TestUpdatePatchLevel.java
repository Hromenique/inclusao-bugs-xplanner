/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.tacitknowledge.util.migration.jdbc;

import org.easymock.classextension.MockClassControl;
import org.easymock.MockControl;
import junit.framework.*;
import com.tacitknowledge.util.migration.jdbc.AutopatchSupport;
import com.tacitknowledge.util.migration.jdbc.UpdatePatchLevel;

public class TestUpdatePatchLevel extends TestCase {
   private UpdatePatchLevel updateOnlyPatchLevel;
   private MockControl mockAutopatchSupportControl;
   private AutopatchSupport mockAutopatchSupport;

   protected void setUp() throws Exception {
      super.setUp();
      mockAutopatchSupportControl = MockClassControl.createControl(AutopatchSupport.class);
      mockAutopatchSupport = (AutopatchSupport) mockAutopatchSupportControl.getMock();
      updateOnlyPatchLevel = new UpdatePatchLevel(mockAutopatchSupport);
   }

   public void testSetPathLevel() throws Exception {
      mockAutopatchSupportControl.expectAndReturn(mockAutopatchSupport.getHighestPatchLevel(), 6);
      mockAutopatchSupport.setPatchLevel(6);
      mockAutopatchSupportControl.replay();
      updateOnlyPatchLevel.updatePatchLevel("testSystem", null);
      mockAutopatchSupportControl.verify();
      mockAutopatchSupportControl.reset();
      mockAutopatchSupport.setPatchLevel(3);
      mockAutopatchSupportControl.replay();
      updateOnlyPatchLevel.updatePatchLevel("testSystem", "3");
      mockAutopatchSupportControl.verify();


   }
}