/*******************************************************************************
 * Copyright (c) 2014, 2019 stemys SA
 *******************************************************************************/
package io.stemys.thingtype;

import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.stemys.platform.core.thing.model.thingtype.*;

public class SmartfactoryThingType implements ThingTypeModel{
    private static final Logger s_logger = LoggerFactory.getLogger(SmartfactoryThingType.class);

    // ----------------------------------------------------------------
    //
    // Dependencies
    //
    // ----------------------------------------------------------------

    public SmartfactoryThingType() {
        super();

    }

    // ----------------------------------------------------------------
    //
    // Activation APIs
    //
    // ----------------------------------------------------------------

    protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
        s_logger.info("Activating ThingType Smartfactory ...");

        s_logger.info("Activating ThingType Smartfactory ... Done.");
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.debug("Deactivating ThingType Smartfactory ....");

        s_logger.debug("Deactivating ThingType Smartfactory ... Done.");
    }
}
