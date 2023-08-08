package com.fazdevguy.fancynotes.misc;

import com.fazdevguy.fancynotes.entity.CustomTextFields;
import org.springframework.data.geo.CustomMetric;

import java.util.ArrayList;
import java.util.List;

public class CustomFieldFormData {

    List<CustomTextFields> customTextFieldsList;


    public CustomFieldFormData(){}

    public CustomFieldFormData(List<CustomTextFields> customTextFieldsList) {
        this.customTextFieldsList = customTextFieldsList;
    }

    public List<CustomTextFields> getCustomTextFieldsList() {
        return customTextFieldsList;
    }

    public void setCustomTextFieldsList(List<CustomTextFields> customTextFieldsList) {
        this.customTextFieldsList = customTextFieldsList;
    }


    // utility
    public void addCustomFieldToList(CustomTextFields ctf){
        if(customTextFieldsList == null){
            customTextFieldsList = new ArrayList<>();
        }

        customTextFieldsList.add(ctf);

    }

    public void removeCustomFieldFromList(CustomTextFields ctf){
        if (customTextFieldsList == null) return;

        customTextFieldsList.remove(ctf);
    }


}
