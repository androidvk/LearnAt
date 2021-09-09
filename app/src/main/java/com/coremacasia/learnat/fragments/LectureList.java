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

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.adapters.LectureAdapter;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LectureList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LectureList";
    // TODO: Rename and change types of parameters
    private String catagoreyId;
    private String course_id;

    public LectureList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LectureList.
     */
    // TODO: Rename and change types and number of parameters
    public static LectureList newInstance(String param1, String param2) {
        LectureList fragment = new LectureList();
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
            course_id = getArguments().getString(ARG_PARAM1);
            catagoreyId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_list, container, false);
    }

    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);
        getCourseData();
    }

    private CourseHelper courseHelper;

    private void getCourseData() {
        DocumentReference reference = Reference.superCourseRef(catagoreyId
                , course_id);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value,
                                @Nullable @org.jetbrains.annotations.Nullable
                                        FirebaseFirestoreException error) {
                if (value.exists()) {
                    courseHelper = value.toObject(CourseHelper.class);
                    Log.e(TAG, "onEvent: " );
                    setRecyclerView();
                }

            }
        });
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        LectureAdapter adapter = new LectureAdapter(getActivity());
        if (courseHelper.getLectures().size() != 0) {
            adapter.setLectureList(courseHelper.getLectures());
            recyclerView.setAdapter(adapter);
        } else Log.e(TAG, "setRecyclerView: No Data for Lectures");
    }


}