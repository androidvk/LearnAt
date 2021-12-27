package com.coremacasia.learnat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.adapters.PriceDurationAdapter;
import com.coremacasia.learnat.databinding.FragmentAboutCourseBinding;
import com.coremacasia.learnat.databinding.FragmentCheckoutBinding;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.PriceDurationHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {
    public static final String TAG = "CheckoutFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentCheckoutBinding binding;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    private CourseHelper helper;

    public static CheckoutFragment newInstance(String param1, String param2) {
        CheckoutFragment fragment = new CheckoutFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Gson gson = new Gson();
            helper = gson.fromJson(getArguments().
                    getString("helper"), CourseHelper.class);
        }
    }

    private TextView tCourseTitle, tLiveClass, tSubscriptionFee, tCreditUsed, tGrandTotal;
    private EditText eReferralCode;
    private TextView tApply, tLaunchOffer, tKnowMore;
    private Button bPay;
    private ImageView imageView;
    private RecyclerView recyclerViewPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(LayoutInflater.from(inflater.getContext()));
        tCourseTitle = binding.textView53;
        tLiveClass = binding.textView54;
        tLaunchOffer = binding.textView441;
        tSubscriptionFee = binding.textView62;
        tCreditUsed = binding.textView67;
        tGrandTotal = binding.textView65;
        tApply = binding.textView68;
        eReferralCode = binding.editTextTextPersonName;
        bPay = binding.button;
        tKnowMore = binding.tKnowMore;
        imageView = binding.imageView23;
        recyclerViewPrice = binding.recyclerView4;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setViews();
        onClicks();

    }

    private void onClicks() {
        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) getActivity())
                        .getSupportFragmentManager();

                FragmentTransaction transaction =
                        manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_bottom,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_bottom);
                Trans_status fragment = new Trans_status();
                transaction.replace(android.R.id.content, fragment)
                .addToBackStack(fragment.TAG);  //here, android.R.id.content is a view on which your fragment's view is replaced
                transaction.commit();
            }
        });
    }

    private void setViews() {

        tCourseTitle.setText(helper.getTitle());
        String isLive;
        if (helper.isIs_live()) {
            isLive = getString(R.string.LiveClasses);
        } else isLive = getString(R.string.RecordedClass);

        if (helper.getDuration() != 0) {
            tLiveClass.setText(isLive + " | " + helper.getDuration() + " " +
                    getString(R.string.Months));
        } else tLiveClass.setText(isLive);
        new ImageSetterGlide().defaultImg(getActivity(), helper.getThumbnail(), imageView);

        setPriceRecyclerView();
    }

    private ArrayList<PriceDurationHelper> price_durationList = new ArrayList<>();
    private PriceDurationHelper selectedPriceDuration = null;

    private void setPriceRecyclerView() {
        price_durationList = helper.getPrice_duration();
        if (price_durationList.size() >=2) {
            selectedPriceDuration = price_durationList.get(1);

        } else if (price_durationList.size() == 1) {
            selectedPriceDuration = price_durationList.get(0);

        }
        setUi();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        PriceDurationAdapter adapter = new PriceDurationAdapter(getActivity(), price_durationList);
        recyclerViewPrice.setLayoutManager(layoutManager);
        recyclerViewPrice.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerViewPrice.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.onPriceClick(new PriceDurationAdapter.OnPriceClickListener() {
            @Override
            public void onPriceClick(PriceDurationHelper helper) {
                selectedPriceDuration = helper;
                setUi();
            }
        });
    }

    // TODO: 08-10-2021 dynamize credit used amount
    private void setUi() {
        tSubscriptionFee.setText(getString(R.string.rupee_sign) + selectedPriceDuration.getPrice());
        int price = Integer.parseInt(selectedPriceDuration.getPrice());
        Log.e(TAG, "setUi: " + price);
        tGrandTotal.setText(getString(R.string.rupee_sign) + (price - 50));
        bPay.setText(getString(R.string.pay) + " " + getString(R.string.rupee_sign) + (price - 50));
    }

}