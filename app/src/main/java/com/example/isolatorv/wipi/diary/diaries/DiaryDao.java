package com.example.isolatorv.wipi.diary.diaries;

import com.example.isolatorv.wipi.diary.helper.DiaryMigration;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

public class DiaryDao {
    private static RealmConfiguration diaryConfig;

    private DiaryDao() {}

    public static Realm getRealmInstance() {
        if (diaryConfig == null) {
            diaryConfig = new RealmConfiguration.Builder()
                    .name("diary.realm")
                    .schemaVersion(4)
                    .migration(new DiaryMigration())
                    /*.deleteRealmIfMigrationNeeded()*/
                    .modules(Realm.getDefaultModule())
                    .build();

        }
        final Realm diaryRealm = Realm.getInstance(diaryConfig);
        return diaryRealm;
    }


    public static void createDiary(final long currentTimeMillis, final String title, final String contents) {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            public void execute(Realm realm) {
                int sequence = 1;
                if (realm.where(DiaryDto.class).count() > 0L) {
                    Number number = realm.where(DiaryDto.class).max("sequence");
                    sequence = number.intValue() + 1;
                }
                DiaryDto diaryDto = new DiaryDto(sequence, currentTimeMillis, title, contents);
                realm.insert(diaryDto);
            }
        });
    }

    public static void createDiary(final DiaryDto diaryDto) {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            public void execute(Realm realm) {
                int sequence = 1;
                if (realm.where(DiaryDto.class).count() > 0L) {
                    Number number = realm.where(DiaryDto.class).max("sequence");
                    sequence = number.intValue() + 1;
                }
                diaryDto.setSequence(sequence);
                realm.insert(diaryDto);
            }
        });
    }

    public static List<DiaryDto> readDiary(String query) {
        RealmResults<DiaryDto> results = null;
        if (StringUtils.isEmpty(query)) {
            results = getRealmInstance().where(DiaryDto.class).findAllSorted("currentTimeMillis", Sort.DESCENDING);
        } else {
            results = getRealmInstance().where(DiaryDto.class).beginGroup().contains("contents", query).or().contains("title", query).endGroup().findAllSorted("currentTimeMillis", Sort.DESCENDING);
        }
        List<DiaryDto> list = new ArrayList<>();
        list.addAll(results.subList(0, results.size()));
        return list;
    }

    public static DiaryDto readDiaryBy(int sequence) {
        DiaryDto diaryDto = getRealmInstance().where(DiaryDto.class).equalTo("sequence", sequence).findFirst();
        return diaryDto;
    }

    public static List<DiaryDto> readDiaryByDateString(String dateString) {
        RealmResults<DiaryDto> results = null;
        results = getRealmInstance().where(DiaryDto.class).equalTo("dateString", dateString).findAllSorted("sequence", Sort.DESCENDING);
        List<DiaryDto> list = new ArrayList<>();
        list.addAll(results.subList(0, results.size()));
        return list;
    }

    public static int countDiaryBy(String dateString) {
        int total = 0;
        total = (int)getRealmInstance().where(DiaryDto.class).equalTo("dateString", dateString).count();
        return total;
    }

    public static void updateDiary(final DiaryDto diaryDto) {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            public void execute(Realm realm) {
                realm.insertOrUpdate(diaryDto);
            }
        });
    }

    public static void deleteDiary(int sequence) {
        Realm realm = getRealmInstance();
        DiaryDto diaryDto = realm.where(DiaryDto.class).equalTo("sequence", sequence).findFirst();
        if (diaryDto != null) {
            realm.beginTransaction();
            diaryDto.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}
