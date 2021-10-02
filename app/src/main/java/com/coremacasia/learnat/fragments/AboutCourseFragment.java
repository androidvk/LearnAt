package com.coremacasia.learnat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.databinding.FragmentAboutCourseBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.MyStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutCourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String courseId="1";
    private static String categoryId="2";

    // TODO: Rename and change types of parameters
    private String course_id;
    private String category_id;
    private static final String TAG = "AboutCourseFrag";

    public AboutCourseFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutCourseFragment newInstance(String param1, String param2) {
        AboutCourseFragment fragment = new AboutCourseFragment();
        Bundle args = new Bundle();
        args.putString(courseId, param1);
        args.putString(categoryId, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course_id = getArguments().getString(courseId);
            category_id = getArguments().getString(categoryId);
        }
    }

    private FragmentAboutCourseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutCourseBinding.inflate(LayoutInflater.from(inflater.getContext()));
        return binding.getRoot();
    }

    private TextView tSubjectName, tCourseName, tDescription, tLiveClasses, tMentorName;
    private RecyclerView recyclerViewSyllabus;
    private ImageView iFav;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tCourseName = binding.textView46;
        tMentorName = binding.textView47;
        tDescription = binding.textView44;
        iFav = binding.imageView16;
        recyclerViewSyllabus = binding.recyclerView;

        getCourseDetails();

    }

    private void getCourseDetails() {
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                setCourseViews(helper);
                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                    tMentorName.setText(helper1.getName());
                    }
                }

            }
        }
    }

    private void setCourseViews(CourseHelper helper) {
        Log.e(TAG, "setCourseViews: " );
        tCourseName.setText(helper.getTitle());

        tDescription.setText(helper.getDesc());
    }
}