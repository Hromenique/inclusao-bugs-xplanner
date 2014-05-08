package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.Integration;

import javax.servlet.http.HttpServletRequest;

public interface IntegrationListener {
    public static final int INTEGRATION_READY_EVENT = 1;

    public void onEvent(int eventType, Integration integration, HttpServletRequest request);
}