package com.example.startimageclassify.classes;

import com.example.startimageclassify.R;

public class ReturnVal {

    String nameval;
    FavRecycler.MyViewHolder holder;

    ReturnVal(String nameval, FavRecycler.MyViewHolder holder){
        this.nameval = nameval;
        this.holder = holder;
        ImageSetUp(ReturnVal.this);
    }

    public void ImageSetUp (ReturnVal returnVal){
        if ("Bitter".equals(returnVal.nameval)) {
            holder.name.setText("Better Gourd");
            holder.image.setImageResource(R.drawable.veg_img_bitter);
        } else if ("Cabbage".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_cab);
        } else if ("Chili".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_chili);
        } else if ("Eggplant".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_eggp);
        } else if ("Ginger".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_gin);
        } else if ("Lettuce".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_lett);
        } else if ("Onion".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_oni);
        } else if ("Pechay".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_bokchoy);
        } else if ("Sweet Potato".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_swepo);
        } else if ("Tomato".equals(returnVal.nameval)) {
            holder.image.setImageResource(R.drawable.veg_img_toma);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
