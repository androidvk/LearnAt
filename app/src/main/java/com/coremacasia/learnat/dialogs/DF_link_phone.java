package com.coremacasia.learnat.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.activities.PhoneAuth;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hbb20.CountryCodePicker;

public class DF_link_phone extends BottomSheetDialogFragment {
    public static final String TAG = "DF_link_phone";
    private View view;

    public static DF_link_phone newInstance() {
        Bundle args = new Bundle();
        DF_link_phone fragment = new DF_link_phone();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_number_enter, container, false);
        return view;
    }

    private EditText eNumber;
    private CountryCodePicker ccp;
    private Button bContinue;
    private TextView tSignIn;
    private ProgressBar progressBar;
    private TextView tSignupText;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eNumber = view.findViewById(R.id.eNumber);
        ccp = view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(eNumber);
        bContinue = view.findViewById(R.id.bContinue);
        tSignIn=view.findViewById(R.id.textView23);
        progressBar=view.findViewById(R.id.progressBar1);
        tSignIn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tSignupText=view.findViewById(R.id.textView5);
        tSignupText.setVisibility(View.GONE);
        eNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: " + s.toString());

                if ( ccp.getFullNumber().length() != 12) {
                    bContinue.setEnabled(false);
                } else {
                    bContinue.setEnabled(true);
                    bContinue.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bContinue.setVisibility(View.GONE);
                view.findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        startActivity(new Intent(getActivity(), PhoneAuth.class).putExtra(
                                "number",ccp.getFullNumberWithPlus()
                        ).putExtra("just_number",eNumber.getText().toString().trim()));
                        dismiss();


                    }
                }, 1000);
            }
        });
    }


}
