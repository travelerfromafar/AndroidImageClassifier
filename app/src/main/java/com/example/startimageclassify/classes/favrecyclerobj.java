package com.example.startimageclassify.classes;

public class favrecyclerobj {
    public String name, filclass;

    favrecyclerobj(){

    }
    public favrecyclerobj(String name, String filclass) {
        this.name = name;
        this.filclass = filclass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilclass() {
        return filclass;
    }

    public void setFilclass(String filclass) {
        this.filclass = filclass;
    }
}
