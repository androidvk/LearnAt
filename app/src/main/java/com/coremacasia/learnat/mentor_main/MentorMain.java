package com.coremacasia.learnat.mentor_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.adapters.Inside_courseAdapter;
import com.coremacasia.learnat.databinding.ActivityMentorMainBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.mentor_main.adapters.MSubjectAdapter;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import java.util.ArrayList;

public class MentorMain extends AppCompatActivity {
    private static final String TAG = "MentorMain";
    private ActivityMentorMainBinding binding;

    private ImageView iMentorImage;
    private TextView tName, tStudentCount, tRating, tDescription;
    private RecyclerView rvSubjects, rvTeachingNow, rvAllCourses;
    private MentorHelper mentorHelper;
    private UserHelper userHelper;
    private ArrayList<CourseHelper> courseList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMentorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.MentorPanel));
        getIds();

        if (MyStore.getUserData() != null) {
            userHelper = MyStore.getUserData();
            Log.e(TAG, "onCreate: UserHelper:" + userHelper.getMentor_id());
            for (MentorHelper helper : MyStore.getCommonData().getMentors()) {
                Log.e(TAG, "onCreate: MentorHelper:" + helper.getMentor_id());
                if (helper.getMentor_id().equals(userHelper.getMentor_id())) {
                    mentorHelper = helper;
                    Log.e(TAG, "onCreate: If Mentor:" + userHelper.getMentor_id());
                    setValues();
                    break;
                }

            }


        }
    }

    private void setValues() {
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getMentor_id().equals(mentorHelper.getMentor_id())) {
                courseList.add(helper);
            }
        }

        new ImageSetterGlide().defaultImg(this, mentorHelper.getImage(), iMentorImage);
        tName.setText(mentorHelper.getName());
        setRecyclerView();
    }

    private void setRecyclerView() {
        setSubjectsRV();
        setTeachingNowRV();

    }

    private void setTeachingNowRV() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //MTeachingNowAdapter adapter=new MTeachingNowAdapter(getApplicationContext() );
        Inside_courseAdapter adapter = new Inside_courseAdapter(getApplicationContext());
        rvTeachingNow.setLayoutManager(linearLayoutManager);
        rvTeachingNow.setAdapter(adapter);
        adapter.setDataModel(courseList);
        adapter.notifyDataSetChanged();
    }

    private void setSubjectsRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MSubjectAdapter adapter = new MSubjectAdapter(getApplicationContext());
        rvSubjects.setLayoutManager(linearLayoutManager);
        rvSubjects.setAdapter(adapter);
        adapter.setDataModel(mentorHelper.getSubjects());
        adapter.notifyDataSetChanged();

    }

    private void getIds() {
        iMentorImage = binding.imageView22;
        tName = binding.textView55;
        tStudentCount = binding.tStudentCount;
        tRating = binding.textView57;
        tDescription = binding.textView59;
        rvSubjects = binding.recyclerView5;
        rvTeachingNow = binding.recyclerView6;
        rvAllCourses = binding.recyclerView7;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}