package com.coremacasia.learnat.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.commons.category_repo.CategoryViewModel;
import com.coremacasia.learnat.commons.user_repo.UserDataViewModel;
import com.coremacasia.learnat.databinding.FragmentHomeBinding;
import com.coremacasia.learnat.adapters.CategoriesAdapter;
import com.coremacasia.learnat.adapters.MentorAdapter;
import com.coremacasia.learnat.adapters.PopularAdapter;
import com.coremacasia.learnat.adapters.SubjectAdapter;
import com.coremacasia.learnat.adapters.TrendingAdapter;
import com.coremacasia.learnat.dialogs.DF_SubjectChooser;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.utility.MyStore;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private RecyclerView rPopular, rSubjects, rMentors, rCourseCategory, rTrending;
    private CategoryViewModel categoryViewModel;
    private UserHelper helper;

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
        rTrending = binding.rTrending;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lManagerTrending = new LinearLayoutManager(getActivity());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            DocumentReference userRef = Reference.userRef(firebaseUser.getUid());

            UserDataViewModel viewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
            viewModel.getMutableLiveData(userRef).observe(getActivity(), new Observer<UserHelper>() {
                @Override
                public void onChanged(UserHelper userHelper) {
                    helper = userHelper;
                    MyStore.setUserData(userHelper);
                    if(helper.getPreferred_type1()!=null){
                        categoryRef = Reference.superRef(helper.getPreferred_type1());
                        getCategoryData();
                    }else {
                        startSubjectChooser(2);
                    }

                }
            });
        }



    }
    private void startSubjectChooser(int From) {
        FragmentManager manager = ((AppCompatActivity) getActivity())
                .getSupportFragmentManager();
        DF_SubjectChooser df_number =
                DF_SubjectChooser.newInstance(From);
        df_number.show(manager,
                DF_SubjectChooser.TAG);
    }
    private void getCategoryData() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryMutableData(categoryRef).observe(this,
                new Observer<CategoryDashboardHelper>() {
                    @Override
                    public void onChanged(CategoryDashboardHelper categoryDashboardHelper) {
                        MyStore.setCategoryDashboardHelper(categoryDashboardHelper);
                        setRecyclerViewPopular();
                        setRecyclerViewSubject();
                        setRecyclerViewMentor();
                        setRecyclerViewCategory();
                        setRecyclerViewTrending();
                    }
                });

    }

    private CommonDataViewModel viewModel;
    Handler handler = new Handler();
    DocumentReference commonListRef = Reference.superRef(RMAP.list);
    private DocumentReference categoryRef;

    private void setRecyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        PopularAdapter adapter = new PopularAdapter(getActivity());
        rPopular.setLayoutManager(linearLayoutManager);
        rPopular.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCategoryDashboardHelper());
        adapter.notifyDataSetChanged();


    }

    private void setRecyclerViewTrending() {
        ScrollingPagerIndicator scrollingPagerIndicator = binding.scrollingIndicator;
        SnapHelper snapHelper = new PagerSnapHelper();
        rTrending.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(rTrending);

        lManagerTrending.setOrientation(LinearLayoutManager.HORIZONTAL);
        TrendingAdapter adapter = new TrendingAdapter(getActivity());
        rTrending.setLayoutManager(lManagerTrending);
        rTrending.setAdapter(adapter);
        scrollingPagerIndicator.attachToRecyclerView(rTrending);
        adapter.setDataModel(MyStore.getCategoryDashboardHelper());
        adapter.notifyDataSetChanged();

        rTrending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                currentPosition = lManagerTrending.findFirstCompletelyVisibleItemPosition();
                totalItem = lManagerTrending.getItemCount() - 1;
                mHandler.postDelayed(SCROLLING_RUNNABLE, 4000);
            }
        });

    }

    private void setRecyclerViewSubject() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        SubjectAdapter adapter = new SubjectAdapter(getActivity());
        rSubjects.setLayoutManager(linearLayoutManager);
        rSubjects.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCategoryDashboardHelper());
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerViewMentor() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MentorAdapter adapter = new MentorAdapter(getActivity());
        rMentors.setLayoutManager(linearLayoutManager);
        rMentors.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCategoryDashboardHelper());
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        rCourseCategory.setLayoutManager(linearLayoutManager);
        rCourseCategory.setAdapter(adapter);
        adapter.setDataModel(MyStore.getCommonData());
        adapter.notifyDataSetChanged();
    }

    private int currentPosition, totalItem;
    private  LinearLayoutManager lManagerTrending;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            if (currentPosition != totalItem) {
                lManagerTrending.smoothScrollToPosition(rTrending, null, currentPosition + 1);
            } else {
                lManagerTrending.scrollToPosition(0);
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}