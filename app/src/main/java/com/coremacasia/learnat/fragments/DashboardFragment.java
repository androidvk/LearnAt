package com.coremacasia.learnat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.databinding.FragmentDashboardBinding;
import com.coremacasia.learnat.repos.DashboardViewModel;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.google.firebase.firestore.DocumentReference;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private CommonDataViewModel viewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        DocumentReference commonListRef = Reference.superRef(RMAP.list);
        viewModel=new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(), new Observer<CommonDataModel>() {
            @Override
            public void onChanged(CommonDataModel commonDataModel) {
                Log.e(TAG, "onChanged: "+commonDataModel.getCategory().get(0).getDescription() );
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}