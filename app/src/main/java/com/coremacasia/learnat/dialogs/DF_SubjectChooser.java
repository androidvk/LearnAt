package com.coremacasia.learnat.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.MainActivity;
import com.coremacasia.learnat.R;
import com.coremacasia.learnat.adapters.CourseMenuAdapter;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.utility.RMAP;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DF_SubjectChooser extends DialogFragment {
    public final static String TAG = "DF_SubjectChooser";
    private static int from;
    private View view;
    private RecyclerView recyclerView;
    private Group gCongrats, gIcode, gSubjects;
    private Button bApply, bDontHaveCode, bLetsBegin;

    public static DF_SubjectChooser newInstance(int from) {
        DF_SubjectChooser.from = from;
        Bundle args = new Bundle();
        DF_SubjectChooser fragment = new DF_SubjectChooser();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getTheme() {
        return R.style.Theme_LearnAt_FullScreenDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_subject_chooser, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        setCancelable(false);
        gCongrats = view.findViewById(R.id.gCongrats);
        gIcode = view.findViewById(R.id.gIcode);
        gSubjects = view.findViewById(R.id.gSubjects);
        bApply = view.findViewById(R.id.button2);
        bDontHaveCode = view.findViewById(R.id.button3);
        bLetsBegin = view.findViewById(R.id.button4);
        gSubjects.setVisibility(View.GONE);
        gIcode.setVisibility(View.GONE);
        gCongrats.setVisibility(View.GONE);
        return view;
    }

    CourseHelper helper;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (from == 1) {
            Log.e(TAG, "Google Sign In" );
            startGoogleSignIn();

        } else if (from == 2) {
            gSubjects.setVisibility(View.VISIBLE);
            Log.e(TAG, "Subject Chooser" );
        }
      /*  DocumentReference reference = Reference.superRef(RMAP.list);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    helper = documentSnapshot.toObject(CourseHelper.class);
                    ArrayList<CategoryHelper> listHelper = helper.getCategory();
                    Log.e(TAG, "onSuccess: " + listHelper.get(0).getDescription());


                }
            }
        });*/
        setRecyclerView();
        onClicks();
    }

    private void onClicks() {
        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 02-06-2021 Check for Validation
                gCongrats.setVisibility(View.VISIBLE);
                gIcode.setVisibility(View.GONE);
            }
        });
        bDontHaveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(in);
            }
        });
        bLetsBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                Intent in = new Intent(getContext(), MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(in);

            }
        });
    }
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 9001;

    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
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
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    FirebaseUser phoneUser = FirebaseAuth.getInstance().getCurrentUser();

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            writeUserData(user);
                        } else {
                            Log.e(TAG, "linkWithCredential:failure", task.getException());
                            //FirebaseAuth.getInstance().signOut();
                        }
                    }
                });

    }

    private void writeUserData(FirebaseUser userInfo) {
        // TODO: 01-06-2021 Server Side
        UserInfo user = phoneUser.getProviderData().get(1);
        Map map = new HashMap();
        map.put(kMap.name, user.getDisplayName());
        map.put(kMap.image, user.getPhotoUrl().toString());
        map.put(kMap.name_small, user.getDisplayName().toLowerCase());
        map.put(kMap.firebase_id, user.getUid());
        map.put(kMap.m_number, phoneUser.getPhoneNumber());
        map.put(kMap.timestamp, FieldValue.serverTimestamp());
        map.put(kMap.registered_date, FieldValue.serverTimestamp());
        map.put(kMap.email, user.getEmail());
        Reference.userRef().document(phoneUser.getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

        gSubjects.setVisibility(View.VISIBLE);

    }

    private CommonDataViewModel viewModel;
    DocumentReference commonListRef = Reference.superRef(RMAP.list);

    private void setRecyclerView() {


        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CourseMenuAdapter adapter = new CourseMenuAdapter(getActivity(), gSubjects, gIcode);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(getViewLifecycleOwner(), new Observer<CommonDataModel>() {
            @Override
            public void onChanged(CommonDataModel commonDataModel) {
                adapter.setCommonDataModel(commonDataModel);
                adapter.notifyDataSetChanged();
            }
        });


    }
}
