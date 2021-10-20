package com.coremacasia.learnat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coremacasia.learnat.MainActivity;
import com.coremacasia.learnat.R;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class PhoneAuth extends AppCompatActivity {
    private static final String TAG = "PhoneAuth";
    private String NUMBER;
    private ImageView i_back;
    private TextView tNumber, tOtpInfo, tTimer, tOtpStatus;
    private ProgressBar progressBar;
    private OtpTextView eOtp;
    private Button bVerify;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private boolean mNewUser = false;
    private String from;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        getIds();
        tTimer.setVisibility(View.GONE);
        tOtpStatus.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        processAuth();

        startPhoneNumberVerification(NUMBER);

        i_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void processAuth() {
        mAuth = FirebaseAuth.getInstance();
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                eOtp.showSuccess();
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
                Snackbar.make(findViewById(android.R.id.content), "Unable to Send OTP.",
                        Snackbar.LENGTH_LONG).show();
                tOtpInfo.setText(R.string.unableToSendOTP);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    tOtpStatus.setVisibility(View.VISIBLE);
                    tOtpStatus.setText(R.string.invalidPhoneNumber);
                    tOtpInfo.setVisibility(View.GONE);
                    tTimer.setVisibility(View.GONE);
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    tOtpStatus.setVisibility(View.VISIBLE);
                    tOtpStatus.setText(R.string.YouHaveReachedMaxLimit);
                    tOtpInfo.setVisibility(View.GONE);
                    tTimer.setVisibility(View.GONE);
                    // [END_EXCLUDE]
                }


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                tTimer.setVisibility(View.VISIBLE);
                tOtpInfo.setText(R.string.resendIn);
                tOtpInfo.setVisibility(View.VISIBLE);
                countdownTimer();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
        bVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tOtpStatus.setVisibility(View.INVISIBLE);
                if (eOtp.getOTP().length() == 6) {
                    verifyPhoneNumberWithCode(mVerificationId, eOtp.getOTP());
                    closeKeyboard();
                } else {
                    Toast.makeText(PhoneAuth.this, R.string.enterOtp, Toast.LENGTH_SHORT).show();
                }


            }
        });

        eOtp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                tOtpStatus.setVisibility(View.INVISIBLE);
                verifyPhoneNumberWithCode(mVerificationId, eOtp.getOTP());
                closeKeyboard();
            }
        });

    }

    private void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

    private void countdownTimer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long duration) {
                //tTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                // Duration
                long Mmin = (duration / 1000) / 60;
                long Ssec = (duration / 1000) % 60;
                if (Ssec < 10) {
                    tTimer.setText("" + Mmin + ":0" + Ssec);
                } else tTimer.setText("" + Mmin + ":" + Ssec);
            }

            public void onFinish() {
                tTimer.setText(getResources().getString(R.string.resend));
                tOtpInfo.setVisibility(View.GONE);
                tTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendVerificationCode
                                (NUMBER, mResendToken);
                    }
                });
            }

        }.start();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        mVerificationInProgress = true;
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);            // ForceResendingToken from callbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        eOtp.resetState();
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [END resend_verification]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Map map = new HashMap();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser phoneUser = task.getResult().getUser();
                        // TODO: 20-10-2021 Write on Server
                        map.put(kMap.m_number, phoneUser.getPhoneNumber());
                        map.put(kMap.phone_registration, true);
                        final Handler handler = new Handler();
                        //
                        Reference.userRef().document(auth.getCurrentUser().getUid())
                                .set(map, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(PhoneAuth.this,
                                        MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        }, 1000);

                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            progressBar.setVisibility(View.GONE);
                            tOtpStatus.setVisibility(View.VISIBLE);
                            eOtp.showError();
                        }
                    }
                }
            });
        } else {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                DocumentReference userRef = Reference.userRef(user.getUid());
                                userRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        UserHelper helper = task.getResult().toObject(UserHelper.class);
                                        if (helper != null) {
                                            //start Main Activity

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms
                                                    progressBar.setVisibility(View.GONE);
                                                    startActivity(new Intent(PhoneAuth.this,
                                                            MainActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                }
                                            }, 1000);

                                        } else {
                                            // TODO: 20-10-2021 Server Side Write
                                            writeUserData(user);
                                        }

                                    }
                                });

                            } else {
                                // Sign in failed, display a message and update the UI
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    progressBar.setVisibility(View.GONE);
                                    tOtpStatus.setVisibility(View.VISIBLE);
                                    eOtp.showError();
                                }
                            }
                        }
                    });
        }
    }
    private void writeUserData(FirebaseUser userInfo) {
        // TODO: 01-06-2021 Server Side
        Map map = new HashMap();
        map.put(kMap.firebase_id, userInfo.getUid());
        map.put(kMap.m_number, userInfo.getPhoneNumber());
        map.put(kMap.timestamp, FieldValue.serverTimestamp());
        map.put(kMap.registered_date, FieldValue.serverTimestamp());
        Reference.userRef().document(userInfo.getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(PhoneAuth.this,
                                MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                });

    }
    private void getIds() {
        i_back = findViewById(R.id.iBack);
        tNumber = findViewById(R.id.tNumber);
        tOtpStatus = findViewById(R.id.tOtpStatus);
        tTimer = findViewById(R.id.t_timer);
        bVerify = findViewById(R.id.bContinue);
        eOtp = findViewById(R.id.eOtp);
        progressBar = findViewById(R.id.progressBar);
        NUMBER = getIntent().getStringExtra("number");
        tOtpInfo = findViewById(R.id.textView10);
        setFullView();

        tNumber.setText(NUMBER);
    }

    private void setFullView() {
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
}