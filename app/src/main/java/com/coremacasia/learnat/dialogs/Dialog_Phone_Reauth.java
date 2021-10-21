package com.coremacasia.learnat.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coremacasia.learnat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Dialog_Phone_Reauth extends BottomSheetDialogFragment {
    private static String ONLY_NUMBER;

    public static Dialog_Phone_Reauth newInstance(String ONLY_NUMBER) {
        Dialog_Phone_Reauth.ONLY_NUMBER = ONLY_NUMBER;
        Bundle args = new Bundle();
        Dialog_Phone_Reauth fragment = new Dialog_Phone_Reauth();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bLoginWith.setText(getString(R.string.LoginWith)+" "+ONLY_NUMBER);
        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneLinkDialog();
                dismiss();
            }
        });
    }

    private Button bLoginWith;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_phone_reauth, container, false);
        bLoginWith=view.findViewById(R.id.button5);
        return view;
    }

    private void showPhoneLinkDialog() {
        DF_link_phone dialog = DF_link_phone.newInstance();
        dialog.setCancelable(false);
        dialog.show(((AppCompatActivity) getActivity())
                .getSupportFragmentManager(), DF_link_phone.TAG);

    }
}
