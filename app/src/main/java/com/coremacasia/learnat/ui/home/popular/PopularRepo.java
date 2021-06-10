package com.coremacasia.learnat.ui.home.popular;

import android.util.Log;

import com.coremacasia.learnat.commons.CommonData;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.FirebaseRepo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PopularRepo {
    private static final String TAG = "PopularRepo";
    private PopularRepo.OnFirestoreTaskComplete onFirestoreTaskComplete;

    public PopularRepo(PopularRepo.OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        Log.e(TAG, "getCommonData:  New Fetch");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value.exists()) {
                    onFirestoreTaskComplete.commonData(value
                            .toObject(CommonDataModel.class));
                } else {
                    onFirestoreTaskComplete.error(error);
                }
            }
        });

    }

    public interface OnFirestoreTaskComplete {
        void commonData(CommonDataModel commonDataModel);

        void error(Exception e);
    }
}
