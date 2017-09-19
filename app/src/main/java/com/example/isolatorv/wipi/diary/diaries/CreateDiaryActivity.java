package com.example.isolatorv.wipi.diary.diaries;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.BitmapUtils;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DateUtils;
import com.example.isolatorv.wipi.diary.DialogUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.PermissionUtils;
import com.example.isolatorv.wipi.diary.helper.EasyDiaryActivity;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;


/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

public class CreateDiaryActivity extends EasyDiaryActivity {

    private final int REQUEST_CODE_SPEECH_INPUT = 100;
    private Intent mRecognizerIntent;
    private long mCurrentTimeMillis;
    private int mCurrentCursor = 0;
    private RealmList<PhotoUriDto> mPhotoUris;
    private List<Integer> mRemoveIndexes = new ArrayList<>();

    @BindView(R.id.contents)
    EditText mContents;

    @BindView(R.id.title)
    EditText mTitle;

    @BindView(R.id.toggleSwitch)
    Switch mToggleSwitch;

    @BindView(R.id.toggleMicOn)
    ImageView mToggleMicOn;

    @BindView(R.id.toggleMicOff)
    ImageView mToggleMicOff;

    @BindView(R.id.weatherSpinner)
    Spinner mWeatherSpinner;

    @BindView(R.id.photoContainer)
    ViewGroup mPhotoContainer;

    @BindView(R.id.speechButton)
    FloatingActionButton mSpeechButton;

    @BindView(R.id.zoomIn)
    ImageView mZoomIn;

    @BindView(R.id.zoomOut)
    ImageView mZoomOut;

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

        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        FontUtils.setToolbarTypeface(toolbar, Typeface.DEFAULT);

        bindView();
        bindEvent();
        initSpinner();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initShowcase();
    }

    private int showcaseIndex = 2;
    ShowcaseView mShowcaseView;

    private void initShowcase() {
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();

        final RelativeLayout.LayoutParams centerParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        centerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        centerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centerParams.setMargins(0, 0, 0, margin);

        View.OnClickListener showcaseViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (showcaseIndex) {
                    case 2:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mTitle), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_2));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_2));
                        break;
                    case 3:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mContents), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_3));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_3));
                        break;
                    case 4:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mPhotoView), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_4));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_4));
                        break;
                    case 5:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mZoomIn), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_5));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_5));
                        break;
                    case 6:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mZoomOut), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_6));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_6));
                        break;
                    case 7:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mDatePicker), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_7));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_7));
                        break;
                    case 8:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mTimePicker), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_8));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_8));
                        break;
                    case 9:
                        mShowcaseView.setButtonPosition(centerParams);
                        mShowcaseView.setShowcase(new ViewTarget(mSaveContents), true);
                        mShowcaseView.setContentTitle(getString(R.string.create_diary_showcase_title_9));
                        mShowcaseView.setContentText(getString(R.string.create_diary_showcase_message_9));
                        mShowcaseView.setButtonText(getString(R.string.create_diary_showcase_button_2));
                        break;
                    case 10:
                        mShowcaseView.hide();
                        break;
                }
                showcaseIndex++;
            }
        };

        mShowcaseView = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(new ViewTarget(mWeatherSpinner))
                .setContentTitle(getString(R.string.create_diary_showcase_title_1))
                .setContentText(getString(R.string.create_diary_showcase_message_1))
                .setStyle(R.style.ShowcaseTheme)
                .singleShot(DiaryConstants.SHOWCASE_SINGLE_SHOT_CREATE_DIARY_NUMBER)
                .setOnClickListener(showcaseViewOnClickListener)
                .build();
        mShowcaseView.setButtonText(getString(R.string.create_diary_showcase_button_1));
        mShowcaseView.setButtonPosition(centerParams);
    }

    public void initSpinner() {
        String[]  weatherArr = getResources().getStringArray(R.array.weather_item_array);
        ArrayAdapter arrayAdapter = new DiaryWeatherArrayAdapter(CreateDiaryActivity.this, R.layout.spinner_item_diary_weather_array_adapter, Arrays.asList(weatherArr));
        mWeatherSpinner.setAdapter(arrayAdapter);
    }

    private void bindView() {}

    private void bindEvent() {
//        mInputMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked) {
//                    mTitle.setEnabled(false);
//                    mContents.setEnabled(false);
//                    DialogUtils.makeSnackBar(findViewById(android.R.id.content), getString(R.string.input_mode_a));
//                } else {
//                    CreateDiaryActivity.this.mTitle.setEnabled(true);
//                    CreateDiaryActivity.this.mContents.setEnabled(true);
//                    InputMethodManager imm = (InputMethodManager)CreateDiaryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (StringUtils.isEmpty(CreateDiaryActivity.this.mTitle.getText())) {
//                        imm.showSoftInput(mTitle, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                        CreateDiaryActivity.this.mTitle.clearFocus();
//                    } else  {
//                        CreateDiaryActivity.this.mContents.requestFocus();
//                        imm.showSoftInput(mContents, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                        CreateDiaryActivity.this.mContents.setSelection(CreateDiaryActivity.this.mContents.getText().length());
//                    }
//                    DialogUtils.makeSnackBar(findViewById(android.R.id.content), getString(R.string.input_mode_b));
//                }
//            }
//        });

        mToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRecognizer();
                } else {
                    disableRecognizer();
                }
            }
        });

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

        mToggleMicOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableRecognizer();
            }
        });

        mToggleMicOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableRecognizer();
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

    private void enableRecognizer() {
        mToggleMicOff.setVisibility(View.GONE);
        mToggleMicOn.setVisibility(View.VISIBLE);
        mSpeechButton.setVisibility(View.VISIBLE);
        mToggleSwitch.setChecked(true);
    }

    private void disableRecognizer() {
        mToggleMicOn.setVisibility(View.GONE);
        mToggleMicOff.setVisibility(View.VISIBLE);
        mSpeechButton.setVisibility(View.GONE);
        mToggleSwitch.setChecked(false);
    }

    @OnClick({R.id.speechButton, R.id.zoomIn, R.id.zoomOut, R.id.saveContents, R.id.photoView, R.id.datePicker, R.id.timePicker})
    public void onClick(View view) {
        float fontSize = mContents.getTextSize();

        switch(view.getId()) {
            case R.id.speechButton:
                showSpeechDialog();
                break;
            case R.id.zoomIn:
                CommonUtils.saveFloatPreference(CreateDiaryActivity.this, "font_size", fontSize + 5);
                setDiaryFontSize();
                break;
            case R.id.zoomOut:
                CommonUtils.saveFloatPreference(CreateDiaryActivity.this, "font_size", fontSize - 5);
                setDiaryFontSize();
                break;
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
//                pickIntent.setType("image/*");
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

    private void showSpeechDialog() {
        try {
            startActivityForResult(mRecognizerIntent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            DialogUtils.showAlertDialog(this, getString(R.string.recognizer_intent_not_found_message), new DialogInterface.OnClickListener() {
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
            case REQUEST_CODE_SPEECH_INPUT:
                if ((resultCode == RESULT_OK) && (data != null)) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (mCurrentCursor == 0) { // edit title
                        String title = String.valueOf(mTitle.getText());
                        StringBuilder sb = new StringBuilder(title);
                        sb.insert(mTitle.getSelectionStart(), result.get(0));
                        int cursorPosition = mTitle.getSelectionStart() + result.get(0).length();
                        mTitle.setText(sb.toString());
                        mTitle.setSelection(cursorPosition);
                    } else {                   // edit contents
                        String contents = String.valueOf(mContents.getText());
                        StringBuilder sb = new StringBuilder(contents);
                        sb.insert(mContents.getSelectionStart(), result.get(0));
                        int cursorPosition = mContents.getSelectionStart() + result.get(0).length();
                        mContents.setText(sb.toString());
                        mContents.setSelection(cursorPosition);
                    }
                }
                break;
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
