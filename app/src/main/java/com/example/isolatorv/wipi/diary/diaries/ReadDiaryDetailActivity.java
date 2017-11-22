package com.example.isolatorv.wipi.diary.diaries;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.Utils.BitmapUtils;
import com.example.isolatorv.wipi.diary.Utils.CommonUtils;
import com.example.isolatorv.wipi.diary.Utils.DateUtils;
import com.example.isolatorv.wipi.diary.Utils.DialogUtils;
import com.example.isolatorv.wipi.diary.Utils.EasyDiaryUtils;
import com.example.isolatorv.wipi.diary.Utils.FontUtils;
import com.example.isolatorv.wipi.diary.helper.EasyDiaryActivity;
import com.example.isolatorv.wipi.diary.photo.PhotoViewPagerActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

public class ReadDiaryDetailActivity extends EasyDiaryActivity {


    @BindView(R.id.container)
    ViewPager mViewPager;



    private SectionsPagerAdapter mSectionsPagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        // fixme elegance start    =============================================================================
        // activity destroy 시 저장된 savedInstance 값이 전달되면 갱신된 fragment 접근이 안됨
        // super.onCreate(savedInstanceState);
        super.onCreate(null);

        final int startPageIndex;
        // init viewpager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getIntent().getStringExtra("query"));
        if (savedInstanceState == null) {
            startPageIndex = mSectionsPagerAdapter.sequenceToPageIndex(getIntent().getIntExtra("sequence", -1));
        } else {
            startPageIndex = mSectionsPagerAdapter.sequenceToPageIndex(savedInstanceState.getInt("sequence", -1));
        }
        // fixme elegance end      =============================================================================

        setContentView(R.layout.activity_read_diary_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.read_diary_detail_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FontUtils.setToolbarTypeface(toolbar, Typeface.DEFAULT);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PlaceholderFragment fragment = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
//                Log.i("determine", String.valueOf(fragment.getActivity()));
                if (fragment.getActivity() != null) {
                    //fragment.setDiaryTypeface();
                    //fragment.setDiaryFontSize();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(startPageIndex, false);
            }
        });

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        PlaceholderFragment fragment = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
        outState.putInt("sequence", fragment.mSequence);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

   /* @OnClick({R.id.delete, R.id.edit})
    public void onClick(View view) {

//        ViewGroup viewPagerRootView = (ViewGroup) mViewPager.getChildAt(0);
//        mViewPager.setCurrentItem(2);
//        float fontSize = ((TextView) viewPagerRootView.findViewById(R.id.title)).getTextSize();
        final PlaceholderFragment fragment = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
        float fontSize = fragment.mTitle.getTextSize();

        switch(view.getId()) {

            case R.id.delete:
                DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DiaryDao.deleteDiary(fragment.mSequence);
                        finish();
                    }
                };
                DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                };
                DialogUtils.showAlertDialog(ReadDiaryDetailActivity.this, getString(R.string.delete_confirm), positiveListener, negativeListener);
                break;
            case R.id.edit:
                Intent updateDiaryIntent = new Intent(ReadDiaryDetailActivity.this, UpdateDiaryActivity.class);
                updateDiaryIntent.putExtra("sequence", fragment.mSequence);
                startActivity(updateDiaryIntent);
                finish();
                break;
        }
    }*/



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String DIARY_SEQUENCE = "diary_sequence";
        private static final String DIARY_SEARCH_QUERY = "diary_search_query";
        private int mSequence;

        @BindView(R.id.viewTitle)
        TextView mTitle;

        @BindView(R.id.contentsDetail)
        TextView mContents;

        @BindView(R.id.currentTimeDetail)
        TextView mCurrentTime;

        @BindView(R.id.deleteBtn)
        Button mDeleteBtn;

        @BindView(R.id.textDetail)
        TextView mText;

        @BindView(R.id.viewTimeDetail)
        TextView mViewTime;

        @BindView(R.id.weatherDetail)
        ImageView mWeather;

        @BindView(R.id.photoContainerDetail)
        ViewGroup mPhotoContainer;

        @BindView(R.id.photoContainerScrollViewDetail)
        HorizontalScrollView mHorizontalScrollView;

        public PlaceholderFragment() {}

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sequence, String query) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(DIARY_SEQUENCE, sequence);
            args.putString(DIARY_SEARCH_QUERY, query);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // bind view
            View rootView = inflater.inflate(R.layout.fragment_read_diary_detail, container, false);
            mContents = (TextView) rootView.findViewById(R.id.contentsDetail);
            mTitle = (TextView) rootView.findViewById(R.id.viewTitle);
            mViewTime = (TextView) rootView.findViewById(R.id.viewTimeDetail);
            mWeather = (ImageView) rootView.findViewById(R.id.weatherDetail);
            mCurrentTime = (TextView)rootView.findViewById(R.id.currentTimeDetail);
            mText = (TextView)rootView.findViewById(R.id.textDetail);
            mDeleteBtn = (Button)rootView.findViewById(R.id.deleteBtn);
            ViewGroup mPhotoContainer = (ViewGroup) rootView.findViewById(R.id.photoContainerDetail);
            HorizontalScrollView mHorizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.photoContainerScrollViewDetail);

            mSequence = getArguments().getInt(DIARY_SEQUENCE);
            DiaryDto diaryDto = DiaryDao.readDiaryBy(mSequence);
            if (StringUtils.isEmpty(diaryDto.getTitle())) {
                mTitle.setVisibility(View.GONE);
            }

            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DiaryDao.deleteDiary(mSequence);
                            getActivity().finish();
                        }
                    };
                    DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    };
                    DialogUtils.showAlertDialog(getActivity(), getString(R.string.delete_confirm), positiveListener, negativeListener);
                }
            });
            mTitle.setText(diaryDto.getTitle());
            mContents.setText(diaryDto.getContents());
            String fullTime = DateUtils.getFullPatternDateWithTime(diaryDto.getCurrentTimeMillis());
            String divTime1 = fullTime.substring(0,17);
            Log.d("TAG", String.valueOf(fullTime.length()));
            mViewTime.setText(divTime1);
            mCurrentTime.setText(fullTime.substring(17));


            String query = getArguments().getString(DIARY_SEARCH_QUERY);
            if (StringUtils.isNotEmpty(query)) {
                EasyDiaryUtils.highlightString(mTitle, query);
                EasyDiaryUtils.highlightString(mContents, query);
            }

            int weather = diaryDto.getWeather();
            Log.d("TAG", String.valueOf(weather));

            EasyDiaryUtils.initWeatherView(mWeather, weather);
            switch (weather){
                case 0 :
                    mText.setText("");
                    break;
                case 1:
                    mText.setText("맑음");
                    break;
                case 2:
                    mText.setText("흐림");
                    break;
                case 3:
                    mText.setText("비");
                    break;
                case 4:
                    mText.setText("번개");
                    break;
                case 5:
                    mText.setText("눈");
                    break;
            }

            // TODO fixme elegance
            if (diaryDto.getPhotoUris() != null && diaryDto.getPhotoUris().size() > 0) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoViewPager = new Intent(getContext(), PhotoViewPagerActivity.class);
                        photoViewPager.putExtra("sequence", mSequence);
                        startActivity(photoViewPager);
                    }
                };

                for (PhotoUriDto dto : diaryDto.getPhotoUris()) {
                    Uri uri = Uri.parse(dto.getPhotoUri());

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapUtils.decodeUri(getContext(), uri, CommonUtils.dpToPixel(getContext(), 70, 1), CommonUtils.dpToPixel(getContext(), 60, 1), CommonUtils.dpToPixel(getContext(), 40, 1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.question_mark_4);
                    }
                    ImageView imageView = new ImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(CommonUtils.dpToPixel(getContext(), 70, 1), CommonUtils.dpToPixel(getContext(), 50, 1));
                    layoutParams.setMargins(0, 0, CommonUtils.dpToPixel(getContext(), 3, 1), 0);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setBackgroundResource(R.drawable.bg_card_01);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    mPhotoContainer.addView(imageView);
                    imageView.setOnClickListener(onClickListener);
                }
            } else {
                mHorizontalScrollView.setVisibility(View.GONE);
            }
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
           // setDiaryTypeface();
           // setDiaryFontSize();
        }



    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<DiaryDto> mDiaryList;
        private List<PlaceholderFragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, String query) {
            super(fm);
            this.mDiaryList = DiaryDao.readDiary(query);
            for (DiaryDto diaryDto : mDiaryList) {
                mFragmentList.add(PlaceholderFragment.newInstance(diaryDto.getSequence(), query));
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        public PlaceholderFragment getFragment(int position) {
            return mFragmentList.get(position);
        }

        public int sequenceToPageIndex(int sequence) {
            int pageIndex = 0;
            if (sequence > -1) {
                for (int i = 0; i < mDiaryList.size(); i++) {
                    if (mDiaryList.get(i).getSequence() == sequence) {
                        pageIndex = i;
                        break;
                    }
                }
            }
            return pageIndex;
        }

        @Override
        public int getCount() {
            return mDiaryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}
