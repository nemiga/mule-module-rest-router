/**
 * Mule Rest Module
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.rest;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.transport.PropertyScope;
import org.mule.construct.Flow;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.FunctionalTestCase;

import java.util.HashMap;
import java.util.Map;

public class RestRouterModuleUriMatchingTest extends FunctionalTestCase {
    private static final String FLOW_NAME = "uriMatching";

	@Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Test
    public void testGetMenu() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "get");
        properties.put("http.request.path", "/myDomain/menu");
        runFlowWithPayloadAndExpect(FLOW_NAME, "Retrieving Administration menu for domain myDomain", null, properties);
    }

    @Test
    public void testGetLookup() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "get");
        properties.put("http.request.path", "/myDomain/myLookup");
        runFlowWithPayloadAndExpect(FLOW_NAME, "Retrieving myLookup lookup table for domain myDomain", null, properties);
    }

    @Test
    public void testGetEntry() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "get");
        properties.put("http.request.path", "/myDomain/myLookup/myEntry");
        runFlowWithPayloadAndExpect(FLOW_NAME, "Getting entry with ID: myEntry from myLookup lookup table for domain myDomain", null, properties);
    }

     /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     */
    protected <T> void runFlowAndExpect(String flowName, T expect) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = AbstractMuleTestCase.getTestEvent(null);
        MuleEvent responseEvent = flow.process(event);

        assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     * @param payload  The payload of the input event
     */
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload, Map<String, Object> inboundProperties) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = AbstractMuleTestCase.getTestEvent(payload);

        for (String propertyKey : inboundProperties.keySet()) {
            event.getMessage().setProperty(propertyKey, inboundProperties.get(propertyKey), PropertyScope.INBOUND);
        }
        MuleEvent responseEvent = flow.process(event);

        assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Retrieve a flow by name from the registry
     *
     * @param name Name of the flow to retrieve
     */
    protected Flow lookupFlowConstruct(String name) {
        return (Flow) AbstractMuleTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
