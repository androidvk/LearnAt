package com.coremacasia.learnat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.adapters.Inside_courseAdapter;
import com.coremacasia.learnat.adapters.MentorAdapter;
import com.coremacasia.learnat.databinding.FragmentSubjectViewBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.helpers.SubjectHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectViewFragment extends Fragment {
    public static final String TAG = "SubjectViewFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubjectViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectViewFragment newInstance(String param1, String param2) {
        SubjectViewFragment fragment = new SubjectViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String subjectId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            subjectId = getArguments().getString("subjectId");
        }
    }

    private FragmentSubjectViewBinding binding;
    private TextView tSubjectName, tFollow, tBio;
    private ImageView iBack, iSubjectImage;
    private RecyclerView recyclerViewCourses, recyclerViewMentors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubjectViewBinding.inflate(LayoutInflater.from(inflater.getContext()));
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tSubjectName = binding.textView75;
        tBio = binding.textView73;
        iBack = binding.imageView25;
        iSubjectImage = binding.imageView30;
        recyclerViewCourses = binding.recyclerView;
        recyclerViewMentors = binding.recyclerView2;
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        for (SubjectHelper helper : MyStore.getCommonData().getAll_subjects()) {
            if (helper.getSubject_id().equals(subjectId)) {
                tSubjectName.setText(helper.getTitle());
                new ImageSetterGlide().defaultImg(getActivity(), helper.getIcon(), iSubjectImage);
            }
        }

        ArrayList<CourseHelper> list = new ArrayList<>();
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getSubject_id().equals(subjectId)) {
                list.add(helper);
            }
        }
        setRecyclerViewCourses(list);
        setRecyclerViewMentor();
    }

    private ArrayList<String> subjectMentorList = new ArrayList<>();

    private void setRecyclerViewMentor() {
        Log.e(TAG, "setRecyclerViewMentor: 1");
        int i = 0;
        for (MentorHelper helper : MyStore.getCommonData().getMentors()) {
            i++;
            for (String s : helper.getSubjects()) {
                if (s.equals(subjectId)) {
                    subjectMentorList.add(helper.getMentor_id());
                    break;
                }
            }

            if (i == MyStore.getCommonData().getMentors().size() - 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                MentorAdapter adapter = new MentorAdapter(getActivity());
                recyclerViewMentors.setLayoutManager(linearLayoutManager);
                recyclerViewMentors.setAdapter(adapter);
                adapter.setDataModel(subjectMentorList);
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void setRecyclerViewCourses(ArrayList<CourseHelper> list) {
        LinearLayoutManager lManagerTrending = new LinearLayoutManager(getActivity());
        lManagerTrending.setOrientation(LinearLayoutManager.HORIZONTAL);
        Inside_courseAdapter adapter = new Inside_courseAdapter(getActivity());
        recyclerViewCourses.setLayoutManager(lManagerTrending);
        recyclerViewCourses.setAdapter(adapter);
        adapter.setDataModel(list);
        adapter.notifyDataSetChanged();


    }
}