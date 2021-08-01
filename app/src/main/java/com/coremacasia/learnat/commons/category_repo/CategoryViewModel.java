package com.coremacasia.learnat.commons.category_repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.google.firebase.firestore.DocumentReference;

public class CategoryViewModel extends ViewModel implements CategoryRepo.OnFirestoreTaskComplete {
    private static final String TAG = "CommonListViewModel";
    private CategoryRepo CategoryRepo = new CategoryRepo(this);

    public LiveData<CategoryDashboardHelper> getCategoryMutableData(DocumentReference documentReference) {
        CategoryRepo.getCommonData(documentReference);
        return commonMutableLiveData;
    }

    private MutableLiveData<CategoryDashboardHelper> commonMutableLiveData = new MutableLiveData<>();

    public CategoryViewModel() {
        //firebaseRepo.getCommonData(documentReference);
    }

    @Override
    public void commonData(CategoryDashboardHelper categoryDashboardHelper) {
        commonMutableLiveData.setValue(categoryDashboardHelper);
    }

    @Override
    public void error(Exception e) {
        Log.e(TAG, "error: ", e);
    }
}
