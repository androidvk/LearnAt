package com.coremacasia.learnat.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coremacasia.learnat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Dialog_Google_Reauth extends BottomSheetDialogFragment {
    private static final String TAG = "Dialog_Google_Reauth";
    private static String email;

    public static Dialog_Google_Reauth newInstance(String email) {
        Dialog_Google_Reauth.email = email;

        Bundle args = new Bundle();

        Dialog_Google_Reauth fragment = new Dialog_Google_Reauth();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ContinueWithSameAccount {
        void onSingInWithClick(Boolean continueLogin);
        void onChangeAccountClick(Boolean changeAccount);
    }

    public ContinueWithSameAccount continueWithSameAccount;

    public void onContinueClick(ContinueWithSameAccount continueWithSameAccount) {
        this.continueWithSameAccount = continueWithSameAccount;
    }

    private TextView tSignInWith;
    private Button bChangeAccount;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.dialog_google_reauth, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tSignInWith = view.findViewById(R.id.tSignInWithGoogle);
        bChangeAccount = view.findViewById(R.id.button6);
        tSignInWith.setText(getString(R.string.SignInWith) + "  " + email);

        tSignInWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueWithSameAccount.onSingInWithClick(true);
            }
        });

        bChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueWithSameAccount.onChangeAccountClick(false);
            }
        });
    }
}
