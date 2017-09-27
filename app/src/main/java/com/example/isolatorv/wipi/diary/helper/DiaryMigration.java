package com.example.isolatorv.wipi.diary.helper;


import com.example.isolatorv.wipi.diary.Utils.DateUtils;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;


public class DiaryMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            RealmObjectSchema diarySchema = schema.get("DiaryDto");

            diarySchema
                    .addField("dateString", String.class)
                    .transform(new RealmObjectSchema.Function() {


                        @Override
                        public void apply(DynamicRealmObject obj) {
                            String dateString = DateUtils.timeMillisToDateTime(obj.getLong("currentTimeMillis"), DateUtils.DATE_PATTERN_DASH);
                            obj.set("dateString", dateString);
                        }
                    });

            oldVersion++;
        }

        if (oldVersion == 2) {
            RealmObjectSchema diarySchema = schema.get("DiaryDto");
            diarySchema
                    .addField("weather", int.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("weather", 0);
                        }
                    });
            oldVersion++;
        }

        if (oldVersion == 3) {
            RealmObjectSchema diarySchema = schema.get("DiaryDto");

            RealmObjectSchema photoUriSchema = schema.create("PhotoUriDto")
                    .addField("photoUri", String.class);

            diarySchema
                    .addRealmListField("photoUris", photoUriSchema)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
//                            obj.set("photoUris", null);
                        }
                    });
            oldVersion++;
        }

    }
}