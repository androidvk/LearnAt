package com.coremacasia.learnat.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coremacasia.learnat.MainActivity;
import com.coremacasia.learnat.R;
import com.coremacasia.learnat.helpers.UserHelper;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class GoogleSignInDialog extends BottomSheetDialogFragment {
    public static final String TAG = "GoogleSignInDialog";
    private static String from;

    public static final int RC_SIGN_IN = 9001;
    private Dialog_Google_Reauth dialog;

    public static GoogleSignInDialog newInstance(String from) {
        GoogleSignInDialog.from = from;
        Bundle args = new Bundle();
        GoogleSignInDialog fragment = new GoogleSignInDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGoogleSignIn();
    }

    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getActivity().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.revokeAccess();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (from.equals("mainActivity")) {
                    linkAccounts(account.getIdToken(), account);
                } else if (from.equals("signIn")) {
                    signIn(account.getIdToken());
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                dismiss();
            }
        }
    }

    private void signIn(String idToken) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference userRef = Reference.userRef(user.getUid());
                            userRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    UserHelper helper = task.getResult().toObject(UserHelper.class);
                                    if (helper != null) {
                                        //start Main Activity
                                        startMainActivity();
                                    } else {
                                        // TODO: 20-10-2021 Check phone sign in or not
                                        writeUserData(user);
                                    }

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
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
                            Log.e(TAG, "onComplete: " + user.getPhoneNumber() + user.getEmail());
                            dismiss();
                            startMainActivity();
                            //writeUserData(user);
                        }
                    }
                });

    }

    private void showGoogleAuthDialog(String email) {
        dialog = Dialog_Google_Reauth.newInstance(email);
        dialog.setCancelable(false);
        dialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(),
                dialog.getTag());
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
                                        startGoogleSignIn();
                                        //dismiss();

                                    }
                                });
                            }
                        }
                    }
                });

    }

    private void writeUserData(FirebaseUser user) {
        // TODO: 01-06-2021 Server Side

        Map map = new HashMap();

        map.put(kMap.name, user.getDisplayName());
        map.put(kMap.image, user.getPhotoUrl().toString());
        map.put(kMap.name_small, user.getDisplayName().toLowerCase());
        map.put(kMap.firebase_id, user.getUid());
        map.put(kMap.type, kMap.student);
        map.put(kMap.phone_registration, false);
        //map.put(kMap.m_number, phoneUser.getPhoneNumber());
        map.put(kMap.timestamp, FieldValue.serverTimestamp());
        map.put(kMap.registered_date, FieldValue.serverTimestamp());
        map.put(kMap.email, user.getEmail());

        Reference.userRef().document(user.getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startMainActivity();

                    }
                });
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
                        dismiss();
                        startMainActivity();
                    }
                });
    }

    private void startMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
