package com.coremacasia.learnat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.activities.CourseViewer;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.PriceDurationHelper;
import com.coremacasia.learnat.utility.MyStore;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trans_status#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trans_status extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = "Trans_Status";
    private float discount=0f;
    public Trans_status() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trans_status.
     */
    // TODO: Rename and change types and number of parameters
    public static Trans_status newInstance(String param1, String param2) {
        Trans_status fragment = new Trans_status();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private CourseHelper courseHelper;
    private PriceDurationHelper priceDurationHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Gson gson = new Gson();
            discount =getArguments().getFloat("discount");
            courseHelper = gson.fromJson(getArguments().
                    getString("helper"), CourseHelper.class);
            priceDurationHelper = gson.fromJson(getArguments().
                    getString("selectedPackage"), PriceDurationHelper.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_status, container, false);
    }

    private TextView tStartLearning;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tStartLearning = view.findViewById(R.id.textStartLearning);
        onClicks();
    }

    private void onClicks() {
        subscribe();

        tStartLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String myJson = gson.toJson(courseHelper);
                getActivity().startActivity(new Intent(getActivity(),
                        CourseViewer.class).putExtra("helper", myJson)
                        .putExtra("courseId", courseHelper.getCourse_id())
                        .putExtra("category", courseHelper.getCategory_id())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
    }

    // TODO: 29-12-2021 SeverSide
    private void subscribe() {
        String referral=null;
        float cashback=0f;
        String referred_by=null;
        String durationUnit = priceDurationHelper.getDuration_unit();
        String duration = priceDurationHelper.getDuration();
        float price=Float.parseFloat(priceDurationHelper.getPrice());
        Date startDate=courseHelper.getStart_date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startDate);
        Date endDate = null;

        if (!durationUnit.equals(RMAP.Days)) {
            if (durationUnit.equals(RMAP.Month)) {
                calendar.add(Calendar.MONTH,Integer.parseInt(duration));
                endDate=new Date(calendar.getTimeInMillis());
            } else if (durationUnit.equals(RMAP.Year)) {
                calendar.add(Calendar.YEAR,Integer.parseInt(duration));
                endDate=new Date(calendar.getTimeInMillis());
            }
        } else {
            calendar.add(Calendar.DATE,Integer.parseInt(duration));
            endDate=new Date(calendar.getTimeInMillis());
        }

        float subsPrice=price-discount;


        DocumentReference reference = Reference.userRef(MyStore.getUserData().getFirebase_id());
        Map map = new HashMap();
        map.put(kMap.subs_date, new Date());
        map.put(kMap.start_date, courseHelper.getStart_date());
        map.put(kMap.end_date, endDate);
        map.put(kMap.subs_price, subsPrice);
        map.put(kMap.gross_price,price);
        map.put(kMap.referral, referral);
        map.put(kMap.cashback_received, cashback);
        map.put(kMap.discount_received, discount);
        map.put(kMap.referred_by,referred_by );
        map.put(kMap.course_id, courseHelper.getCourse_id());

        //coursePrice

        Map map1 = new HashMap();
        map1.put("subscriptions", FieldValue.arrayUnion(map));
        reference.set(map1, SetOptions.merge());
    }

}