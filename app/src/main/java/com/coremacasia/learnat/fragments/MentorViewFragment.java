package com.coremacasia.learnat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.adapters.Inside_courseAdapter;
import com.coremacasia.learnat.databinding.FragmentMentorViewBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MentorViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentorViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = "MentorViewFragment";

    public MentorViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MentorView.
     */
    // TODO: Rename and change types and number of parameters
    public static MentorViewFragment newInstance(String param1, String param2) {
        MentorViewFragment fragment = new MentorViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String mentorId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            mentorId = getArguments().getString("mentorId");
        }
    }

    private FragmentMentorViewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMentorViewBinding.inflate(LayoutInflater.from(inflater.getContext()));
        return binding.getRoot();
    }
    private TextView tMentorName,tFollow,tBio;
    private ImageView iBack,iMentorImage;
    private RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tMentorName=binding.textView72;
        tBio=binding.textView73;
        tFollow=binding.textView69;
        iBack=binding.imageView25;
        iMentorImage=binding.imageView30;
        recyclerView=binding.recyclerView;
        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        for (MentorHelper helper : MyStore.getCommonData().getMentors()) {
            if (helper.getMentor_id().equals(mentorId)) {
                tMentorName.setText(helper.getName());
                new ImageSetterGlide().defaultImg(getActivity()
                        , helper.getImage(), iMentorImage);
            }
        }
        ArrayList<CourseHelper> list=new ArrayList<>();
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getMentor_id().equals(mentorId)) {
                list.add(helper);
            }
        }
        setRecyclerViewCourses(list);

    }
    private void setRecyclerViewCourses(ArrayList<CourseHelper> list) {
        LinearLayoutManager lManagerTrending=new LinearLayoutManager(getActivity());
        lManagerTrending.setOrientation(LinearLayoutManager.HORIZONTAL);
        Inside_courseAdapter adapter = new Inside_courseAdapter(getActivity());
        recyclerView.setLayoutManager(lManagerTrending);
        recyclerView.setAdapter(adapter);
        adapter.setDataModel(list);
        adapter.notifyDataSetChanged();


    }
}