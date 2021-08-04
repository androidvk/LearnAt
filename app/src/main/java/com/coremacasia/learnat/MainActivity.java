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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBar.LayoutParams lp;
    private View itemView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

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
            //startSubjectChooser(2);
            getLoginInfo();
            getData();
        }
    }

    private DocumentReference commonListRef;
    private CommonDataViewModel viewModel;
    private AllCoursesViewModel allCoursesViewModel;
    private CategoryViewModel categoryViewModel;



    private String CAT;

    private void getLoginInfo() {
        DocumentReference userRef = Reference.userRef().document(firebaseUser.getUid());
        userRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (!value.exists()) {
                startSubjectChooser(1);
            } else {
                if (value.get(kMap.preferred_type1) == null) {
                    startSubjectChooser(2);
                } else {

                    CAT = value.get(kMap.preferred_type1).toString();
                    getUserData();
                    //getCategoryData();
                }
            }

        });

    }

    private void getUserData() {
        DocumentReference userRef = Reference.userRef(firebaseUser.getUid());
        Log.e(TAG, "getUserData: "+firebaseUser.getUid() );
        UserDataViewModel viewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        viewModel.getMutableLiveData(userRef).observe(this, new Observer<UserHelper>() {
            @Override
            public void onChanged(UserHelper userHelper) {
                Log.e(TAG, "onChanged: "+userHelper.getFirebase_id() );
                MyStore.setUserData(userHelper);
                getCategoryData();
            }
        });
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
    private void getCategoryData() {
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