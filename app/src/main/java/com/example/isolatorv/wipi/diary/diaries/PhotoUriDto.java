package com.example.isolatorv.wipi.diary.diaries;

import io.realm.RealmObject;

public class PhotoUriDto extends RealmObject {

    private String photoUri;

    public PhotoUriDto() {}

    public PhotoUriDto(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
