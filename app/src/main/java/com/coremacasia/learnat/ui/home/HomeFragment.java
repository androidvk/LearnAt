package com.coremacasia.learnat.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.databinding.FragmentHomeBinding;
import com.coremacasia.learnat.startItems.CategoryHelper;
import com.coremacasia.learnat.ui.home.categories.CategoriesAdapter;
import com.coremacasia.learnat.ui.home.mentors.MentorAdapter;
import com.coremacasia.learnat.ui.home.popular.PopularAdapter;
import com.coremacasia.learnat.ui.home.popular.PopularViewModel;
import com.coremacasia.learnat.ui.home.subjects.SubjectAdapter;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private RecyclerView rPopular, rSubjects, rMentors, rCourseCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getIds();
        return root;
    }

    private void getIds() {
        rPopular = binding.rev2;
        rMentors = binding.rMentor;
        rSubjects = binding.recyclerView3;
        rCourseCategory = binding.recyclerViewOt;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable
                                      Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerViewPopular();
        setRecyclerViewSubject();
        setRecyclerViewMentor();
        setRecyclerViewCategory();
    }

    private CommonDataViewModel viewModel;
    private PopularViewModel popularViewModel;
    DocumentReference commonListRef = Reference.superRef(RMAP.list);
    private void setRecyclerViewPopular() {
        DocumentReference popularListRef = Reference.superRef(RMAP.comp_exam);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        PopularAdapter adapter = new PopularAdapter(getActivity());
        rPopular.setLayoutManager(linearLayoutManager);
        rPopular.setAdapter(adapter);

        popularViewModel = new ViewModelProvider(getActivity()).get(PopularViewModel.class);
        popularViewModel.getMutableLiveData(popularListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void setRecyclerViewSubject() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        SubjectAdapter adapter = new SubjectAdapter(getActivity());
        rSubjects.setLayoutManager(linearLayoutManager);
        rSubjects.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void setRecyclerViewMentor() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MentorAdapter adapter = new MentorAdapter(getActivity());
        rMentors.setLayoutManager(linearLayoutManager);
        rMentors.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void setRecyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        rCourseCategory.setLayoutManager(linearLayoutManager);
        rCourseCategory.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}