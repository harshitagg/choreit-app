package com.pineapps.choreit.view;

import com.pineapps.choreit.R;

import java.util.HashMap;

public class ChoreIconMap extends HashMap<String, Integer> {
    public ChoreIconMap() {
        this.put("Laundry", R.drawable.clothing);
        this.put("Cleaning", R.drawable.cleaning);
        this.put("Dish washing", R.drawable.dining_out);
        this.put("Dusting", R.drawable.furniture);
    }

    @Override
    public Integer get(Object key) {
        Integer imageResourceId = super.get(key);

        if (imageResourceId == null) {
            return R.drawable.general;
        }

        return imageResourceId;
    }
}
