package com.coremacasia.learnat.commons;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;

public class CommonDataViewModel extends ViewModel implements CommonListRepo.OnFirestoreTaskComplete {
    private static final String TAG = "CommonListViewModel";
    private CommonListRepo commonListRepo = new CommonListRepo(this);

    public LiveData<CommonDataModel> getCommonMutableLiveData(DocumentReference documentReference) {
        commonListRepo.getCommonData(documentReference);
        return commonMutableLiveData;
    }

    private MutableLiveData<CommonDataModel> commonMutableLiveData = new MutableLiveData<>();

    public CommonDataViewModel() {
        //firebaseRepo.getCommonData(documentReference);
    }

    @Override
    public void commonData(CommonDataModel commonDataModel) {
        commonMutableLiveData.setValue(commonDataModel);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ", e);
    }
}
