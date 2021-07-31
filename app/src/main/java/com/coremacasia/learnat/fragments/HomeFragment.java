package com.coremacasia.learnat.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.databinding.FragmentHomeBinding;
import com.coremacasia.learnat.adapters.CategoriesAdapter;
import com.coremacasia.learnat.adapters.MentorAdapter;
import com.coremacasia.learnat.adapters.PopularAdapter;
import com.coremacasia.learnat.repos.PopularViewModel;
import com.coremacasia.learnat.adapters.SubjectAdapter;
import com.coremacasia.learnat.adapters.UpcomingAdapter;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private RecyclerView rPopular, rSubjects, rMentors, rCourseCategory, rUpcoming;

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
        rUpcoming = binding.rUpcoming;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable
                                      Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setRecyclerViewPopular();
        setRecyclerViewSubject();
        setRecyclerViewMentor();
        setRecyclerViewCategory();
        //setRecyclerViewUpcoming();
    }


    private CommonDataViewModel viewModel;
    private PopularViewModel popularViewModel;
    Handler handler = new Handler();
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

    private int currentPosition, totalItem;
    private final LinearLayoutManager lManagerUpcoming = new LinearLayoutManager(getActivity());
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            if (currentPosition != totalItem) {
                lManagerUpcoming.smoothScrollToPosition(rUpcoming, null, currentPosition + 1);
            } else {
                lManagerUpcoming.scrollToPosition(0);
            }
        }
    };

    private void setRecyclerViewUpcoming() {
        ScrollingPagerIndicator scrollingPagerIndicator = binding.scrollingIndicator;

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rUpcoming);
        DocumentReference popularListRef = Reference.superRef(RMAP.comp_exam);

        lManagerUpcoming.setOrientation(LinearLayoutManager.HORIZONTAL);
        UpcomingAdapter adapter = new UpcomingAdapter(getActivity());
        rUpcoming.setLayoutManager(lManagerUpcoming);
        rUpcoming.setAdapter(adapter);
        scrollingPagerIndicator.attachToRecyclerView(rUpcoming);
        popularViewModel = new ViewModelProvider(getActivity()).get(PopularViewModel.class);
        popularViewModel.getMutableLiveData(popularListRef).observe(getViewLifecycleOwner(),
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        adapter.setDataModel(commonDataModel);
                        adapter.notifyDataSetChanged();
                    }
                });

        rUpcoming.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                currentPosition = lManagerUpcoming.findFirstCompletelyVisibleItemPosition();
                totalItem = lManagerUpcoming.getItemCount() - 1;
                mHandler.postDelayed(SCROLLING_RUNNABLE, 4000);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}