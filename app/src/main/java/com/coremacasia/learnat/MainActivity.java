package com.coremacasia.learnat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.commons.all_courses.AllCoursesViewModel;
import com.coremacasia.learnat.commons.all_courses.CourseModel;
import com.coremacasia.learnat.commons.category_repo.CategoryViewModel;
import com.coremacasia.learnat.dialogs.DF_link_phone;
import com.coremacasia.learnat.dialogs.Dialog_Google_Reauth;
import com.coremacasia.learnat.dialogs.GoogleSignInDialog;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.UserHelper;
import com.coremacasia.learnat.commons.user_repo.UserDataViewModel;
import com.coremacasia.learnat.databinding.ActivityMainBinding;
import com.coremacasia.learnat.dialogs.DF_SubjectChooser;
import com.coremacasia.learnat.activities.Splash;
import com.coremacasia.learnat.utility.MyStore;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBar.LayoutParams lp;
    private View itemView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference commonListRef;
    private CommonDataViewModel viewModel;
    private AllCoursesViewModel allCoursesViewModel;
    private CategoryViewModel categoryViewModel;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        HeaderFooter();

    }

    private void HeaderFooter() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_my_profile)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main2);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        itemView = LayoutInflater.from(this).inflate(R.layout.actionbar_main, null);
        actionBar.setCustomView(itemView, lp);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(MainActivity.this,
                    Splash.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else {
            //mAuth.signOut();
            getLoginInfo();
            getData();
        }
    }


    private void getLoginInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.getPhoneNumber() == null || user.getPhoneNumber().equals("")) {
            showPhoneLinkDialog();
        } else if (user.getEmail() == null || user.getEmail().equals("")) {
            //startSubjectChooser(1);
            googleSignInDialog();
        }

        DocumentReference userRef = Reference.userRef().document(firebaseUser.getUid());
        userRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get(kMap.preferred_type1) == null
                ||task.getResult().get(kMap.preferred_type1).equals("")) {
                       // startSubjectChooser(2);
                    } else {
                        String CAT = task.getResult().get(kMap.preferred_type1).toString();
                        getUserData(CAT);
                        //getCategoryData();
                    }

            }
        });


    }

    private void getUserData(String CAT) {
        DocumentReference userRef = Reference.userRef(firebaseUser.getUid());
        UserDataViewModel viewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        viewModel.getMutableLiveData(userRef).observe(this, new Observer<UserHelper>() {
            @Override
            public void onChanged(UserHelper userHelper) {
                MyStore.setUserData(userHelper);
                getCategoryData(CAT);
            }
        });

    }

    private void showPhoneLinkDialog() {
        Log.e(TAG, "showPhoneLinkDialog: ");
        DF_link_phone dialog = DF_link_phone.newInstance();
        //dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), DF_link_phone.TAG);

    }

    public static final int RC_SIGN_IN = 9001;
    private Dialog_Google_Reauth dialog;
    private void googleSignInDialog() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
        dialog.show(getSupportFragmentManager(),
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
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    private void getData() {
        commonListRef = Reference.superRef(RMAP.list);
        viewModel = new ViewModelProvider(this).get(CommonDataViewModel.class);
        viewModel.getCommonMutableLiveData(commonListRef).observe(MainActivity.this,
                new Observer<CommonDataModel>() {
                    @Override
                    public void onChanged(CommonDataModel commonDataModel) {
                        MyStore.setCommonData(commonDataModel);
                    }
                });

        allCoursesViewModel = new ViewModelProvider(this).get(AllCoursesViewModel.class);
        allCoursesViewModel.getCommonMutableLiveData(Reference.superRef(RMAP.all_courses))
                .observe(MainActivity.this, new Observer<CourseModel>() {
                    @Override
                    public void onChanged(CourseModel courseModel) {
                        MyStore.setCourseData(courseModel);
                    }
                });


    }

    private void getCategoryData(String CAT) {
        DocumentReference categoryRef = Reference.superRef(CAT);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryMutableData(categoryRef).observe(this,
                new Observer<CategoryDashboardHelper>() {
                    @Override
                    public void onChanged(CategoryDashboardHelper categoryDashboardHelper) {
                        MyStore.setCategoryDashboardHelper(categoryDashboardHelper);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mLogout) {

        }
        return super.onOptionsItemSelected(item);

    }

    private void startSubjectChooser(int From) {
        DF_SubjectChooser df_number =
                DF_SubjectChooser.newInstance(From);
        df_number.show(getSupportFragmentManager(),
                DF_SubjectChooser.TAG);
    }
}