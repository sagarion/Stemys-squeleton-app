/** Description : DataModel for a thing of type smartfactory
 *  Author : Thibault Daucourt
 *  date : 18.12.2019
 *  version : 1.0.0
 *  status : waiting for test
 */

package io.stemys.binding.smartfactory.model;

import org.bson.Document;

import io.stemys.platform.core.thing.model.Thing;
import io.stemys.platform.core.thing.model.binding.Binding;
import io.stemys.platform.core.thing.model.thingtype.ThingType;


public class Smartfactory extends Thing {
    /**
     *
     */
    private static final long serialVersionUID = 8441456370409080448L;

    public static final String ITEM_EMERGENCY_STATE = "emergencyState";
    public static final String ITEM_RUNTIME = "runtime";


    public Smartfactory() {
        super();
    }


    public Smartfactory(Document doc) {
        super(doc);
    }

    public Smartfactory(Binding binding, ThingType thingType) {
        super(binding, thingType);
    }

    public void setEmergencyState(boolean state){
        setItemValueById(ITEM_EMERGENCY_STATE, String.valueOf(state));
    }

    public Boolean getEmergencyState(){
        return (Boolean)getItemValueById(ITEM_EMERGENCY_STATE);
    }

    public void setRuntime(Integer runtime){
        setItemValueById(ITEM_RUNTIME, runtime);
    }

    public Integer getRuntime(){
        Object value = getItemValueById(ITEM_RUNTIME);
        return this.getValue(value);
    }

    public String getBindingName(){
        return "io.stemys.binding.smartfactory";
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
