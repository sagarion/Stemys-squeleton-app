/** Description : Services offert pour le binding Sondetemperature
 *  Author : Thibault Daucourt
 *  date : 22.02.2020
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.sondetemperature.service;

import java.util.Date;
import java.util.List;

import io.stemys.binding.sondetemperature.model.Sondetemperature;
import io.stemys.platform.core.thing.query.QueryBuilder;
import io.stemys.platform.core.thing.model.ItemValue;

public interface SondetemperatureService {

    public Sondetemperature findByObjectId(String objectId) ;
    public List<Sondetemperature> findInObjectIds(List<String> objectIds) ;
    public List<Sondetemperature> findAll();
    public Sondetemperature save(Sondetemperature sondetemperature);
    public void delete(Sondetemperature sondetemperature);
    public List<Sondetemperature> executeQuery(QueryBuilder filter);
    public Sondetemperature instanciateSondetemperature(String thingModel);
    public List<ItemValue<?>> getItemValues(String sondetemperatureId, String itemId, Date from, Date to);
}
