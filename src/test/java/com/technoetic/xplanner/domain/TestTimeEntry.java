package com.technoetic.xplanner.domain;

import junit.framework.TestCase;

import java.util.Date;

public class TestTimeEntry extends TestCase {
    private TimeEntry timeEntry;

    public TestTimeEntry(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        timeEntry = new TimeEntry();
    }

    public void testEditedTimeEntry() {
        timeEntry.setStartTime(new Date(0));
        timeEntry.setEndTime(new Date(3600000));
        timeEntry.setDuration(0.5);

        double duration = timeEntry.getDuration();

        assertEquals("wrong duration", 1, duration, 0);
    }
}
