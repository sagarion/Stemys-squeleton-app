/*******************************************************************************
 * Copyright (c) 2014, 2019 stemys SA
 *******************************************************************************/
package io.stemys.thingtype;

import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class sondetemperatureThingType {
    private static final Logger s_logger = LoggerFactory.getLogger(sondetemperatureThingType.class);

    // ----------------------------------------------------------------
    //
    // Dependencies
    //
    // ----------------------------------------------------------------

    public sondetemperatureThingType() {
        super();

    }

    // ----------------------------------------------------------------
    //
    // Activation APIs
    //
    // ----------------------------------------------------------------

    protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
        s_logger.info("Activating ThingType Sondetemperature ...");

        s_logger.info("Activating ThingType Sondetemperature ... Done.");
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.debug("Deactivating ThingType Sondetemperature ....");

        s_logger.debug("Deactivating ThingType Sondetemperature ... Done.");
    }
}
