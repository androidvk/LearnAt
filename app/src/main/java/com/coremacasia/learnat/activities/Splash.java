package com.coremacasia.learnat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coremacasia.learnat.MainActivity;
import com.coremacasia.learnat.R;
import com.coremacasia.learnat.dialogs.DF_link_phone;
import com.coremacasia.learnat.dialogs.DF_number;
import com.coremacasia.learnat.dialogs.Dialog_Phone_Reauth;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class Splash extends AppCompatActivity {
    private static final String TAG = "Splash";
    private TextView tSignIn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        tSignIn = findViewById(R.id.textView21);
        progressBar = findViewById(R.id.progressBar2);
        setViews();

        Button bGetStarted = findViewById(R.id.bContinue);
        bGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        tSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoogleSignIn();
                tSignIn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    public void showBottomSheet() {
        DF_number df_number =
                DF_number.newInstance().newInstance();
        df_number.show(getSupportFragmentManager(),
                DF_number.TAG);
    }

    private void setViews() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }

    public static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Splash.this, gso);
        mGoogleSignInClient.revokeAccess();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.e(TAG, "onActivityResult:1 ");
            progressBar.setVisibility(View.INVISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                progressBar.setVisibility(View.INVISIBLE);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressBar.setVisibility(View.INVISIBLE);
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                tSignIn.setVisibility(View.VISIBLE);
                tSignIn.setText(getString(R.string.signInFailed) + " " + e.getMessage());

            }
        }
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

    private void checkPhoneAuth(FirebaseUser user) {
        FirebaseUser info = mAuth.getCurrentUser();
        if (info.getPhoneNumber() == null || info.getPhoneNumber().equals("")) {
            showPhoneLinkDialog();
        } else if (info.getEmail() == null || info.getEmail().equals("")) {
            //googleSignInDialog();
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void writeUserData(FirebaseUser user) {
        progressBar.setVisibility(View.VISIBLE);
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

       /* for (UserInfo info : user.getProviderData()) {
            if (info.getPhoneNumber() != null ) {
                map.put(kMap.phone_registration, true);
                map.put(kMap.m_number, info.getPhoneNumber());
                break;
            }
        }*/

        Reference.userRef().document(user.getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        checkPhoneAuth(user);

                    }
                });
    }

    private void showPhoneLinkDialog() {
        DF_link_phone dialog = DF_link_phone.newInstance();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), DF_link_phone.TAG);



    }




}