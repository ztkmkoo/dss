package io.github.ztkmkoo.dss.example.http.tutorial;

import java.io.Serializable;

public class MyServiceRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}