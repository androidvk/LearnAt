package com.coremacasia.learnat.startItems;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.References;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DF_SubjectChooser extends DialogFragment {
    public final static String TAG = "DF_SubjectChooser";
    private View view;
    private RecyclerView recyclerView;
    public static DF_SubjectChooser newInstance() {

        Bundle args = new Bundle();

        DF_SubjectChooser fragment = new DF_SubjectChooser();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_subject_chooser, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        setCancelable(false );
        view.findViewById(R.id.group1).setVisibility(View.GONE);
        return view;
    }

    CourseHelper helper;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DocumentReference reference = References.superRef(RMAP.course_menu);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    helper = documentSnapshot.toObject(CourseHelper.class);
                    ArrayList<menu_list> listHelper = helper.getMenu_list();
                    Log.e(TAG, "onSuccess: "+listHelper.get(0).getDesc_en() );

                    setRecyclerView(listHelper);
                }
            }
        });
    }

    private void setRecyclerView(ArrayList<menu_list> listHelper) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CourseMenuAdapter adapter=new CourseMenuAdapter(getActivity(),listHelper);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
