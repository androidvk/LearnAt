package com.coremacasia.learnat.commons.user_repo;

import com.coremacasia.learnat.helpers.UserHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserRepo {
    private static final String TAG = "FirebaseRepo";
    private UserRepo.OnFirestoreTaskComplete onFirestoreTaskComplete;
    public UserRepo(UserRepo.OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommonData(DocumentReference documentReference) {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (value!=null && value.exists()) {
                    onFirestoreTaskComplete.commonData(value
                            .toObject(UserHelper.class));

                } else {
                    onFirestoreTaskComplete.error(error);
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void commonData(UserHelper userHelper);
        void error(Exception e);
    }
}
