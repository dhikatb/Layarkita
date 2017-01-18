package com.example.test.layarkita;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dhika TB on 14/06/2016.
 */
public class Product implements Serializable {

    @SerializedName("pid")
    public String pid;

    @SerializedName("name")
    public String name;

    @SerializedName("deskripsi")
    public String deskripsi;

    @SerializedName("youtube_url")
    public String youtube_url;

    @SerializedName("youtube_code")
    public String youtube_code;


}
