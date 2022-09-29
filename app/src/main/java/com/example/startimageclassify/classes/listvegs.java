package com.example.startimageclassify.classes;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class listvegs {

    public String name, filclass;
    public int image;

    public listvegs(){

    }

    public listvegs(String name, String filclass, int image) {
        this.name = name;
        this.image = image;
        this.filclass = filclass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
    public String getFilclass() {
        return filclass;
    }

    public void setFilclass(String filclass) {
        this.filclass = filclass;
    }
}
