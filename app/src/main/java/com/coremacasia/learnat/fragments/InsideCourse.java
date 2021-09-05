package com.coremacasia.learnat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.databinding.FragmentInsideCourseBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsideCourse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsideCourse extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Lecture_fragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InsideCourse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lecture_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsideCourse newInstance(String param1, String param2) {
        InsideCourse fragment = new InsideCourse();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            CAT=getArguments().getString("cat");
            FROM=getArguments().getString("from");
            course_id=getArguments().getString("courseId");

        }
    }

    private FragmentInsideCourseBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView backGround,teacherPng;
    private String course_id,CAT,FROM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInsideCourseBinding.inflate(inflater, container, false);
        tabLayout = binding.tablayout;
        viewPager = binding.viewPager;
        return binding.getRoot();
    }

    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayoutConfig();
        onClicks(view);
        toolbar = view.findViewById(R.id.toolbar2);
        toolbarTitle=view.findViewById(R.id.toolbar_title);
        backGround=view.findViewById(R.id.imageView17);
        teacherPng=view.findViewById(R.id.imageView15);
        /* AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Quick Maths By Alok Sir");*/
        String wallpaper = "https://learnat.in/wp-content/uploads/2021/08/17879-scaled-e1628793009125.jpg";
        new ImageSetterGlide().defaultImg(getActivity(), wallpaper,
                backGround);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                toolbarTitle.setText(helper.getTitle());

                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        new ImageSetterGlide().defaultImg(getActivity(), helper1.getImage(),
                                teacherPng);
                    }
                }

            }
        }

    }

    private void onClicks(View view) {
        view.findViewById(R.id.iBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void tabLayoutConfig() {
        FragmentManager manager = ((AppCompatActivity) getActivity())
                .getSupportFragmentManager();
        adapter = new ViewPagerAdapter(manager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AboutCourseFragment().newInstance(course_id,null));
        adapter.addFragment(new LectureList().newInstance(course_id,null));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0, true);
        tabLayout.getTabAt(0).setText(getString(R.string.About));
        tabLayout.getTabAt(1).setText(getString(R.string.Lectures));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

}