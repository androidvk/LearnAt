package com.coremacasia.learnat;

import android.content.Intent;
import android.os.Bundle;

import com.coremacasia.learnat.databinding.ActivityMainBinding;
import com.coremacasia.learnat.startItems.DF_SubjectChooser;
import com.coremacasia.learnat.startItems.Splash;
import com.coremacasia.learnat.utility.Reference;
import com.coremacasia.learnat.utility.kMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

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
            getUserData();
        }
    }

    private void getUserData() {
        DocumentReference userRef = Reference.userRef().document(firebaseUser.getUid());
        userRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (!value.exists()) {
                startSubjectChooser(1);
            }else {
                if(value.get(kMap.preferred_type1)==null){
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