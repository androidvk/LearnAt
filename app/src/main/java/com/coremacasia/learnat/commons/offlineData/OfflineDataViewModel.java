package com.coremacasia.learnat.commons.offlineData;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OfflineDataViewModel extends ViewModel implements OfflineRepo.OnTaskComplete {
    private static final String TAG = "OfflineDataViewModel";
    private OfflineRepo offlineRepo=new OfflineRepo(this);
    private MutableLiveData<XtraHelper> mutableLiveData=new MutableLiveData<>();
    public LiveData<XtraHelper> getMutableLiveData(){
             offlineRepo.getData();
            return mutableLiveData;
    }
    @Override
    public void data(XtraHelper helper) {
        mutableLiveData.setValue(helper);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ",e );
    }
}
