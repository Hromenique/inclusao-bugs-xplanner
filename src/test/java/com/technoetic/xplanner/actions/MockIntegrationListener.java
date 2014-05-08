package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.Integration;

import javax.servlet.http.HttpServletRequest;

public class MockIntegrationListener implements IntegrationListener {
    public boolean onEventCalled;
    public int onEventEventType;
    public Integration onEventIntegration;
    public HttpServletRequest onEventRequest;

    public void onEvent(int eventType, Integration integration, HttpServletRequest request) {
        onEventCalled = true;
        onEventEventType = eventType;
        onEventIntegration = integration;
        onEventRequest = request;
    }
}
