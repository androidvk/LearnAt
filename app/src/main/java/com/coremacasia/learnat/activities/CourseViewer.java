package com.coremacasia.learnat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.fragments.AboutCourseFragment;
import com.coremacasia.learnat.fragments.CheckoutFragment;
import com.coremacasia.learnat.fragments.LectureList;
import com.coremacasia.learnat.fragments.ViewPagerAdapter;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.Getter;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CourseViewer extends AppCompatActivity {
    private static final String TAG = "InsideCourse_activity";
    private ImageView backGround, teacherPng;
    private String course_id, CAT, FROM;
    private CourseHelper mHelper;
    private String category;
    private TextView tLiveClasses,tStartDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Gson gson = new Gson();
        mHelper = gson.fromJson(getIntent().getStringExtra("helper"),CourseHelper.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        course_id = getIntent().getStringExtra("courseId");
        CAT = getIntent().getStringExtra("category");
        backGround = findViewById(R.id.imageView17);
        teacherPng = findViewById(R.id.imageView15);
        tLiveClasses = findViewById(R.id.textView50);
        tStartDate =findViewById(R.id.textView48);
        tabLayoutConfig();
        onClicks();
        category = new Getter().getCategoryName(CourseViewer.this, CAT);

        setViews();
    }

    private void setViews() {
        if (mHelper.isIs_live()) {
            tLiveClasses.setText(getString(R.string.LiveClasses));
        } else tLiveClasses.setText(getString(R.string.RecordedClass));

        String wallpaper = "https://learnat.in/wp-content/uploads/2" +
                "021/08/17879-scaled-e1628793009125.jpg";
        new ImageSetterGlide().defaultImg(CourseViewer.this, wallpaper,
                backGround);
        ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
        getSupportActionBar().setTitle(category+" - "+mHelper.getTitle());

        for (MentorHelper helper1 : mentorList) {
            if (mHelper.getMentor_id().equals(helper1.getMentor_id())) {
                new ImageSetterGlide().defaultImg
                        (CourseViewer.this, helper1.getImage(),
                                teacherPng);
            }
        }

        if (mHelper.getStart_date() != null) {
            tStartDate.setVisibility(View.VISIBLE);
            String myFormat = "dd-MMMM"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tStartDate.setText("" + sdf.format(mHelper.getStart_date()));
        } else tStartDate.setVisibility(View.GONE);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void onClicks() {
        findViewById(R.id.buttonSubcribe).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(mHelper);
                        getSupportActionBar().setTitle(getString(R.string.GetSubscription));
                        Bundle bundle = new Bundle();
                        bundle.putString("cat", CAT);
                        bundle.putString("helper",myJson);
                        bundle.putString("from", "InsideCourse");
                        bundle.putString("courseId", course_id);

                        FragmentTransaction fragmenttransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmenttransaction.setCustomAnimations(R.anim.enter_from_bottom,
                                R.anim.exit_to_left,
                                R.anim.enter_from_left,
                                R.anim.exit_to_bottom);
                        CheckoutFragment frag = new CheckoutFragment();
                        frag.setArguments(bundle);
                        fragmenttransaction.replace(android.R.id.content, frag)
                                .addToBackStack(frag.TAG);
                        fragmenttransaction.commit();
                    }
                });
    }

    private void tabLayoutConfig() {
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.addFragment(new AboutCourseFragment().newInstance(course_id, CAT));
        adapter.addFragment(new LectureList().newInstance(course_id, CAT));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
        tabLayout.getTabAt(0).setText(getString(R.string.About));
        tabLayout.getTabAt(1).setText(getString(R.string.Lectures));
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mHelper != null) {
            getSupportActionBar().setTitle(mHelper.getTitle());
        }

        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mHelper != null) {
            getSupportActionBar().setTitle(category+" - "+mHelper.getTitle());
        }
    }
}