package com.example.isolatorv.wipi.diary.diaries;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String id;
    @Ignore
    private int sessionId;
    private String name;
    private long currentMillis;
    private int age;

    public String getName() { return name; }
    public void   setName(String name) { this.name = name; }
    public int    getAge() { return age; }
    public void   setAge(int age) { this.age = age; }
    public int    getSessionId() { return sessionId; }
    public void   setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public void setCurrentMillis(long currentMillis) {
        this.currentMillis = currentMillis;
    }
}
