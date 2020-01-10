/** Description : Services offert pour le binding Smartfactory
 *  Author : Thibault Daucourt
 *  date : 18.12.2019
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.smartfactory.service;

import java.util.Date;
import java.util.List;

import io.stemys.binding.smartfactory.model.Smartfactory;
import io.stemys.platform.core.thing.query.QueryBuilder;
import io.stemys.platform.core.thing.model.ItemValue;

public interface SmartfactoryService {

    public Smartfactory findByObjectId(String objectId) ;
    public List<Smartfactory> findInObjectIds(List<String> objectIds) ;
    public List<Smartfactory> findAll();
    public Smartfactory save(Smartfactory smartfactory);
    public void delete(Smartfactory smartfactory);
    public List<Smartfactory> executeQuery(QueryBuilder filter);
    public Smartfactory instanciateSmartfactory(String thingModel);
    public List<ItemValue<?>> getItemValues(String smartfactoryId, String itemId, Date from, Date to);
}
