package com.coremacasia.learnat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import java.util.ArrayList;

public class CourseViewer extends AppCompatActivity {
    private static final String TAG = "InsideCourse_activity";
    private ImageView backGround, teacherPng;
    private String course_id, CAT, FROM;
    private CourseHelper mHelper;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        course_id = getIntent().getStringExtra("courseId");
        CAT = getIntent().getStringExtra("category");
        backGround = findViewById(R.id.imageView17);
        teacherPng = findViewById(R.id.imageView15);
        tabLayoutConfig();
        onClicks();
        category = new Getter().getCategoryName(CourseViewer.this, CAT);
    }


    @Override
    protected void onStart() {
        super.onStart();

        String wallpaper = "https://learnat.in/wp-content/uploads/2" +
                "021/08/17879-scaled-e1628793009125.jpg";
        new ImageSetterGlide().defaultImg(CourseViewer.this, wallpaper,
                backGround);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                mHelper = helper;
                getSupportActionBar().setTitle(category+" - "+helper.getTitle());
                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        new ImageSetterGlide().defaultImg
                                (CourseViewer.this, helper1.getImage(),
                                        teacherPng);
                    }
                }

            }
        }

    }

    private void onClicks() {
        findViewById(R.id.buttonSubcribe).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportActionBar().setTitle(getString(R.string.GetSubscription));
                        Bundle bundle = new Bundle();
                        bundle.putString("cat", CAT);
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