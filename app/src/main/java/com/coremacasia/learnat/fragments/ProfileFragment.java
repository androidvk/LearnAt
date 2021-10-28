package com.coremacasia.learnat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coremacasia.learnat.MainActivity;
import com.coremacasia.learnat.R;
import com.coremacasia.learnat.activities.Splash;
import com.coremacasia.learnat.databinding.FragmentProfileBinding;
import com.coremacasia.learnat.dialogs.Dialog_Google_Reauth;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.utility.Getter;
import com.coremacasia.learnat.utility.MyStore;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private ImageView imageView;
    private TextView tName, tEmail, tPreparing, tCategory, tChange, tSignOut, tSignInWithGoogle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        imageView = binding.imageView4;
        tName = binding.textView11;
        tEmail = binding.textView12;
        tPreparing = binding.textView14;
        tCategory = binding.textView16;
        tChange = binding.textView22;
        tSignOut = binding.tSignOut;
        tSignInWithGoogle = binding.tSignInWithGoogle;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserHelper helper = MyStore.getUserData();
        if (helper != null) {

            String category = new Getter().getCategoryName(getActivity(), helper.getPreferred_type1());
            tCategory.setText(category);
            if (helper.getEmail() == null || helper.getEmail().equals("")) {
                tName.setVisibility(View.GONE);
                tEmail.setVisibility(View.GONE);
                tSignInWithGoogle.setVisibility(View.VISIBLE);
            } else {
                tName.setText(helper.getName());
                tEmail.setText(helper.getEmail());
                tSignInWithGoogle.setVisibility(View.GONE);
            }
        }

        onClicks();

    }

    private void onClicks() {

        tSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Splash.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        tSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInDialog();
            }
        });

    }

    public static final int RC_SIGN_IN = 9001;
    private Dialog_Google_Reauth dialog;

    private void googleSignInDialog() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.revokeAccess();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private static final String TAG = "Profile Fragment";

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                linkAccounts(account.getIdToken(), account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void linkAccounts(String idToken, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.e(TAG, "onComplete: " + user.getPhoneNumber() + user.getEmail());
                            writeUserData(user.getProviderData().get(1));
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showGoogleAuthDialog(account.getEmail());
                                dialog.onContinueClick(new Dialog_Google_Reauth.ContinueWithSameAccount() {
                                    @Override
                                    public void onSingInWithClick(Boolean continueLogin) {
                                        signInWithExistingAccount(idToken, account);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onChangeAccountClick(Boolean changeAccount) {
                                        //Fresh login
                                        googleSignInDialog();
                                        //dismiss();

                                    }
                                });
                            }
                        }
                    }
                });

    }


    private void signInWithExistingAccount(String idToken, GoogleSignInAccount account) {
        FirebaseAuth.getInstance().signOut();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "onComplete: " + user.getPhoneNumber() + user.getEmail());
                            startMainActivity();
                        }
                    }
                });

    }

    private void showGoogleAuthDialog(String email) {
        dialog = Dialog_Google_Reauth.newInstance(email);
        dialog.setCancelable(true);
        dialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(),
                dialog.getTag());
    }

    private void writeUserData(UserInfo user) {

        // TODO: 20-10-2021 ServerWrite
        Map map = new HashMap();
        map.put(kMap.email, user.getEmail());
        map.put(kMap.name, user.getDisplayName());
        if (user.getPhotoUrl() != null) {
            map.put(kMap.image, user.getPhotoUrl().toString());
        } else map.put(kMap.image, "");
        if (user.getDisplayName() != null) {
            map.put(kMap.name_small, user.getDisplayName());
        }
        map.put(kMap.timestamp, FieldValue.serverTimestamp());
        map.put(kMap.firebase_id, user.getUid());
        map.put(kMap.type, kMap.student);
        map.put(kMap.registered_date, FieldValue.serverTimestamp());
        Reference.userRef().document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startMainActivity();
                    }
                });
    }

    private void startMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}