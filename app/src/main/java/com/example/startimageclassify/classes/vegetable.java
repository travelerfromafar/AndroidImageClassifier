package com.example.startimageclassify.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class vegetable {
    String name, description, image;

    public vegetable() {
    }

    public vegetable(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public void setDescription(String description) {
        this.description = description;
    }
}
