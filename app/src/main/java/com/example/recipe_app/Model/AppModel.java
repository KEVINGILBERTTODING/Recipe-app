package com.example.recipe_app.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppModel implements Serializable {
    @SerializedName("id")
    String id;
    @SerializedName("about_us")
    String abut_us;
    @SerializedName("app_version")
    String app_version;

    public AppModel (String id, String about_us, String app_version) {
        this.id = id;
        this.abut_us = about_us;
        this.app_version = app_version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbut_us() {
        return abut_us;
    }

    public void setAbut_us(String abut_us) {
        this.abut_us = abut_us;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }
}
