package com.coremacasia.learnat.commons.all_courses;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;

public class AllCoursesViewModel extends ViewModel implements AllCoursesRepo.OnFirestoreTaskComplete {
    private static final String TAG = "CommonListViewModel";
    private AllCoursesRepo AllCoursesRepo = new AllCoursesRepo(this);

    public LiveData<CourseModel> getCommonMutableLiveData(DocumentReference documentReference) {
        AllCoursesRepo.getCommonData(documentReference);
        return commonMutableLiveData;
    }

    private MutableLiveData<CourseModel> commonMutableLiveData = new MutableLiveData<>();

    public AllCoursesViewModel() {
        //firebaseRepo.getCommonData(documentReference);
    }

    @Override
    public void commonData(CourseModel CourseModel) {
        commonMutableLiveData.setValue(CourseModel);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ", e);
    }
}
