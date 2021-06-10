package com.coremacasia.learnat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.CommonDataViewModel;
import com.coremacasia.learnat.databinding.ActivityMainBinding;
import com.coremacasia.learnat.startItems.DF_SubjectChooser;
import com.coremacasia.learnat.startItems.Splash;
import com.coremacasia.learnat.utility.RMAP;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

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
    private CommonDataViewModel viewModel;

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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
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
            getUserData();
        }
    }

    private void getUserData() {
        DocumentReference userRef = Reference.userRef().document(firebaseUser.getUid());
        userRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (!value.exists()) {
                startSubjectChooser(1);
            } else {
                if (value.get(kMap.preferred_type1) == null) {
                    startSubjectChooser(2);
                }
            }

        });

    }

    private void startSubjectChooser(int From) {
        DF_SubjectChooser df_number =
                DF_SubjectChooser.newInstance(From);
        df_number.show(getSupportFragmentManager(),
                DF_SubjectChooser.TAG);
    }
}