package com.coremacasia.learnat.ui.home.popular;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.google.firebase.firestore.DocumentReference;

public class PopularViewModel extends ViewModel implements PopularRepo.OnFirestoreTaskComplete {
    private static final String TAG = "PopularViewModel";
    PopularRepo popularRepo=new PopularRepo(this);
    private MutableLiveData<CommonDataModel> mutableLiveData = new MutableLiveData<>();
    public LiveData<CommonDataModel> getMutableLiveData(DocumentReference documentReference){
        popularRepo.getCommonData(documentReference);
        return mutableLiveData;
    }

    @Override
    public void commonData(CommonDataModel commonDataModel) {
        mutableLiveData.setValue(commonDataModel);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ",e );
    }
}
