package com.coremacasia.learnat.commons.category_repo;

import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CategoryRepo {
    private static final String TAG = "CompExamRepo";
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    public CategoryRepo(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value!=null && value.exists()) {
                    onFirestoreTaskComplete.commonData(value
                            .toObject(CategoryDashboardHelper.class));
                    
                } else {
                    onFirestoreTaskComplete.error(error);
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void commonData(CategoryDashboardHelper categoryDashboardHelper);
        void error(Exception e);
    }
}
