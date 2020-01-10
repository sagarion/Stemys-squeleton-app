/** Description : fichier java lié au binding
 *  Author : Thibault Daucourt
 *  date : 18.12.2019
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.smartfactory;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.stemys.binding.smartfactory.model.Smartfactory;
import io.stemys.binding.smartfactory.service.SmartfactoryService;
import io.stemys.platform.core.thing.model.ItemValue;
import io.stemys.platform.core.thing.model.binding.BindingModel;
import io.stemys.platform.core.thing.query.QueryBuilder;
import io.stemys.platform.core.thing.service.ItemValueService;
import io.stemys.platform.core.thing.service.ThingDBService;



public class SmartfactoryBinding implements BindingModel, SmartfactoryService  {

    private static final Logger s_logger = LoggerFactory.getLogger(SmartfactoryBinding.class);

    // Cloud Application identifier
    private static final String APP_ID = "io.stemys.binding.smartfactory";

    private ThingDBService thingDBService;

    private ItemValueService itemValueService;

    // ----------------------------------------------------------------
    //
    // Dependencies
    //
    // ----------------------------------------------------------------

    public SmartfactoryBinding() {
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
        s_logger.info("Activating Binding Smartfactory...");

        s_logger.info("Activating Binding Smartfactory... Done.");
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.debug("Deactivating Binding Smartfactory...");

        s_logger.debug("Deactivating Binding Smartfactory... Done.");
    }

    @Override
    public List<Smartfactory> findAll() {

        return thingDBService.findAll(Smartfactory.class);
    }

    @Override
    public Smartfactory save(Smartfactory smartfactory) {
        return thingDBService.save(smartfactory);
    }

    @Override
    public Smartfactory findByObjectId(String objectId) {
        return thingDBService.findByObjectId(objectId, Smartfactory.class);
    }

    @Override
    public void delete(Smartfactory smartfactory) {
        thingDBService.delete(smartfactory);

    }

    @Override
    public List<Smartfactory> executeQuery(QueryBuilder filter) {

        if (filter == null)
            return null;
        return thingDBService.executeQuery(filter, Smartfactory.class);


    }

    public Smartfactory instanciateSmartfactory(String thingModel) {
        //FIXME we need to create another instanciate method on thingdbservice
        return thingDBService.instanciate(new Smartfactory().getBindingName(), thingModel);
    }

    @Override
    public List<ItemValue<?>> getItemValues(String smartfactoryId, String itemId, Date from, Date to) {
        return itemValueService.findByThingIdAndItemId(smartfactoryId, itemId, from, to);
    }

    @Override
    public List<Smartfactory> findInObjectIds(List<String> objectIds) {
        return this.thingDBService.findInObjectIds(objectIds, Smartfactory.class);
    }
}
