package com.example.isolatorv.wipi.diary.setting;


import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DialogUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.PermissionUtils;
import com.example.isolatorv.wipi.diary.diaries.LockDiaryActivity;
import com.example.isolatorv.wipi.diary.googledrive.GoogleDriveDownloader;
import com.example.isolatorv.wipi.diary.googledrive.GoogleDriveUploader;

import org.apache.commons.lang3.StringUtils;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private static int mTaskFlag = 0;

    @Override
    protected void onPause() {
        super.onPause();
        boolean enableLock = CommonUtils.loadBooleanPreference(SettingsActivity.this, "application_lock");
        if (enableLock) {
            long currentMillis = System.currentTimeMillis();
            CommonUtils.saveLongPreference(SettingsActivity.this, DiaryConstants.PAUSE_MILLIS, currentMillis);
        }

        // init current selected font
        FontUtils.setCurrentTypeface(SettingsActivity.this, getAssets());
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean enableLock = CommonUtils.loadBooleanPreference(SettingsActivity.this, "application_lock");
        long pauseMillis = CommonUtils.loadLongPreference(SettingsActivity.this, DiaryConstants.PAUSE_MILLIS, 0);
        if (enableLock && pauseMillis != 0) {
            if (System.currentTimeMillis() - pauseMillis > 1000) {
                // 잠금해제 화면
                Intent lockDiaryIntent = new Intent(SettingsActivity.this, LockDiaryActivity.class);
                startActivity(lockDiaryIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case DiaryConstants.REQUEST_CODE_EXTERNAL_STORAGE:
                if (PermissionUtils.checkPermission(getApplicationContext(), DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS)) {
                    // 권한이 있는경우
                    if (mTaskFlag == DiaryConstants.SETTING_FLAG_EXPORT_GOOGLE_DRIVE) {
//                            FileUtils.copyFile(new File(DiaryDao.getRealmInstance().getPath()), new File(Path.WORKING_DIRECTORY + Path.DIARY_DB_NAME));
                        Intent uploadIntent = new Intent(getApplicationContext(), GoogleDriveUploader.class);
                        startActivity(uploadIntent);
                    } else if (mTaskFlag == DiaryConstants.SETTING_FLAG_IMPORT_GOOGLE_DRIVE) {
                        Intent downloadIntent = new Intent(getApplicationContext(), GoogleDriveDownloader.class);
                        startActivity(downloadIntent);
                    }
                } else {
                    // 권한이 없는경우
                    DialogUtils.makeSnackBar(findViewById(android.R.id.content), getString(R.string.guide_message_3));
                }
                break;
            default:
                break;
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof EditTextPreference) {

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);

                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Window window = getWindow();
                        View v = window.getDecorView();
                        determineView((ViewGroup)v);
                    }
                });

            }
        }).start();

//        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
////        Window window = getWindow();
////        ViewGroup viewGroup = (ViewGroup) window.getDecorView();
//        determineView(viewGroup);

    }

    public void determineView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            Log.i("info index ", String.format("%d", i));
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                determineView((ViewGroup)viewGroup.getChildAt(i));
            } else {
                if (viewGroup.getChildAt(i) instanceof TextView) {
                    TextView tv = (TextView) viewGroup.getChildAt(i);
                    if (StringUtils.equals(getString(R.string.setting_title), tv.getText())) {
                        tv.setTypeface(Typeface.DEFAULT);
                    }
//                    Log.i("view info", String.format("%s", tv.getText()));
                }
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(getString(R.string.setting_title));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public void onBuildHeaders(List<Header> target) {
//        loadHeadersFromResource(R.xml.pref_headers, target);
//    }


    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
//            bindPreferenceSummaryToValue(findPreference("example_list"));

            bindPreferenceSummaryToValue(findPreference("font_setting"));
            initPreference();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().finish();
//                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        Preference mAppVersionPreference;
        Preference mOpenSourceLicensesInfo;
        Preference mExportGoogleDrive;
        Preference mImportGoogleDrive;
        Preference mApplicationLockPassword;
        private void initPreference() {
            mAppVersionPreference = findPreference("aaf_app_version");
            mOpenSourceLicensesInfo = findPreference("open_source_licenses");
            mExportGoogleDrive= findPreference("export_google_drive");
            mImportGoogleDrive= findPreference("import_google_drive");
            mApplicationLockPassword = findPreference("application_lock_password");

            PackageInfo pInfo = null;
            try {
                pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = pInfo.versionName;
            mAppVersionPreference.setTitle(getString(R.string.rate_app));
            mAppVersionPreference.setSummary("Easy Diary v " + version);
            mAppVersionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Uri uri = Uri.parse("market://details?id=me.blog.korn123.easydiary");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=me.blog.korn123.easydiary")));
                    }
                    return false;
                }
            });

            mOpenSourceLicensesInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), LicensesActivity.class);
                    startActivity(intent);
                    return false;
                }
            });

            mExportGoogleDrive.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mTaskFlag = DiaryConstants.SETTING_FLAG_EXPORT_GOOGLE_DRIVE;
                    if (PermissionUtils.checkPermission(getActivity().getApplicationContext() , DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS)) {
                        // API Level 22 이하이거나 API Level 23 이상이면서 권한취득 한경우
                        Intent uploadIntent = new Intent(getActivity(), GoogleDriveUploader.class);
                        startActivity(uploadIntent);
                    } else {
                        // API Level 23 이상이면서 권한취득 안한경우
                        PermissionUtils.confirmPermission(getActivity(), getActivity(), DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS, DiaryConstants.REQUEST_CODE_EXTERNAL_STORAGE);
                    }
                    return false;
                }
            });

            mApplicationLockPassword.setSummary(getString(R.string.lock_number) + " " +CommonUtils.loadStringPreference(getActivity().getApplicationContext(), "application_lock_password", "0000"));
            mImportGoogleDrive.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mTaskFlag = DiaryConstants.SETTING_FLAG_IMPORT_GOOGLE_DRIVE;
                    if (PermissionUtils.checkPermission(getActivity(), DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS)) {
                        // API Level 22 이하이거나 API Level 23 이상이면서 권한취득 한경우
                        Intent downloadIntent = new Intent(getActivity(), GoogleDriveDownloader.class);
                        startActivity(downloadIntent);
                    } else {
                        // API Level 23 이상이면서 권한취득 안한경우
                        PermissionUtils.confirmPermission(getActivity(), getActivity(), DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS, DiaryConstants.REQUEST_CODE_EXTERNAL_STORAGE);
                    }
                    return false;
                }
            });

            mApplicationLockPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent lockSettingIntent = new Intent(getActivity(), LockSettingActivity.class);
                    startActivityForResult(lockSettingIntent, DiaryConstants.REQUEST_CODE_LOCK_SETTING);
                    return false;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                String password = data.getStringExtra("password");
                CommonUtils.saveStringPreference(getActivity().getApplicationContext(), "application_lock_password", password);
                mApplicationLockPassword.setSummary(getString(R.string.lock_number) + password);
            }
            CommonUtils.saveLongPreference(getActivity().getApplicationContext(), DiaryConstants.PAUSE_MILLIS, System.currentTimeMillis());
        }
    }

}
