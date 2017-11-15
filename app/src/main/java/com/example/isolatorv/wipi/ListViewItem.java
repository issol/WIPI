package com.example.isolatorv.wipi;

import android.graphics.drawable.Drawable;

/**
 * Created by isolatorv on 2017. 11. 2..
 */

public class ListViewItem {

    private String pet_image;
    private String pet_name;
    private String pet_type;
    private String pet_sex;
    private String pet_age;
    private int pet_size;

    public String getPet_image() {
        return pet_image;
    }

    public void setPet_image(String pet_image) {
        this.pet_image = pet_image;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getPet_sex() {
        return pet_sex;
    }

    public void setPet_sex(String pet_sex) {
        this.pet_sex = pet_sex;
    }

    public String getPet_age() {
        return pet_age;
    }

    public void setPet_age(String pet_age) {
        this.pet_age = pet_age;
    }

    public int getPet_size() {
        return pet_size;
    }

    public void setPet_size(int pet_size) {
        this.pet_size = pet_size;
    }
}
