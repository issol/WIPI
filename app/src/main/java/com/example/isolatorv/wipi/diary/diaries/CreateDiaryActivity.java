package com.example.isolatorv.wipi.diary.diaries;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.isolatorv.wipi.R;

import com.example.isolatorv.wipi.diary.Utils.BitmapUtils;
import com.example.isolatorv.wipi.diary.Utils.CommonUtils;
import com.example.isolatorv.wipi.diary.Utils.DateUtils;
import com.example.isolatorv.wipi.diary.Utils.DialogUtils;
import com.example.isolatorv.wipi.diary.Utils.FontUtils;
import com.example.isolatorv.wipi.diary.Utils.PermissionUtils;
import com.example.isolatorv.wipi.diary.helper.DiaryConstants;


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

public class CreateDiaryActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SPEECH_INPUT = 100;
    private long mCurrentTimeMillis;
    private int mCurrentCursor = 0;
    private RealmList<PhotoUriDto> mPhotoUris;
    private List<Integer> mRemoveIndexes = new ArrayList<>();

    @BindView(R.id.contents)
    EditText mContents;

    @BindView(R.id.title)
    EditText mTitle;

    @BindView(R.id.weatherSpinner)
    Spinner mWeatherSpinner;

    @BindView(R.id.photoContainer)
    ViewGroup mPhotoContainer;

    @BindView(R.id.saveContents)
    ImageView mSaveContents;

    @BindView(R.id.datePicker)
    ImageView mDatePicker;

    @BindView(R.id.timePicker)
    ImageView mTimePicker;

    @BindView(R.id.photoView)
    ImageView mPhotoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diary);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.create_diary_title));
        setDateTime();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FontUtils.setToolbarTypeface(toolbar, Typeface.DEFAULT);

        bindView();
        bindEvent();
        initSpinner();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void initSpinner() {

        String[]  weatherArr = getResources().getStringArray(R.array.weather_item_array);
        ArrayAdapter arrayAdapter = new DiaryWeatherArrayAdapter(CreateDiaryActivity.this, R.layout.spinner_item_diary_weather_array_adapter, Arrays.asList(weatherArr));
        mWeatherSpinner.setAdapter(arrayAdapter);

    }

    private void bindView() {}

    private void bindEvent() {

        mTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCurrentCursor = 0;
                return false;
            }
        });

        mContents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCurrentCursor = 1;
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DialogUtils.showAlertDialog(CreateDiaryActivity.this, getString(R.string.back_pressed_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
    }

    @OnClick({ R.id.saveContents, R.id.photoView, R.id.datePicker, R.id.timePicker})
    public void onClick(View view) {
        float fontSize = mContents.getTextSize();

        switch(view.getId()) {

            case R.id.saveContents:
                if (StringUtils.isEmpty(mContents.getText())) {
                    mContents.requestFocus();
                    DialogUtils.makeSnackBar(findViewById(android.R.id.content), getString(R.string.request_content_message));
                } else {
                    DiaryDto diaryDto = new DiaryDto(
                            -1,
                            mCurrentTimeMillis,
                            String.valueOf(CreateDiaryActivity.this.mTitle.getText()),
                            String.valueOf(CreateDiaryActivity.this.mContents.getText()),
                            mWeatherSpinner.getSelectedItemPosition()
                    );
                    applyRemoveIndex();
                    diaryDto.setPhotoUris(mPhotoUris);
                    DiaryDao.createDiary(diaryDto);
                    CommonUtils.saveIntPreference(CreateDiaryActivity.this, DiaryConstants.PREVIOUS_ACTIVITY, DiaryConstants.PREVIOUS_ACTIVITY_CREATE);
                    finish();
                }
                break;
            case R.id.photoView:
                if (PermissionUtils.checkPermission(this, DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS)) {
                    // API Level 22 이하이거나 API Level 23 이상이면서 권한취득 한경우
                    callImagePicker();
                } else {
                    // API Level 23 이상이면서 권한취득 안한경우
                    PermissionUtils.confirmPermission(this, this, DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS, DiaryConstants.REQUEST_CODE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.datePicker:
                if (mDatePickerDialog == null) {
                    mDatePickerDialog = new DatePickerDialog(this, mStartDateListener, mYear, mMonth - 1, mDayOfMonth);
                }
                mDatePickerDialog.show();
                break;
            case R.id.timePicker:
                if (mTimePickerDialog == null) {
                    mTimePickerDialog = new TimePickerDialog(this, mTimeSetListener, mHourOfDay, mMinute, false);
                }
                mTimePickerDialog.show();
                break;
        }
    }

    private int mYear = Integer.valueOf(DateUtils.getCurrentDateAsString(DateUtils.YEAR_PATTERN));
    private int mMonth = Integer.valueOf(DateUtils.getCurrentDateAsString(DateUtils.MONTH_PATTERN));
    private int mDayOfMonth = Integer.valueOf(DateUtils.getCurrentDateAsString(DateUtils.DAY_PATTERN));
    private int mHourOfDay = Integer.valueOf(DateUtils.getCurrentDateAsString("HH"));
    private int mMinute = Integer.valueOf(DateUtils.getCurrentDateAsString("mm"));

    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    DatePickerDialog.OnDateSetListener mStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month + 1;
            mDayOfMonth = dayOfMonth;
            setDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHourOfDay = hourOfDay;
            mMinute = minute;
            setDateTime();
        }
    };

    private void setDateTime() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
            String dateTimeString = String.format(
                    "%d%s%s%s%s",
                    mYear,
                    StringUtils.leftPad(String.valueOf(mMonth), 2, "0"),
                    StringUtils.leftPad(String.valueOf(mDayOfMonth), 2, "0"),
                    StringUtils.leftPad(String.valueOf(mHourOfDay), 2, "0"),
                    StringUtils.leftPad(String.valueOf(mMinute), 2, "0")
            );
            Date parsedDate = format.parse(dateTimeString);
            mCurrentTimeMillis = parsedDate.getTime();
            getSupportActionBar().setSubtitle(DateUtils.getFullPatternDateWithTime(mCurrentTimeMillis));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void applyRemoveIndex() {
        Collections.sort(mRemoveIndexes, Collections.<Integer>reverseOrder());
        for (int index : mRemoveIndexes) {
            mPhotoUris.remove(index);
        }
        mRemoveIndexes.clear();
    }

    private void callImagePicker() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        try {
            startActivityForResult(pickImageIntent, DiaryConstants.REQUEST_CODE_IMAGE_PICKER);
        } catch (ActivityNotFoundException e) {
            DialogUtils.showAlertDialog(this, getString(R.string.gallery_intent_not_found_message), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CommonUtils.saveLongPreference(CreateDiaryActivity.this, DiaryConstants.PAUSE_MILLIS, System.currentTimeMillis()); // clear screen lock policy
        switch (requestCode) {
            case DiaryConstants.REQUEST_CODE_IMAGE_PICKER:
//                String path = CommonUtils.uriToPath(getContentResolver(), data.getData());
                try {
                    if (resultCode == RESULT_OK && (data != null)) {
                        if (mPhotoUris == null) mPhotoUris = new RealmList<>();
                        mPhotoUris.add(new PhotoUriDto(data.getData().toString()));
                        Bitmap bitmap = BitmapUtils.decodeUri(this, data.getData(), CommonUtils.dpToPixel(this, 70, 1), CommonUtils.dpToPixel(this, 60, 1), CommonUtils.dpToPixel(this, 40, 1));
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(CommonUtils.dpToPixel(this, 70, 1), CommonUtils.dpToPixel(this, 50, 1));
                        layoutParams.setMargins(0, 0, CommonUtils.dpToPixel(this, 3, 1), 0);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setBackgroundResource(R.drawable.bg_card_01);
                        imageView.setImageBitmap(bitmap);
                        imageView.setScaleType(ImageView.ScaleType.CENTER);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        final int currentIndex = mPhotoUris.size() - 1;
                        imageView.setOnClickListener(new PhotoClickListener(currentIndex));
                        mPhotoContainer.addView(imageView, mPhotoContainer.getChildCount() - 1);
                        mPhotoContainer.postDelayed(new Runnable() {
                            public void run() {
                                ((HorizontalScrollView)mPhotoContainer.getParent()).fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                        }, 100L);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case DiaryConstants.REQUEST_CODE_EXTERNAL_STORAGE:
                if (PermissionUtils.checkPermission(this, DiaryConstants.EXTERNAL_STORAGE_PERMISSIONS)) {
                    // 권한이 있는경우
                    callImagePicker();
                } else {
                    // 권한이 없는경우
                    DialogUtils.makeSnackBar(findViewById(android.R.id.content), getString(R.string.guide_message_3));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FontUtils.setTypeface(this, getAssets(), this.mContents);
        FontUtils.setTypeface(this, getAssets(), this.mTitle);
        setDiaryFontSize();
    }

    private void setDiaryFontSize() {
        float fontSize = CommonUtils.loadFloatPreference(this, "font_size", 0);
        if (fontSize > 0) {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            mContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            initSpinner();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                this.overridePendingTransition(R.anim.anim_left_to_center, R.anim.anim_center_to_right);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class PhotoClickListener implements View.OnClickListener {

        int index;
        PhotoClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            final View targetView = v;
            final int targetIndex = index;
            DialogUtils.showAlertDialog(
                    CreateDiaryActivity.this,
                    getString(R.string.delete_photo_confirm_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRemoveIndexes.add(targetIndex);
                            mPhotoContainer.removeView(targetView);
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }
            );
        }
    }
}
