/** Description : fichier java lié au binding Sondetemperature
 *  Author : Thibault Daucourt
 *  date : 22.01.2020
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.sondetemperature;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.stemys.binding.sondetemperature.model.Sondetemperature;
import io.stemys.binding.sondetemperature.service.SondetemperatureService;
import io.stemys.platform.core.thing.model.ItemValue;
import io.stemys.platform.core.thing.model.binding.BindingModel;
import io.stemys.platform.core.thing.query.QueryBuilder;
import io.stemys.platform.core.thing.service.ItemValueService;
import io.stemys.platform.core.thing.service.ThingDBService;



public class SondetemperatureBinding implements BindingModel, SondetemperatureService  {

    private static final Logger s_logger = LoggerFactory.getLogger(SondetemperatureBinding.class);

    // Cloud Application identifier
    private static final String APP_ID = "io.stemys.binding.sondetemperature";

    private ThingDBService thingDBService;

    private ItemValueService itemValueService;

    // ----------------------------------------------------------------
    //
    // Dependencies
    //
    // ----------------------------------------------------------------

    public SondetemperatureBinding() {
        super();

    }

    public void setThingDBService(ThingDBService thingDBService) {
        this.thingDBService = thingDBService;
    }

    public void unsetThingDBService(ThingDBService thingDBService) {
        this.thingDBService = null;
    }

    public void setItemValueService(ItemValueService itemValueService) {
        this.itemValueService = itemValueService;
    }

    public void unsetItemValueService(ItemValueService itemValueService) {
        this.itemValueService = null;
    }

    // ----------------------------------------------------------------
    //
    // Activation APIs
    //
    // ----------------------------------------------------------------

    protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
        s_logger.info("Activating Binding Sondetemperature...");

        s_logger.info("Activating Binding Sondetemperature... Done.");
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.debug("Deactivating Binding Sondetemperature...");

        s_logger.debug("Deactivating Binding Sondetemperature... Done.");
    }

    @Override
    public List<Sondetemperature> findAll() {

        return thingDBService.findAll(Sondetemperature.class);
    }

    @Override
    public Sondetemperature save(Sondetemperature sondetemperature) {
        return thingDBService.save(sondetemperature);
    }

    @Override
    public Sondetemperature findByObjectId(String objectId) {
        return thingDBService.findByObjectId(objectId, Sondetemperature.class);
    }

    @Override
    public void delete(Sondetemperature sondetemperature) {
        thingDBService.delete(sondetemperature);

    }

    @Override
    public List<Sondetemperature> executeQuery(QueryBuilder filter) {

        if (filter == null)
            return null;
        return thingDBService.executeQuery(filter, Sondetemperature.class);


    }

    public Sondetemperature instanciateSondetemperature(String thingModel) {
        //FIXME we need to create another instanciate method on thingdbservice
        return thingDBService.instanciate(new Sondetemperature().getBindingName(), thingModel);
    }

    @Override
    public List<ItemValue<?>> getItemValues(String sondetemperatureId, String itemId, Date from, Date to) {
        return itemValueService.findByThingIdAndItemId(sondetemperatureId, itemId, from, to);
    }

    @Override
    public List<Sondetemperature> findInObjectIds(List<String> objectIds) {
        return this.thingDBService.findInObjectIds(objectIds, Sondetemperature.class);
    }
}
