package com.coremacasia.learnat.commons;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CommonListRepo {
    private static final String TAG = "FirebaseRepo";
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    public CommonListRepo(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value!=null && value.exists()) {
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
