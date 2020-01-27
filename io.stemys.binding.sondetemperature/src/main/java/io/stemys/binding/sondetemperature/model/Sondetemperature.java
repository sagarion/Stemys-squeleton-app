/** Description : DataModel for a thing of type smartfactory
 *  Author : Thibault Daucourt
 *  date : 18.12.2019
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.sondetemperature.model;

import org.bson.Document;

import io.stemys.platform.core.thing.model.Thing;
import io.stemys.platform.core.thing.model.binding.Binding;
import io.stemys.platform.core.thing.model.thingtype.ThingType;


public class Sondetemperature extends Thing {
    /**
     *
     */
    private static final long serialVersionUID = 8441456370409080448L;

    public static final String ITEM_TEMPERATURE = "temperature";


    public Sondetemperature() {
        super();
    }


    public Sondetemperature(Document doc) {
        super(doc);
    }

    public Sondetemperature(Binding binding, ThingType thingType) {
        super(binding, thingType);
    }

    public void setTemperature(boolean temperature){
        setItemValueById(ITEM_TEMPERATURE, String.valueOf(temperature));
    }

    public Integer getTemperature(){
        Object value = getItemValueById(ITEM_TEMPERATURE);
        return this.getValue(value);
    }

    public String getBindingName(){
        return "io.stemys.binding.sondetemperature";
    }

    private Integer getValue(Object value) {
        if (value != null) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
            if (value instanceof Integer) {
                return (Integer) value;
            }
        }
        return 0;
    }


}
