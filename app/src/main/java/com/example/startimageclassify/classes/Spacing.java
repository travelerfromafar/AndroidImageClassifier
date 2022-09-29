package com.example.startimageclassify.classes;

import androidx.recyclerview.widget.RecyclerView;

public class Spacing {
    String VegeName, VegeSpacing;

    public Spacing() {

    }

    public Spacing(String vegeName
            , String vegeSpacing
            ) {
        this.VegeName = vegeName;
        this.VegeSpacing = vegeSpacing;
    }

    public String getVegeName() {
        return VegeName;
    }

    public void setVegeName(String vegeName) {
        VegeName = vegeName;
    }

    public String getVegeSpacing() {
        return VegeSpacing;
    }

//    public void setVegeSpacing(String vegeSpacing) {
//        VegeSpacing = vegeSpacing;
//    }
}
