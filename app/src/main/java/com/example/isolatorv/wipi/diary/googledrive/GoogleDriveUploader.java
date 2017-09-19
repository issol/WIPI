package com.example.isolatorv.wipi.diary.googledrive;

import android.content.IntentSender;
import android.os.Bundle;

import com.example.isolatorv.wipi.diary.DateUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.EasyDiaryUtils;
import com.example.isolatorv.wipi.diary.Path;
import com.example.isolatorv.wipi.diary.diaries.DiaryDao;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.MetadataChangeSet;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by CHO HANJOONG on 2016-09-26.
 */
public class GoogleDriveUploader extends GoogleDriveUtils {

    @Override
    public void onConnected(Bundle connectionHint) {
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    final DriveContents driveContents = result.getDriveContents();
                    OutputStream outputStream = driveContents.getOutputStream();
                    File backupFile = new File(DiaryDao.getRealmInstance().getPath());
                    try {
                        FileUtils.copyFile(backupFile, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                            .setTitle(Path.DIARY_DB_NAME + "_" + DateUtils.getCurrentDateTime("yyyyMMdd_HHmmss"))
                            .setMimeType(EasyDiaryUtils.getEasyDiaryMimeType()).build();
                    IntentSender intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.getDriveContents())
                            .build(getGoogleApiClient());
                    try {
                        startIntentSenderForResult(
                                intentSender, DiaryConstants.REQUEST_CODE_GOOGLE_DRIVE_UPLOAD, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            };

}
