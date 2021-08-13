package com.coremacasia.learnat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coremacasia.learnat.databinding.FragmentProfileBinding;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.utility.Getter;
import com.coremacasia.learnat.utility.MyStore;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private ImageView imageView;
    private TextView tName, tEmail, tPreparing, tCategory, tChange;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        imageView = binding.imageView4;
        tName = binding.textView11;
        tEmail = binding.textView12;
        tPreparing = binding.textView14;
        tCategory = binding.textView16;
        tChange = binding.textView22;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserHelper helper= MyStore.getUserData();
        if(helper!=null){
            tName.setText(helper.getName());
            tEmail.setText(helper.getEmail());
            String category=new Getter().getCategoryName(getActivity(),helper.getPreferred_type1());
            tCategory.setText(category);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}