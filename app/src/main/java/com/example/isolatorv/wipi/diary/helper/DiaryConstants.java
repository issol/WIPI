package com.example.isolatorv.wipi.diary.helper;

import android.Manifest;

/**
 * Created by isolatorv on 2017. 9. 19..
 */

public class DiaryConstants {
    // startActivityForResult Request Code: Permission
    final static public int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    final static public int REQUEST_CODE_EXTERNAL_STORAGE_WITH_SHARE_DIARY_CARD = 2;

    // startActivityForResult Request Code: Google Drive
    final static public int REQUEST_CODE_GOOGLE_DRIVE_UPLOAD = 11;
    final static public int REQUEST_CODE_GOOGLE_DRIVE_DOWNLOAD = 12;

    // startActivityForResult Request Code: Etc
    final static public int REQUEST_CODE_LOCK_SETTING = 21;
    final static public int REQUEST_CODE_IMAGE_PICKER = 22;

    // startActivityForResult Request Code: ColorPicker
    final static public int REQUEST_CODE_BACKGROUND_COLOR_PICKER = 31;
    final static public int REQUEST_CODE_TEXT_COLOR_PICKER = 32;

    final static public String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS};
    final static public String[] EXTERNAL_STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    final static public int SETTING_FLAG_EXPORT_GOOGLE_DRIVE = 1;
    final static public int SETTING_FLAG_IMPORT_GOOGLE_DRIVE = 2;
    final static public String PAUSE_MILLIS = "pause_millis";

    // 이전화면
    final static public String PREVIOUS_ACTIVITY = "previous_activity";

    // 일기작성화면
    final static public int PREVIOUS_ACTIVITY_CREATE = 1;

    // 맑음, 화창함
    final static public int WEATHER_FLAG_SUN = 1;

    // 흐림
    final static public int WEATHER_FLAG_SUN_AND_CLOUD = 2;

    // 비
    final static public int WEATHER_FLAG_RAIN = 3;

    // 번개
    final static public int WEATHER_FLAG_THUNDER_BOLT = 4;

    // 눈
    final static public int WEATHER_FLAG_SNOW = 5;

    final static public int SHOWCASE_SINGLE_SHOT_READ_DIARY_NUMBER = 0;
    final static public int SHOWCASE_SINGLE_SHOT_CREATE_DIARY_NUMBER = 1;
    final static public int SHOWCASE_SINGLE_SHOT_READ_DIARY_DETAIL_NUMBER = 2;
    final static public int SHOWCASE_SINGLE_SHOT_POST_CARD_NUMBER = 3;

    // Easy Diary custom fonts supported language
    final static public String CUSTOM_FONTS_SUPPORT_LANGUAGE = "en|ko";

}
