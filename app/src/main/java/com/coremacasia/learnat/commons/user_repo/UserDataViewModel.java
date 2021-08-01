package com.coremacasia.learnat.commons.user_repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coremacasia.learnat.helpers.UserHelper;
import com.google.firebase.firestore.DocumentReference;

public class UserDataViewModel extends ViewModel implements UserRepo.OnFirestoreTaskComplete {
    private static final String TAG = "UserDataViewModel";
    private UserRepo userRepo =new UserRepo(this);
    private MutableLiveData<UserHelper> modelMutableLiveData=new MutableLiveData<>();
    public LiveData<UserHelper> getMutableLiveData(DocumentReference documentReference) {
        userRepo.getCommonData(documentReference);
        return modelMutableLiveData;
    }
    @Override
    public void commonData(UserHelper userHelper) {
        modelMutableLiveData.setValue(userHelper);
    }

    @Override
    public void error(Exception e) {

    }
}
